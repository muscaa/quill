package quill.core.pkg;

import java.util.Set;

import quill.core.QVersion;

public interface IPackage {
	
    String getAuthor();
    
    String getID();
    
    QVersion getVersion();
    
    Set<String> getDependencies();
    
    String getMainClass();
    
    PackageInfo getInfo();
}
