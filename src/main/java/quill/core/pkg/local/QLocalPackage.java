package quill.core.pkg.local;

import java.io.File;

import quill.core.pkg.QPackage;

public interface QLocalPackage extends QPackage {
	
	File getDir();
}
