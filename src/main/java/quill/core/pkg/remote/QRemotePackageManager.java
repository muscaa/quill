package quill.core.pkg.remote;

import java.util.List;

import quill.core.QException;
import quill.core.pkg.QPackage;
import quill.core.pkg.QPackageManager;

public interface QRemotePackageManager extends QPackageManager {
	
	void install(List<QPackage> qpkgs) throws QException;
	
	void uninstall(List<QPackage> qpkgs) throws QException;
}
