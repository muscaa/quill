package quill.core.pkg;

import java.util.Set;

public interface QPackage {
	
    String getAuthor();
    
    String getID();
    
    QVersion getVersion();
    
    Set<String> getDependencies();
    
    String getQClass();
}
