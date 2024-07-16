package quill.core.pkg.local;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import quill.core.QException;
import quill.core.pkg.AbstractPackageManager;
import quill.core.pkg.PackageResolver;
import quill.core.pkg.ResolvedPackage;

public abstract class AbstractLocalPackageManager extends AbstractPackageManager<ILocalPackage, ILocalPackageRepository> {
	
	protected final Set<String> loaded = new HashSet<>();
	
	public void load(Collection<String> tags) throws QException {
		List<ResolvedPackage<ILocalPackage, ILocalPackageRepository>> resolved = PackageResolver.resolve(this, tags);
		
		for (ResolvedPackage<ILocalPackage, ILocalPackageRepository> r : resolved) {
			if (loaded.contains(r.tag)) continue;
			
			r.repository.load(r.pkg);
			
			loaded.add(r.tag);
		}
	}
}
