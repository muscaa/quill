package quill.local;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Objects;

import fluff.core.utils.StringUtils;
import quill.IPackage;
import quill.QFiles;
import quill.bootstrap.QuillBootstrap;
import quill.info.Tag;
import quill.info.Version;

public class LocalPackage implements IPackage {

	private final String namespace;
	private final String author;
	private final String id;
	private final Version version;
	private final String description;
	private final File dir;

	public LocalPackage(String namespace, String author, String id, Version version, String description) {
		this.namespace = namespace;
		this.author = author;
		this.id = id;
		this.version = version;
		this.description = description;
		this.dir = new File(QFiles.PACKAGES, Tag.of(this).toString());
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
		File dir = new File(getDir(), "java");
		if (!dir.isDirectory())
			return false;

		QuillBootstrap.addLibrary(dir);

		return true;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Version getVersion() {
		return version;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public File getDir() {
		return dir;
	}

	@Override
	public String toString() {
		return StringUtils.format(
				"LocalPackage(namespace=\"${}\", author=\"${}\", id=\"${}\", version=\"${}\", description=\"${}\", dir=\"${}\")",
				namespace, author, id, version, description, dir);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LocalPackage lp)) {
			return false;
		}
		return namespace.equals(lp.namespace) && author.equals(lp.author) && id.equals(lp.id)
				&& version.equals(lp.version) && description.equals(lp.description) && dir.equals(lp.dir);
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, author, id, version, description, dir);
	}

//	public static QPackage fromFile(File file) {
//		Path path = QFiles.PACKAGES.toPath().relativize(file.getAbsoluteFile().toPath());
//
//		if (path.getNameCount() < 2 || path.isAbsolute()) {
//			throw new IllegalArgumentException("Invalid install location");
//		}
//
//		String namespace = path.getName(0).toString();
//		String[] split = split(path.getName(1).toString());
//		String author = split[1];
//		String id = split[2];
//
//		if (id == null || author == null) {
//			throw new IllegalArgumentException("Invalid install location");
//		}
//
//		return new QPackage(namespace, author, id);
//	}
}
