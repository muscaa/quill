package quill.core.pkg.local;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

import quill.core.QException;
import quill.core.pkg.QPackage;
import quill.core.pkg.QVersion;

public class LocalPackage implements QPackage {
	
	private final String author;
	private final String id;
	private final QVersion version;
	private final Set<String> dependencies;
	private final String qClass;
	
	public LocalPackage(File dir) throws QException {
		if (!dir.exists()) throw new QException("Package '" + dir.getName() + "' doesn't exist!");
		
		try {
			List<String> lines = Files.readAllLines(new File(dir, "info.q").toPath());
			this.author = lines.get(0);
			this.id = lines.get(1);
			this.version = new QVersion(lines.get(2));
			String[] split = lines.get(3).split(",");
			this.dependencies = split[0].equals("null") ? Set.of() : Set.of(lines.get(3).split(","));
			this.qClass = lines.get(4);
		} catch (Exception e) {
			throw new QException(e);
		}
	}
	
	@Override
	public String getAuthor() {
		return author;
	}
	
	@Override
	public String getID() {
		return id;
	}
	
	@Override
	public QVersion getVersion() {
		return version;
	}
	
	@Override
	public Set<String> getDependencies() {
		return dependencies;
	}
	
	@Override
	public String getQClass() {
		return qClass;
	}
}
