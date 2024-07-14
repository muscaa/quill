package quill.core.pkg.local;

import quill.core.QException;
import quill.core.pkg.IPackageRepository;
import quill.core.pkg.ResolvedPackage;

public interface ILocalPackageRepository extends IPackageRepository<ILocalPackage, ILocalPackageRepository> {
	
	void load(ResolvedPackage<ILocalPackage, ILocalPackageRepository> r) throws QException;
}
