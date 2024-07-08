package quill.core.main;

import java.lang.reflect.Method;
import java.util.List;

import quill.core.QuillCore;
import quill.core.pkg.local.QLocalPackage;

public class QuillCoreMain {
	
	public static void launch(String tag, String[] args) throws Exception {
		QLocalPackage qpkg = QuillCore.LOCAL.get(tag);
		QuillCore.LOCAL.load(List.of(qpkg));
		
		Class<?> clazz = QuillCore.LOADER.loadClass(qpkg.getQClass());
		
		Method main = clazz.getDeclaredMethod("main", String[].class);
		main.setAccessible(true);
		main.invoke(null, (Object) args);
	}
}
