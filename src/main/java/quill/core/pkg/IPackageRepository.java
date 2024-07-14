package quill.core.pkg;

import java.util.Map;

public interface IPackageRepository<P extends IPackage, R extends IPackageRepository<P, R>> {
	
	void filter(IPackageFilter<P, R> filter, Map<P, R> map);
	
	String getTag(P pkg);
}
