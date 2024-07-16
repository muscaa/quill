package quill.core.pkg.remote;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import quill.core.QException;
import quill.core.QuillCore;
import quill.core.pkg.AbstractPackageManager;
import quill.core.pkg.PackageResolver;
import quill.core.pkg.ResolvedPackage;
import quill.core.pkg.local.ILocalPackage;
import quill.core.pkg.local.ILocalPackageRepository;

public abstract class AbstractRemotePackageManager extends AbstractPackageManager<IRemotePackage, IRemotePackageRepository> {
	
	public abstract void fetch();
	
	public void install(Collection<String> tags) throws QException {
		List<ResolvedPackage<IRemotePackage, IRemotePackageRepository>> resolved = PackageResolver.resolve(this, tags);
		
		for (ResolvedPackage<IRemotePackage, IRemotePackageRepository> r : resolved) {
			Map.Entry<ILocalPackage, ILocalPackageRepository> local = QuillCore.LOCAL_PACKAGES.filter(r.tag).firstEntry();
			if (local != null && local.getKey().getVersion().compare(r.pkg.getVersion()) == 0) continue;
			
			r.repository.install(r.pkg);
		}
	}
	
	public boolean update(String tag) throws QException {
		Map.Entry<IRemotePackage, IRemotePackageRepository> remote = filter(tag).firstEntry();
		if (remote == null) return false;
		
		Map.Entry<ILocalPackage, ILocalPackageRepository> local = QuillCore.LOCAL_PACKAGES.filter(tag).firstEntry();
		if (local == null || local.getKey().getVersion().compare(remote.getKey().getVersion()) == 0) {
			return false;
		}
		
		remote.getValue().install(remote.getKey());
		return true;
	}
}
