package quill;

import java.io.File;

import fluff.files.Folder;
import quill.bootstrap.QuillBootstrap;

public class QFiles {
	
	public static final File HOME = QuillBootstrap.HOME;
	public static final Folder PACKAGES = new Folder(HOME, "packages");
	public static final Folder CONFIG = new Folder(HOME, "config");
	public static final Folder TEMP = new Folder(HOME, "temp");
	public static final Folder DIST = new Folder(HOME, "dist");
}
