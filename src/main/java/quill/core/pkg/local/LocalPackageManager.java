package quill.core.pkg.local;

import quill.core.pkg.local.repositories.LocalPackagesPackageRepository;
import quill.core.pkg.local.repositories.LocalSystemPackageRepository;

public class LocalPackageManager extends AbstractLocalPackageManager {
	
	public LocalPackageManager() {
		repositories.add(new LocalSystemPackageRepository());
		repositories.add(new LocalPackagesPackageRepository());
		
		loaded.add("fluff-loader");
		loaded.add("quill-loader");
		loaded.add("quill-core");
	}
}
