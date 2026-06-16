package quill.remote;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import fluff.core.utils.StringUtils;
import fluff.json.JSON;
import fluff.json.JSONArray;
import fluff.json.JSONObject;
import quill.AbstractRepository;
import quill.info.Version;

public class RemoteRepository extends AbstractRepository<RemotePackage> {

	protected final URL url;

	public RemoteRepository(String namespace, URL url) {
		super(namespace);
		this.url = url;

		refresh();
	}
	
	private void readPackage(URI uri) throws IOException {
		URL packageUrl = uri.resolve("package.json").toURL();
		try (InputStream is = packageUrl.openStream()) {
			String json = new String(is.readAllBytes());
			JSONObject pkg = JSON.object(json);
			
			String id = pkg.getString("id");
			String author = pkg.getString("author");
			String description = pkg.getString("description");
			String latest = pkg.getString("latest"); // TODO maybe replace whats below with a single package that has multiple versions?
			
			JSONObject versions = pkg.getObject("versions");
			for (Map.Entry<String, String> e : versions.iterate(JSONObject::getString)) {
				String version = e.getKey();
				String url = e.getValue();
				
				packages.add(new RemotePackage(namespace, author, id, Version.of(version), description, URI.create(url).toURL()));
			}
		}
	}

	private void readRepository(URI uri) throws IOException {
		URL repositoryUrl = uri.resolve("repository.json").toURL();
		try (InputStream is = repositoryUrl.openStream()) {
			String json = new String(is.readAllBytes());
			JSONObject repository = JSON.object(json);
			
			JSONArray packages = repository.getArray("packages");
			for (String pkg : packages.iterate(JSONArray::getString)) {
				readPackage(isValidUrl(pkg) ? URI.create(pkg) : uri.resolve("packages/" + pkg + "/"));
			}
			
			JSONArray repositories = repository.getArray("repositories");
			for (String repo : repositories.iterate(JSONArray::getString)) {
				readRepository(isValidUrl(repo) ? URI.create(repo) : uri.resolve(repo));
			}
		}
	}

	@Override
	public void refresh() {
		super.refresh();

		try {
			URI uri = url.toURI();

			readRepository(uri);
		} catch (URISyntaxException | IOException e) {
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
