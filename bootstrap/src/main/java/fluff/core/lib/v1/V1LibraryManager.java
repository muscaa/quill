package fluff.core.lib.v1;

import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import fluff.core.lib.ILibraryManager;
import fluff.core.lib.LibraryException;
import fluff.core.lib.info.LibraryInfoReader;

/**
 * Implements {@link ILibraryManager} for managing v1 libraries.
 */
public class V1LibraryManager implements ILibraryManager<V1Library> {
    
    public static final String ID = "v1";
    
    @Override
    public V1Library create(BufferedReader reader) throws LibraryException {
        return new V1Library(new LibraryInfoReader(reader));
    }
    
    @Override
    public void load(ClassLoader loader, V1Library library) throws LibraryException {
        if (library.getMainClass() == null) return;
        if (library.getMainClass().equals("fluff.core.FluffCore")) return;
        
        try {
            Class<?> clazz = loader.loadClass(library.getMainClass());
            
            for (Method m : clazz.getDeclaredMethods()) {
                if (!m.isAnnotationPresent(LibraryMain.class)) continue;
                if (!Modifier.isStatic(m.getModifiers())) continue;
                
                m.setAccessible(true);
                m.invoke(null);
            }
        } catch (Exception e) {
            throw new LibraryException(e);
        }
    }
    
    @Override
    public String getTag(V1Library lib) {
        return lib.getAuthor() + "/" + lib.getID();
    }
    
    @Override
    public String getID() {
        return ID;
    }
}
