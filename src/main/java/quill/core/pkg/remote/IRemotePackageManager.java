package quill.core.pkg.remote;

import java.util.Collection;

import quill.core.QException;
import quill.core.pkg.IPackageManager;

public interface IRemotePackageManager extends IPackageManager<IRemotePackage, IRemotePackageRepository> {
	
	void install(Collection<String> tags) throws QException;
}
