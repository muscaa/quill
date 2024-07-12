package quill.core.pkg.local;

import java.io.File;

import quill.core.pkg.IPackage;

public interface ILocalPackage extends IPackage {
	
	File getDir();
}
