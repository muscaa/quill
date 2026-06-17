package quill.remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import fluff.bin.IBinaryOutput;
import fluff.bin.stream.BinaryInputStream;
import fluff.bin.stream.BinaryOutputStream;
import fluff.core.utils.StringUtils;
import fluff.crypto.format.ICryptoFormat;
import fluff.crypto.hash.Hashing;
import fluff.json.JSON;
import fluff.json.JSONArray;
import fluff.json.JSONObject;
import quill.AbstractRepository;
import quill.QFiles;
import quill.info.Version;

public class RemoteRepository extends AbstractRepository<RemotePackage> {

	protected final URL url;
	protected final File cache;

	public RemoteRepository(String namespace, URL url) {
		super(namespace);
		this.url = url;
		this.cache = new File(QFiles.DB_REPOS, Hashing.SHA3_256.hash(namespace + " " + url.toString()).format(ICryptoFormat.HEX).String());

		refresh();
	}

	private void fetchPackage(IBinaryOutput out, URI uri) throws IOException {
		URL packageUrl = uri.resolve("package.json").toURL();
		try (InputStream is = packageUrl.openStream()) {
			String json = new String(is.readAllBytes());
			JSONObject pkg = JSON.object(json);

			String id = pkg.getString("id");
			String author = pkg.getString("author");
			String description = pkg.getString("description");
			Version latest = Version.of(pkg.getString("latest"));
			Map<Version, URI> versions = pkg.getObject("versions")
					.getMap()
					.entrySet()
					.stream()
					.collect(Collectors.toMap(
							(e) -> Version.of(e.getKey()),
							(e) -> URI.create((String) e.getValue())));
			
			out.LenString(author);
			out.LenString(id);
			out.LenString(description);
			out.LenString(latest.toString());
			out.Int(versions.size());
			for (Map.Entry<Version, URI> e : versions.entrySet()) {
				out.LenString(e.getKey().toString());
				out.LenString(e.getValue().toString());
			}
			
			packages.add(new RemotePackage(namespace, author, id, latest, description, versions));
		}
	}
	
	private void fetchRepository(Queue<URI> repositoriesQueue, Queue<URI> packagesQueue, URI uri) throws IOException {
		URL repositoryUrl = uri.resolve("repository.json").toURL();
		try (InputStream is = repositoryUrl.openStream()) {
			String json = new String(is.readAllBytes());
			JSONObject repository = JSON.object(json);

			JSONArray packages = repository.getArray("packages");
			for (String pkg : packages.iterate(JSONArray::getString)) {
				packagesQueue.offer(isValidUrl(pkg) ? URI.create(pkg) : uri.resolve("packages/" + pkg + "/"));
			}

			JSONArray repositories = repository.getArray("repositories");
			for (String repo : repositories.iterate(JSONArray::getString)) {
				repositoriesQueue.offer(isValidUrl(repo) ? URI.create(repo) : uri.resolve(repo));
			}
		}
	}

	public void fetch() {
		super.refresh();
		
		try (BinaryOutputStream out = new BinaryOutputStream(new FileOutputStream(cache))) {
			Queue<URI> repositoriesQueue = new ArrayDeque<>();
			Queue<URI> packagesQueue = new ArrayDeque<>();
			
			repositoriesQueue.offer(url.toURI());
			while (!repositoriesQueue.isEmpty()) {
				URI uri = repositoriesQueue.poll();
				
				fetchRepository(repositoriesQueue, packagesQueue, uri);
			}
			
			out.Int(packagesQueue.size());
			while (!packagesQueue.isEmpty()) {
				URI uri = packagesQueue.poll();
				
				fetchPackage(out, uri);
			}
		} catch (URISyntaxException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void refresh() {
		super.refresh();
		
		if (!cache.exists()) {
			return;
		}
		
		try (BinaryInputStream in = new BinaryInputStream(new FileInputStream(cache))) {
			int size = in.Int();
			for (int i = 0; i < size; i++) {
				String author = in.LenString();
				String id = in.LenString();
				String description = in.LenString();
				Version latest = Version.of(in.LenString());
				Map<Version, URI> versions = new HashMap<>();
				
				int versionsSize = in.Int();
				for (int j = 0; j < versionsSize; j++) {
					Version version = Version.of(in.LenString());
					URI uri = URI.create(in.LenString());
					
					versions.put(version, uri);
				}
				
				packages.add(new RemotePackage(namespace, author, id, latest, description, versions));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public URL getUrl() {
		return url;
	}
	
	@Override
	public String toString() {
		return StringUtils.format("RemoteRepository(namespace=\"${}\", url=\"${}\")", namespace, url);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RemoteRepository r)) {
			return false;
		}
		return super.equals(r) && url.equals(r.url);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), url);
	}

	private static boolean isValidUrl(String s) {
		try {
			URI uri = URI.create(s);
			return uri.getScheme() != null && (uri.getScheme().equals("http") || uri.getScheme().equals("https"))
					&& uri.getHost() != null;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
