package quill.core;

import fluff.loader.RuntimeClassLoader;
import quill.core.pkg.QPackageManager;
import quill.core.pkg.local.LocalPackageManager;

public class QuillCore {
	
	public static final RuntimeClassLoader LOADER = (RuntimeClassLoader) QuillCore.class.getClassLoader();
	
	public static final QPackageManager LOCAL = new LocalPackageManager();
}
