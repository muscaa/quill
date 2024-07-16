package quill.core.pkg.local.repositories;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import quill.core.QException;
import quill.core.QFiles;
import quill.core.QuillCore;
import quill.core.pkg.IPackageFilter;
import quill.core.pkg.PackageMain;
import quill.core.pkg.local.ILocalPackage;
import quill.core.pkg.local.ILocalPackageRepository;
import quill.core.pkg.local.LocalPackage;

public class LocalSystemPackageRepository implements ILocalPackageRepository {
	
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
	public void filter(IPackageFilter<ILocalPackage, ILocalPackageRepository> filter, Map<ILocalPackage, ILocalPackageRepository> map) {
		for (File dir : QFiles.SYSTEM.listFiles()) {
			if (!dir.isDirectory()) continue;
			
			try {
				ILocalPackage pkg = new LocalPackage(dir);
				if (filter != null && !filter.isValid(pkg, this)) continue;
				
				map.put(pkg, this);
			} catch (QException e) {}
		}
	}
	
	@Override
	public String getTag(ILocalPackage pkg) {
		return pkg.getID();
	}
}
