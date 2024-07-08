package quill.core.pkg.local;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import quill.core.QException;
import quill.core.QFiles;
import quill.core.QuillCore;
import quill.core.pkg.QDependency;
import quill.core.pkg.QLibraryMain;

public class LocalPackageManager implements QLocalPackageManager {
	
	private final Map<String, QLocalPackage> packages = new HashMap<>();
	private final Map<QLocalPackage, String> packagesRev = new HashMap<>();
	private final Set<String> loaded = new HashSet<>();
	
	public LocalPackageManager() {
		reload();
		
		loaded.add("fluff-loader");
		loaded.add("quill-loader");
		loaded.add("quill-core");
	}
	
	@Override
	public void load(List<QLocalPackage> qpkgs) throws QException {
		List<QLocalPackage> resolved = resolve(qpkgs);
		
		for (QLocalPackage qpkg : resolved) {
			String tag = packagesRev.get(qpkg);
			if (loaded.contains(tag)) continue;
			
			File java = new File(qpkg.getDir(), "java");
			if (!java.exists()) continue;
			
			QuillCore.LOADER.addFolder(java);
			
			if (qpkg.getQClass() != null) {
				try {
					Class<?> clazz = QuillCore.LOADER.loadClass(qpkg.getQClass());
					
			    	for (Method m : clazz.getDeclaredMethods()) {
			            if (!m.isAnnotationPresent(QLibraryMain.class)) continue;
			            if (!Modifier.isStatic(m.getModifiers())) continue;
			            
			            try {
			                m.setAccessible(true);
			                m.invoke(null);
			            } catch (Exception e) {
			                throw new QException(e);
			            }
			        }
				} catch (ClassNotFoundException e) {
					throw new QException(e);
				}
			}
			
			loaded.add(tag);
		}
	}
	
	@Override
	public List<QLocalPackage> resolve(List<QLocalPackage> qpkgs) throws QException {
		if (qpkgs.isEmpty()) return List.of();
		
		Queue<QLocalPackage> unresolved = new LinkedList<>(qpkgs);
		Set<String> newlyAdded = new HashSet<>();
		Map<String, QDependency> deps = new HashMap<>();
		
		while (!unresolved.isEmpty()) {
			QLocalPackage qpkg = unresolved.poll();
			
			String tag = packagesRev.get(qpkg);
			if (tag == null) throw new QException("Package " + qpkg.getID() + " does not exist!");
			
			QDependency dep = new QDependency(tag, qpkg);
			if (deps.containsKey(dep.tag)) throw new QException("Overlapping tag: " + dep.tag);
			
			for (String tagDep : qpkg.getDependencies()) {
				if (deps.containsKey(tagDep)) continue;
				if (newlyAdded.contains(tagDep)) continue;
				
				QLocalPackage qpkgDep = packages.get(tagDep);
				if (qpkgDep == null) throw new QException("Package " + tagDep + " does not exist!");
				
				unresolved.add(qpkgDep);
				newlyAdded.add(tagDep);
			}
			
			deps.put(tag, dep);
			newlyAdded.remove(tag);
		}
		
		if (deps.isEmpty()) throw new QException("Couldn't resolve package dependencies!");
		
    	for (Map.Entry<String, QDependency> e : deps.entrySet()) {
    		QDependency dep = e.getValue();
    		
    		for (String tag : dep.qpkg.getDependencies()) {
    			if (deps.containsKey(tag)) {
    				dep.link(deps.get(tag));
    			} else {
    				throw new QException("Missing dependency: " + tag);
    			}
    		}
    	}
    	
    	Queue<QDependency> queue = new LinkedList<>();
    	
    	for (Map.Entry<String, QDependency> e : deps.entrySet()) {
    		QDependency dep = e.getValue();
    		
    		if (dep.unresolved == 0) queue.add(dep);
    	}
    	
    	List<QLocalPackage> sorted = new ArrayList<>();
    	
    	while (!queue.isEmpty()) {
    		QDependency current = queue.poll();
    		sorted.add((QLocalPackage) current.qpkg);
    		
    		for (QDependency dnt : current.dependents) {
    			if (--dnt.unresolved == 0) queue.add(dnt);
    		}
    	}
    	
    	if (sorted.size() != deps.size()) {
            Set<String> visited = new HashSet<>();
            Set<String> recursionStack = new HashSet<>();
            
        	for (Map.Entry<String, QDependency> e : deps.entrySet()) {
        		QDependency dep = e.getValue();
        		
        		if (detectCycle(dep, visited, recursionStack)) {
                    throw new QException("Cyclic dependency detected: " + recursionStack);
                }
        	}
    	}
    	
    	return sorted;
	}
	
	@Override
	public void reload() {
		packages.clear();
		packagesRev.clear();
		
		try {
			for (File dir : QFiles.SYSTEM.listFiles()) {
				if (!dir.isDirectory()) continue;
				
				QLocalPackage pkg = new LocalPackage(dir);
				String tag = pkg.getID();
				
				packages.put(tag, pkg);
				packagesRev.put(pkg, tag);
			}
			
			for (File dir : QFiles.PACKAGES.listFiles()) {
				if (!dir.isDirectory()) continue;
				
				QLocalPackage pkg = new LocalPackage(dir);
				String tag = pkg.getAuthor() + "@" + pkg.getID();
				
				packages.put(tag, pkg);
				packagesRev.put(pkg, tag);
			}
		} catch (QException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public QLocalPackage get(String tag) {
		return packages.get(tag);
	}
	
	@Override
	public Collection<QLocalPackage> getPackages() {
		return packages.values();
	}
    
    private static boolean detectCycle(QDependency dep, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(dep.tag)) return true;
        if (visited.contains(dep.tag)) return false;
        
        visited.add(dep.tag);
        recursionStack.add(dep.tag);
        
        for (QDependency dependent : dep.dependents) {
            if (detectCycle(dependent, visited, recursionStack)) return true;
        }
        
        recursionStack.remove(dep.tag);
        
        return false;
    }
}
