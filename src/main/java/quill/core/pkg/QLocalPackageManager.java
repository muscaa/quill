package quill.core.pkg;

import java.util.List;

import quill.core.QException;

public interface QLocalPackageManager extends QPackageManager {
	
	void load(List<QPackage> qpkgs) throws QException;
}
