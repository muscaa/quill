package fluff.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fluff.core.lib.ILibrary;
import fluff.core.lib.ILibraryManager;
import fluff.core.lib.LibraryResolver;
import fluff.core.lib.ResolvedLibrary;
import fluff.core.lib.v1.V1LibraryManager;
import fluff.core.lib.v1.LibraryMain;

/**
 * The core class responsible for managing and initializing the Fluff library system.
 * It provides functionality to register library managers, initialize the library system,
 * and retrieve loaded libraries.
 */
public class FluffCore {
    
    private static boolean INITIALIZED = false;
    
    /**
     * Initializes the Fluff library system.
     * 
     * @throws Exception if there is an error during initialization
     */
    @LibraryMain
    public static void init() throws Exception {
        if (INITIALIZED) return;
        
        register(new V1LibraryManager());
        
        ClassLoader loader = FluffCore.class.getClassLoader();
        
        List<URL> infos = new ArrayList<>();
        loader.getResources("fluff_lib.info")
                .asIterator()
                .forEachRemaining(url -> {
                    infos.add(url);
                });
        
        LibraryResolver.resolveAndLoad(loader, infos);
        
        INITIALIZED = true;
    }
    
    /**
     * Registers an {@link ILibraryManager} instance with the library resolver.
     * 
     * @param manager the {@link ILibraryManager} to register
     */
    public static void register(ILibraryManager manager) {
        LibraryResolver.MANAGERS.put(manager.getID(), manager);
    }
    
    /**
     * Retrieves a collection of loaded libraries.
     * 
     * @return a collection of loaded {@link ILibrary} instances
     */
    public static Collection<ILibrary> getLibs() {
        return LibraryResolver.LOADED
        		.values()
        		.stream()
        		.map(r -> r.library)
        		.toList();
    }
    
    /**
     * Finds and retrieves a library by its tag.
     * 
     * @param tag the tag of the library to find
     * @return the {@link ILibrary} instance associated with the tag, or {@code null} if not found
     */
    public static ILibrary findLib(String tag) {
    	ResolvedLibrary r = LibraryResolver.LOADED.get(tag);
        return r != null ? r.library : null;
    }
}
