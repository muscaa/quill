package quill.core;

import java.io.File;

import quill.core.main.QuillCoreMain;

public class QFiles {
	
	public static final File ROOT = QuillCoreMain.INSTALL_DIR;
	
	public static final File BIN = new File(ROOT, "bin");
	public static final File LOADER = new File(ROOT, "loader");
	public static final File PACKAGES = new File(ROOT, "packages");
	public static final File SYSTEM = new File(ROOT, "system");
}
