package quill.core;

import fluff.loader.RuntimeClassLoader;
import quill.core.pkg.local.LocalPackageManager;
import quill.core.pkg.local.QLocalPackageManager;

public class QuillCore {
	
	public static final RuntimeClassLoader LOADER = (RuntimeClassLoader) QuillCore.class.getClassLoader();
	
	public static final QLocalPackageManager LOCAL = new LocalPackageManager();
}
