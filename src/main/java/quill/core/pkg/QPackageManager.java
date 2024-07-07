package quill.core.pkg;

import java.util.Collection;
import java.util.List;

import quill.core.QException;

public interface QPackageManager {
	
	List<QPackage> resolve(List<QPackage> qpkgs) throws QException;
	
	void reload();
	
	QPackage get(String tag);
	
	Collection<QPackage> getPackages();
}
