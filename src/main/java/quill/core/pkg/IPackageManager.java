package quill.core.pkg;

import java.util.Collection;
import java.util.NavigableMap;

public interface IPackageManager<P extends IPackage, R extends IPackageRepository<P, R>> {
	
	Collection<R> getRepositories();
	
	NavigableMap<P, R> getPackages();
	
	NavigableMap<P, R> filter(IPackageFilter<P, R> filter);
	
	NavigableMap<P, R> filter(String tag);
}
