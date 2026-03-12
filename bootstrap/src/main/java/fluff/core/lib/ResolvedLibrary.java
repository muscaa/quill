package fluff.core.lib;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a library that has been resolved, including its dependencies and dependents.
 */
public class ResolvedLibrary {
    
    /**
     * The list of libraries that depend on this library.
     */
    public final List<ResolvedLibrary> dependents = new LinkedList<>();
    
    /**
     * The list of libraries that this library depends on.
     */
    public final List<ResolvedLibrary> dependencies = new LinkedList<>();
    
    /**
     * The library manager associated with this resolved library.
     */
    public final ILibraryManager manager;
    
    /**
     * The actual library instance.
     */
    public final ILibrary library;
    
    /**
     * The tag for this resolved library.
     */
    public final String tag;
    
    /**
     * Constructs a ResolvedLibrary instance with the specified manager and library.
     *
     * @param manager the library manager associated with this resolved library.
     * @param library the actual library instance.
     */
    public ResolvedLibrary(ILibraryManager manager, ILibrary library) {
        this.manager = manager;
        this.library = library;
        this.tag = manager.getTag(library);
    }
    
    /**
     * Links this library to another one, adding it to the dependencies and
     * adding this library to the dependents of it.
     *
     * @param r the resolved library to link.
     */
    public void link(ResolvedLibrary r) {
        dependencies.add(r);
        r.dependents.add(this);
    }
    
    @Override
    public String toString() {
        return tag;
    }
    
    @Override
    public int hashCode() {
        return tag.hashCode();
    }
}
