package quill.core.pkg.remote;

import quill.core.QException;
import quill.core.pkg.IPackageRepository;

public interface IRemotePackageRepository extends IPackageRepository<IRemotePackage, IRemotePackageRepository> {
	
	void install(IRemotePackage pkg) throws QException;
}
