package fluff.core.lib;

import java.util.Set;

/**
 * Represents a library with metadata such as author, ID, dependencies, URL, and main class.
 */
public interface ILibrary {
    
    /**
     * Gets the author of the library.
     *
     * @return the author of the library.
     */
    String getAuthor();
    
    /**
     * Gets the ID of the library.
     *
     * @return the ID of the library.
     */
    String getID();
    
    /**
     * Gets the dependencies of the library.
     *
     * @return a set of dependencies required by the library.
     */
    Set<String> getDependencies();
    
    /**
     * Gets the URL of the library.
     *
     * @return the URL where the library can be found.
     */
    String getURL();
    
    /**
     * Gets the main class of the library.
     *
     * @return the fully qualified name of the main class of the library.
     */
    String getMainClass();
}
