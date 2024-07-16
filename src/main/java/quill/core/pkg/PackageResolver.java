package quill.core.pkg;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.Set;

import quill.core.QException;

public class PackageResolver {
	
	public static <P extends IPackage, R extends IPackageRepository<P, R>> List<ResolvedPackage<P, R>> resolve(AbstractPackageManager<P, R> manager, Collection<String> tags) throws QException {
		if (tags.isEmpty()) return List.of();
        
        Queue<String> unresolved = new LinkedList<>();
        Set<String> unresolvedSet = new HashSet<>(tags);
        Map<String, ResolvedPackage<P, R>> resolved = new HashMap<>();
        Map<String, Integer> counter = new HashMap<>();
        
        while (!unresolvedSet.isEmpty()) {
        	Set<String> toAdd = new HashSet<>();
        	
        	unresolved.addAll(unresolvedSet);
	        while (!unresolved.isEmpty()) {
	            String tag = unresolved.poll();
	            
	            NavigableMap<P, R> map = manager.filter(tag);
	            if (map.size() > 1) throw new QException("Found multiple packages for tag: " + tag);
	            if (map.isEmpty()) throw new QException("Package " + tag + " does not exist!");
	            
	            R repository = map.firstEntry().getValue();
	            P pkg = map.firstEntry().getKey();
	            
                ResolvedPackage<P, R> r = new ResolvedPackage<>(repository, pkg);
                if (resolved.containsKey(r.tag)) throw new QException("Overlapping package tag: " + r.tag);
                
                for (String dep : pkg.getDependencies()) {
                	if (resolved.containsKey(dep)) continue;
                	if (unresolvedSet.contains(dep)) continue; // in queue
                	
                	toAdd.add(dep);
                }
                
                resolved.put(r.tag, r);
	        }
	        
	        unresolvedSet.clear();
	        unresolvedSet.addAll(toAdd);
        }
        
        if (resolved.isEmpty()) throw new QException("Couldn't resolve packages!");
        
        for (Map.Entry<String, ResolvedPackage<P, R>> e : resolved.entrySet()) {
        	ResolvedPackage<P, R> r = e.getValue();
            
            for (String tag : r.pkg.getDependencies()) {
                if (resolved.containsKey(tag)) {
                    r.link(resolved.get(tag));
                    
                    counter.put(r.tag, counter.getOrDefault(r.tag, 0) + 1);
                } else {
                    throw new QException("Missing package: " + tag);
                }
            }
        }
        
        Queue<ResolvedPackage<P, R>> queue = new LinkedList<>();
        List<ResolvedPackage<P, R>> sorted = new LinkedList<>();
        
        for (Map.Entry<String, ResolvedPackage<P, R>> e : resolved.entrySet()) {
        	ResolvedPackage<P, R> r = e.getValue();
            int count = counter.getOrDefault(r.tag, 0);
            
            if (count == 0) queue.add(r);
        }
        
        while (!queue.isEmpty()) {
        	ResolvedPackage<P, R> current = queue.poll();
            sorted.add(current);
            
            for (ResolvedPackage<P, R> r : current.dependents) {
                int count = counter.get(r.tag) - 1;
                
                if (count == 0) {
                    queue.add(r);
                } else {
                    counter.put(r.tag, count);
                }
            }
        }
        
        if (sorted.size() != resolved.size()) {
            Set<String> visited = new HashSet<>();
            Set<String> stack = new HashSet<>();
            
            for (Map.Entry<String, ResolvedPackage<P, R>> e : resolved.entrySet()) {
            	ResolvedPackage<P, R> r = e.getValue();
                
                if (detectCycle(r, visited, stack)) {
                    throw new QException("Package cycle detected: " + stack);
                }
            }
        }
        
        return sorted;
	}
	
    public static <P extends IPackage, R extends IPackageRepository<P, R>> boolean detectCycle(ResolvedPackage<P, R> r, Set<String> visited, Set<String> stack) {
        if (stack.contains(r.tag)) return true;
        if (visited.contains(r.tag)) return false;
        
        visited.add(r.tag);
        stack.add(r.tag);
        
        for (ResolvedPackage<P, R> dependent : r.dependents) {
            if (detectCycle(dependent, visited, stack)) return true;
        }
        
        stack.remove(r.tag);
        
        return false;
    }
}
