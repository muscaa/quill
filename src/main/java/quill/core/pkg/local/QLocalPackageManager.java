package quill.core.pkg.local;

import java.util.List;

import quill.core.QException;
import quill.core.pkg.QPackageManager;

public interface QLocalPackageManager extends QPackageManager<QLocalPackage> {
	
	void load(List<QLocalPackage> qpkgs) throws QException;
}
