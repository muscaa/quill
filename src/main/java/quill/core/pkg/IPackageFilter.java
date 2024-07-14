package quill.core.pkg;

public interface IPackageFilter<P extends IPackage, R extends IPackageRepository<P, R>> {
	
	boolean isValid(P pkg, R repo);
}
