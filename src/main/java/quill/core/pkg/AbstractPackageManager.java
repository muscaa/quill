package quill.core.pkg;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public abstract class AbstractPackageManager<P extends IPackage, R extends IPackageRepository<P, R>> {
	
	protected final List<R> repositories = new LinkedList<>();
	
	public Collection<R> getRepositories() {
		return repositories;
	}
	
	public NavigableMap<P, R> getPackages() {
		return filter((IPackageFilter<P, R>) null);
	}
	
	public NavigableMap<P, R> filter(IPackageFilter<P, R> filter) {
		NavigableMap<P, R> map = new TreeMap<>(Comparator.comparing(IPackage::getID));
		for (R repo : repositories) {
			repo.filter(filter, map);
		}
		return map;
	}
	
	public NavigableMap<P, R> filter(String tag) {
		return filter((p, r) -> {
        	String pkgTag = r.getTag(p);
        	
        	return pkgTag.equals(tag);
		});
	}
}
