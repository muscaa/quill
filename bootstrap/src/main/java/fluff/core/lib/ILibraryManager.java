package fluff.core.lib;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Represents a library manager responsible for creating, loading, and managing libraries.
 *
 * @param <V> the type of library this manager handles, extending ILibrary.
 */
public interface ILibraryManager<V extends ILibrary> {
    
    /**
     * Creates a library instance from the given buffered reader.
     *
     * @param reader the BufferedReader to read library data from.
     * @return the created library instance.
     * @throws LibraryException if an error occurs during library creation.
     * @throws IOException if an I/O error occurs.
     */
    V create(BufferedReader reader) throws LibraryException, IOException;
    
    /**
     * Loads the given library using the specified class loader.
     *
     * @param loader the ClassLoader to use for loading the library.
     * @param library the library to load.
     * @throws LibraryException if an error occurs during library loading.
     */
    void load(ClassLoader loader, V library) throws LibraryException;
    
    /**
     * Gets a tag for the given library.
     *
     * @param library the library to get the tag for.
     * @return the tag for the given library.
     */
    String getTag(V library);
    
    /**
     * Gets the ID of the library manager.
     *
     * @return the ID of the library manager.
     */
    String getID();
}
