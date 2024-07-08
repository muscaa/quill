package quill.core.pkg;

import java.util.Collection;
import java.util.List;

import quill.core.QException;

public interface QPackageManager<V extends QPackage> {
	
	List<V> resolve(List<V> qpkgs) throws QException;
	
	void reload();
	
	V get(String tag);
	
	Collection<V> getPackages();
}
