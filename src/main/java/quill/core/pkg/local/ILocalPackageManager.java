package quill.core.pkg.local;

import java.util.Collection;

import quill.core.QException;
import quill.core.pkg.IPackageManager;

public interface ILocalPackageManager extends IPackageManager<ILocalPackage, ILocalPackageRepository> {
	
	void load(Collection<String> tags) throws QException;
}
