package quill.core.pkg;

import java.util.List;

import quill.core.QException;

public interface QRemotePackageManager extends QPackageManager {
	
	void install(List<QPackage> qpkgs) throws QException;
	
	void uninstall(List<QPackage> qpkgs) throws QException;
}
