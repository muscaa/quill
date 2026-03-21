package quill.local;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Objects;

import fluff.core.utils.StringUtils;
import fluff.files.FileHelper;
import fluff.json.JSON;
import fluff.json.JSONObject;
import quill.IPackage;
import quill.QFiles;
import quill.bootstrap.QuillBootstrap;
import quill.info.Spec;
import quill.info.Tag;
import quill.info.Version;

public class LocalPackage implements IPackage {

	private final String namespace;
	private final String author;
	private final String id;
	private final Version version;
	private final String description;
	private final File dir;

	public LocalPackage(String namespace, String author, String id, Version version, String description, File dir) {
		this.namespace = namespace;
		this.author = author;
		this.id = id;
		this.version = version;
		this.description = description;
		this.dir = dir;
	}

	public LocalPackage(String namespace, String author, String id, Version version, String description) {
		this(namespace, author, id, version, description,
				new File(QFiles.PACKAGES, Tag.of(namespace, Spec.of(author, id)).toString()));
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
		File dir = getDir();
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

	public static LocalPackage from(File dir, String namespace) {
		File packageFile = new File(dir, "package.json");
		if (!packageFile.exists() || !packageFile.isFile()) {
			return null;
		}

		JSONObject json = JSON.object(FileHelper.read(packageFile).String());

		String id = json.getString("id");
		String author = json.getString("author");
		String version = json.getString("version");
		String description = json.getString("description");
		
		return new LocalPackage(namespace, author, id, Version.of(version), description, dir);
	}

	public static LocalPackage from(File dir) {
		Path path = QFiles.PACKAGES.toPath().relativize(dir.getAbsoluteFile().toPath());
		String namespace = path.getNameCount() != 2 || path.isAbsolute() ? null : path.getName(0).toString();

		return from(dir, namespace);
	}
}
