package quill.core;

import java.lang.reflect.Method;
import java.util.List;

import fluff.loader.RuntimeClassLoader;
import quill.core.pkg.local.ILocalPackage;
import quill.core.pkg.local.AbstractLocalPackageManager;
import quill.core.pkg.local.LocalPackageManager;

public class QuillCore {
	
	public static final RuntimeClassLoader LOADER = (RuntimeClassLoader) QuillCore.class.getClassLoader();
	
	public static final AbstractLocalPackageManager LOCAL_PACKAGES = new LocalPackageManager();
	
	public static void launch(String tag, String[] args) throws Exception {
		LOCAL_PACKAGES.load(List.of(tag));
		ILocalPackage pkg = LOCAL_PACKAGES.filter(tag).firstKey();
		
		Class<?> clazz = LOADER.loadClass(pkg.getMainClass());
		
		Method main = clazz.getDeclaredMethod("main", String[].class);
		main.setAccessible(true);
		main.invoke(null, (Object) args);
	}
}
