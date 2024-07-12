package quill.core.pkg.local;

import quill.core.QException;
import quill.core.pkg.IPackageRepository;

public interface ILocalPackageRepository extends IPackageRepository<ILocalPackage> {
	
	void load(ILocalPackage pkg) throws QException;
}
