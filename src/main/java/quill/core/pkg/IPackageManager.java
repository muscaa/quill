package quill.core.pkg;

import java.util.Collection;

public interface IPackageManager<P extends IPackage, R extends IPackageRepository<P>> {
	
	void reload();
	
	Collection<R> getRepositories();
	
	Collection<P> getPackages();
	
	P get(String tag);
}
