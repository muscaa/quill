package quill.core.pkg;

import java.util.List;

import quill.core.QException;

public interface IPackageRepository<P extends IPackage> {
	
	void reload() throws QException;
	
	void getPackages(List<P> list);
	
	P get(String tag);
    
    String getTag(P pkg);
}
