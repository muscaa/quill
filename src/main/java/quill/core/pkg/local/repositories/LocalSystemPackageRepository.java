package quill.core.pkg.local.repositories;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import quill.core.QException;
import quill.core.QFiles;
import quill.core.QuillCore;
import quill.core.pkg.PackageMain;
import quill.core.pkg.local.ILocalPackage;
import quill.core.pkg.local.ILocalPackageRepository;
import quill.core.pkg.local.LocalPackage;

public class LocalSystemPackageRepository implements ILocalPackageRepository {
	
	private final Map<String, ILocalPackage> reg = new HashMap<>();
	
	@Override
	public void load(ILocalPackage pkg) throws QException {
		File dir = new File(QFiles.SYSTEM, getTag(pkg));
		
		File java = new File(dir, "java");
		if (!java.exists()) return;
		
		QuillCore.LOADER.addFolder(java);
		
		if (pkg.getMainClass() != null) {
			try {
				Class<?> clazz = QuillCore.LOADER.loadClass(pkg.getMainClass());
				
		    	for (Method m : clazz.getDeclaredMethods()) {
		            if (!m.isAnnotationPresent(PackageMain.class)) continue;
		            if (!Modifier.isStatic(m.getModifiers())) continue;
		            
	                m.setAccessible(true);
	                m.invoke(null);
		        }
			} catch (Exception e) {
				throw new QException(e);
			}
		}
	}
	
	@Override
	public void reload() throws QException {
		reg.clear();
		
		for (File dir : QFiles.SYSTEM.listFiles()) {
			if (!dir.isDirectory()) continue;
			
			LocalPackage pkg = new LocalPackage(dir);
			
			reg.put(getTag(pkg), pkg);
		}
	}
	
	@Override
	public void getPackages(List<ILocalPackage> list) {
		list.addAll(reg.values());
	}
	
	@Override
	public ILocalPackage get(String tag) {
		return reg.get(tag);
	}
	
	@Override
	public String getTag(ILocalPackage pkg) {
		return pkg.getID();
	}
}
