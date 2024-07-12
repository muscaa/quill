package quill.core.pkg.local;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import quill.core.QException;
import quill.core.QVersion;
import quill.core.pkg.PackageInfo;
import quill.core.pkg.info.PackageInfoReader;

public class LocalPackage implements ILocalPackage {
	
	private final File dir;
	private final String author;
	private final String id;
	private final QVersion version;
	private final Set<String> dependencies;
	private final String mainClass;
	private final PackageInfo info;
	
	public LocalPackage(File dir) throws QException {
		File qpkg_info = new File(dir, "qpkg.info");
		if (!qpkg_info.exists()) throw new QException("Package " + dir.getName() + " doesn't exist!");
		
		this.dir = dir;
		
		try (BufferedReader br = new BufferedReader(new FileReader(qpkg_info))) {
			PackageInfoReader r = new PackageInfoReader(br);
			
			author = r.required("author", "Property missing: author").String();
			id = r.required("id", "Property missing: id").String();
			version = new QVersion(r.optional("version", "0.0.0").String());
			dependencies = new HashSet<>();
			for (String s : r.optional("depends", "").String().split(",")) {
				s = s.trim();
				if (s.isEmpty()) continue;
				
				dependencies.add(s);
			}
			mainClass = r.optional("class").String();
			
			String name = r.optional("info.name").String();
			String description = r.optional("info.description").String();
			
			info = new PackageInfo(name, description);
		} catch (IOException e) {
			throw new QException(e);
		}
	}
	
	@Override
	public File getDir() {
		return dir;
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
	public String getMainClass() {
		return mainClass;
	}
	
	@Override
	public PackageInfo getInfo() {
		return info;
	}
}
