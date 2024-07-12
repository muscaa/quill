package quill.core.pkg.local;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import quill.core.QException;
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
		
		reload();
	}
	
	@Override
	public void load(Collection<String> tags) throws QException {
		List<ResolvedPackage<ILocalPackage, ILocalPackageRepository>> resolved = PackageResolver.resolve(this, tags);
		
		for (ResolvedPackage<ILocalPackage, ILocalPackageRepository> r : resolved) {
			if (loaded.contains(r.tag)) continue;
			
			r.repository.load(r.pkg);
			
			loaded.add(r.tag);
		}
	}
	
	@Override
	public void reload() {
		try {
			for (ILocalPackageRepository repo : repositories) {
				repo.reload();
			}
		} catch (QException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Collection<ILocalPackageRepository> getRepositories() {
		return repositories;
	}
	
	@Override
	public Collection<ILocalPackage> getPackages() {
		List<ILocalPackage> list = new LinkedList<>();
		for (ILocalPackageRepository repo : repositories) {
			repo.getPackages(list);
		}
		return list;
	}
	
	@Override
	public ILocalPackage get(String tag) {
		for (ILocalPackageRepository repo : repositories) {
			ILocalPackage pkg = repo.get(tag);
			
			if (pkg != null) return pkg;
		}
		return null;
	}
}
