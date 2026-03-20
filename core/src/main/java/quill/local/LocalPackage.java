package quill.local;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fluff.core.utils.StringUtils;
import quill.bootstrap.QuillBootstrap;

public class LocalPackage {

	public static final Map<String, LocalPackage> PACKAGES = packages(QFiles.PACKAGES);

	public final String namespace;
	public final String author;
	public final String id;
	public final String spec;
	public final String tag;

	public LocalPackage(String namespace, String author, String id) {
		this.namespace = namespace;
		this.author = author;
		this.id = id;

		spec = author + "@" + id;
		tag = namespace + "/" + spec;
	}

	public boolean run(String mainClass, String[] args) {
		if (!include())
			return false;

		try {
			Class<?> clazz = QuillBootstrap.LOADER.loadClass(mainClass);
			Method main = clazz.getDeclaredMethod("main", LocalPackage.class, String[].class);
			main.setAccessible(true);
			main.invoke(null, new Object[] { this, args });

			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return false;
	}

	public boolean include() {
		File path = getPath();
		if (!path.isDirectory())
			return false;

		QuillBootstrap.addLibrary(path);

		return true;
	}

	public File getPath() {
		return new File(QFiles.PACKAGES, namespace + "/" + spec);
	}

	@Override
	public String toString() {
		return StringUtils.format("QPackage(namespace=\"${}\", author=\"${}\", id=\"${}\")", namespace, author, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LocalPackage p))
			return false;

		return namespace.equals(p.namespace) && author.equals(p.author) && id.equals(p.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, author, id);
	}

	public static String[] split(String str) {
		String[] split1 = str.split("/");
		if (split1.length > 2)
			return new String[] { null, null, null };

		String[] split2 = split1[split1.length - 1].split("@");
		if (split2.length > 2)
			return new String[] { null, null, null };

		String namespace = split1.length == 2 ? split1[0] : null;
		String author = split2.length == 2 ? split2[0] : null;
		String id = split2[split2.length - 1];

		return new String[] { namespace, author, id };
	}

	public static Map<String, LocalPackage> packages(File dir) {
		return packages(dir, null);
	}

	public static Map<String, LocalPackage> packages(File dir, List<LocalRepository> repositories) {
		Map<String, LocalPackage> map = new LinkedHashMap<>();

		List<LocalRepository> repoList = repositories != null ? repositories : LocalRepository.REPOSITORIES;
		Map<String, Integer> priorityMap = new LinkedHashMap<>();
		int ri = 0;
		for (LocalRepository repo : repoList) {
			priorityMap.putIfAbsent(repo.namespace, ri++);
		}
		int defaultPriority = priorityMap.size();

		File[] sortedSubdirs = dir.listFiles(File::isDirectory);
		Arrays.sort(sortedSubdirs,
				Comparator.comparingInt(file -> priorityMap.getOrDefault(file.getName(), defaultPriority)));

		for (File namespaceFile : sortedSubdirs) {
			String namespace = namespaceFile.getName();

			for (File specFile : namespaceFile.listFiles(File::isDirectory)) {
				String[] split = split(specFile.getName());
				String author = split[1];
				String id = split[2];

				if (id == null || author == null)
					continue;

				LocalPackage pkg = new LocalPackage(namespace, author, id);
				map.put(pkg.tag, pkg);
			}
		}

		return map;
	}

	public static LocalPackage find(String str) {
		return find(str, null);
	}

	public static LocalPackage find(String str, Map<String, LocalPackage> packages) {
		String[] split = split(str);
		String namespace = split[0];
		String author = split[1];
		String id = split[2];

		if (id == null)
			return null;

		Map<String, LocalPackage> pkgMap = packages != null ? packages : PACKAGES;
		for (Map.Entry<String, LocalPackage> e : pkgMap.entrySet()) {
			String[] split0 = split(e.getKey());
			String namespace0 = split0[0];
			String author0 = split0[1];
			String id0 = split0[2];

			if (namespace != null && !namespace.equals(namespace0))
				continue;
			if (author != null && !author.equals(author0))
				continue;

			if (id.equals(id0))
				return e.getValue();
		}

		return null;
	}

	public static LocalPackage fromFile(File file) {
		Path path = QFiles.PACKAGES.toPath().relativize(file.getAbsoluteFile().toPath());

		if (path.getNameCount() < 2 || path.isAbsolute()) {
			throw new IllegalArgumentException("Invalid install location");
		}

		String namespace = path.getName(0).toString();
		String[] split = split(path.getName(1).toString());
		String author = split[1];
		String id = split[2];

		if (id == null || author == null) {
			throw new IllegalArgumentException("Invalid install location");
		}

		return new LocalPackage(namespace, author, id);
	}
}
