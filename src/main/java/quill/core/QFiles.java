package quill.core;

import java.io.File;
import java.net.URLDecoder;

public class QFiles {
	
	public static final File ROOT = getRootDir();
	public static final File CALL = new File(System.getProperty("user.dir"));
	
	public static final File BIN = new File(ROOT, "bin");
	public static final File CONFIG = new File(ROOT, "config");
	public static final File PACKAGES = new File(ROOT, "packages");
	public static final File SYSTEM = new File(ROOT, "system");
	
	private static File getRootDir() {
		try {
			String path = Class.forName("quill.loader.QuillLoader")
					.getProtectionDomain()
					.getCodeSource()
					.getLocation()
					.getPath();
			String pathDecoded = URLDecoder.decode(path, "UTF-8");
			
			return new File(pathDecoded) // java
					.getParentFile() // fluff-loader
					.getParentFile() // system
					.getParentFile(); // root
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
