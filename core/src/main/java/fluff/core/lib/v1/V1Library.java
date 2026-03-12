package fluff.core.lib.v1;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import fluff.core.lib.ILibrary;
import fluff.core.lib.LibraryException;
import fluff.core.lib.info.LibraryInfoReader;

/**
 * Represents a version 1 library implementation of the the {@link ILibrary} interface.
 */
public class V1Library implements ILibrary {
    
    private final String author;
    private final String id;
    private final Set<String> dependencies;
    private final String url;
    private final String mainClass;
    
    /**
     * Constructs a V1Library object using the information provided by the {@link LibraryInfoReader}.
     * 
     * @param r the {@link LibraryInfoReader} instance to read library information from
     * @throws LibraryException if there is an error while reading or processing library information
     */
    public V1Library(LibraryInfoReader r) throws LibraryException {
        author = r.required("author", "Property missing: author")
                    .String();
        
        id = r.required("id", "Property missing: id")
                 .String();
        
        dependencies = r.optional("depends")
                        .transform(Set.class)
                        .If(Objects::nonNull, v -> {
                            Set<String> set = new HashSet<>();
                            for (String tag : v.split(",")) {
                                tag = tag.trim();
                                set.add(tag);
                            }
                            return set;
                        })
                        .Else(v -> new HashSet<>())
                        .Result();
        if (!author.equals("muscaa") || !id.equals("fluff-core")) dependencies.add("muscaa/fluff-core");
        
        url = r.optional("url")
                    .String();
        
        mainClass = r.optional("class")
                        .String();
    }
    
    @Override
    public String getAuthor() {
        return author;
    }
    
    @Override
    public String getID() {
        return id;
    }
    
    @Override
    public Set<String> getDependencies() {
        return Set.copyOf(dependencies);
    }
    
    @Override
    public String getURL() {
        return url;
    }
    
    @Override
    public String getMainClass() {
        return mainClass;
    }
}
