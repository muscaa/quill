package quill.core.pkg.local;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import quill.core.QException;
import quill.core.pkg.IPackage;
import quill.core.pkg.IPackageFilter;
import quill.core.pkg.PackageResolver;
import quill.core.pkg.ResolvedPackage;
import quill.core.pkg.local.repositories.LocalPackagesPackageRepository;
import quill.core.pkg.local.repositories.LocalSystemPackageRepository;

public class LocalPackageManager implements ILocalPackageManager {
	
	private final List<ILocalPackageRepository> repositories = new ArrayList<>();
	private final Set<String> loaded = new HashSet<>();
	
	public LocalPackageManager() {
		repositories.add(new LocalSystemPackageRepository());
		repositories.add(new LocalPackagesPackageRepository());
		
		loaded.add("fluff-loader");
		loaded.add("quill-loader");
		loaded.add("quill-core");
	}
	
	@Override
	public void load(Collection<String> tags) throws QException {
		List<ResolvedPackage<ILocalPackage, ILocalPackageRepository>> resolved = PackageResolver.resolve(this, tags);
		
		for (ResolvedPackage<ILocalPackage, ILocalPackageRepository> r : resolved) {
			if (loaded.contains(r.tag)) continue;
			
			r.repository.load(r);
			
			loaded.add(r.tag);
		}
	}
	
	@Override
	public Collection<ILocalPackageRepository> getRepositories() {
		return repositories;
	}
	
	@Override
	public NavigableMap<ILocalPackage, ILocalPackageRepository> getPackages() {
		return filter((IPackageFilter<ILocalPackage, ILocalPackageRepository>) null);
	}
	
	@Override
	public NavigableMap<ILocalPackage, ILocalPackageRepository> filter(IPackageFilter<ILocalPackage, ILocalPackageRepository> filter) {
		NavigableMap<ILocalPackage, ILocalPackageRepository> map = new TreeMap<>(Comparator.comparing(IPackage::getID));
		for (ILocalPackageRepository repo : repositories) {
			repo.filter(filter, map);
		}
		return map;
	}
	
	@Override
	public NavigableMap<ILocalPackage, ILocalPackageRepository> filter(String tag) {
		return filter((p, r) -> {
        	String pkgTag = r.getTag(p);
        	
        	return pkgTag.equals(tag);
		});
	}
}
