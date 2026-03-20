package quill;

import quill.info.Version;

public interface IPackage {
	
	String getNamespace();
	
	String getAuthor();
	
	String getId();
	
	Version getVersion();
	
	String getDescription();
}
