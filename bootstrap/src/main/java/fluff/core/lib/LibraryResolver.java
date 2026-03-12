package fluff.core.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Utility class for resolving and loading libraries based on their information URLs.
 */
public class LibraryResolver {
    
    /**
     * Map of library managers indexed by manager ID.
     */
    public static final Map<String, ILibraryManager> MANAGERS = new HashMap<>();
    
    /**
     * Map of loaded resolved libraries indexed by their tags.
     */
    public static final Map<String, ResolvedLibrary> LOADED = new HashMap<>();
    
    /**
     * Resolves and loads libraries using the specified ClassLoader and list of information URLs.
     *
     * @param loader the ClassLoader to use for loading libraries.
     * @param infos the list of URLs containing library information.
     * @throws LibraryException if there is an error resolving or loading libraries.
     */
    public static void resolveAndLoad(ClassLoader loader, List<URL> infos) throws LibraryException {
        List<URL> list = new LinkedList<>(infos);
        
        while (!list.isEmpty()) {
            List<ResolvedLibrary> resolved = resolve(list);
            
            for (ResolvedLibrary r : resolved) {
                r.manager.load(loader, r.library);
                
                LOADED.put(r.tag, r);
            }
        }
    }
    
    /**
     * Resolves libraries from the list of information URLs.
     *
     * @param infos the list of URLs containing library information.
     * @return a list of resolved libraries.
     * @throws LibraryException if there is an error resolving libraries.
     */
    public static List<ResolvedLibrary> resolve(List<URL> infos) throws LibraryException {
        if (infos.isEmpty()) return List.of();
        
        Queue<URL> unresolved = new LinkedList<>(infos);
        Map<String, ResolvedLibrary> resolved = new HashMap<>();
        Map<String, Integer> counter = new HashMap<>();
        
        infos.clear();
        while (!unresolved.isEmpty()) {
            URL url = unresolved.poll();
            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String id = br.readLine();
                ILibraryManager manager = MANAGERS.get(id);
                
                if (manager == null) {
                    br.close();
                    infos.add(url); // doesn't have a manager, wait for it
                    continue;
                }
                
                ILibrary library = manager.create(br);
                ResolvedLibrary r = new ResolvedLibrary(manager, library);
                
                if (LOADED.containsKey(r.tag)) throw new LibraryException("Library already loaded: " + r.tag);
                if (resolved.containsKey(r.tag)) throw new LibraryException("Overlapping library tag: " + r.tag);
                
                resolved.put(r.tag, r);
            } catch (IOException e) {
                throw new LibraryException(e);
            }
        }
        
        if (resolved.isEmpty()) throw new LibraryException("Couldn't resolve libraries!");
        
        for (Map.Entry<String, ResolvedLibrary> e : resolved.entrySet()) {
            ResolvedLibrary r = e.getValue();
            
            for (String tag : r.library.getDependencies()) {
                if (LOADED.containsKey(tag)) {
                    r.link(LOADED.get(tag));
                } else if (resolved.containsKey(tag)) {
                    r.link(resolved.get(tag));
                    
                    counter.put(r.tag, counter.getOrDefault(r.tag, 0) + 1);
                } else {
                    throw new LibraryException("Missing library: " + tag);
                }
            }
        }
        
        Queue<ResolvedLibrary> queue = new LinkedList<>();
        List<ResolvedLibrary> sorted = new LinkedList<>();
        
        for (Map.Entry<String, ResolvedLibrary> e : resolved.entrySet()) {
            ResolvedLibrary r = e.getValue();
            int count = counter.getOrDefault(r.tag, 0);
            
            if (count == 0) queue.add(r);
        }
        
        while (!queue.isEmpty()) {
            ResolvedLibrary current = queue.poll();
            sorted.add(current);
            
            for (ResolvedLibrary r : current.dependents) {
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
            
            for (Map.Entry<String, ResolvedLibrary> e : resolved.entrySet()) {
                ResolvedLibrary r = e.getValue();
                
                if (detectCycle(r, visited, stack)) {
                    throw new LibraryException("Library cycle detected: " + stack);
                }
            }
        }
        
        return sorted;
    }
    
    /**
     * Detects cycles in resolved libraries to prevent circular dependencies.
     *
     * @param r the resolved library to check.
     * @param visited set of visited library tags.
     * @param stack set of libraries in the current stack trace.
     * @return true if a cycle is detected, false otherwise.
     */
    public static boolean detectCycle(ResolvedLibrary r, Set<String> visited, Set<String> stack) {
        if (LOADED.containsKey(r.tag)) return false;
        if (stack.contains(r.tag)) return true;
        if (visited.contains(r.tag)) return false;
        
        visited.add(r.tag);
        stack.add(r.tag);
        
        for (ResolvedLibrary dependent : r.dependents) {
            if (detectCycle(dependent, visited, stack)) return true;
        }
        
        stack.remove(r.tag);
        
        return false;
    }
}
