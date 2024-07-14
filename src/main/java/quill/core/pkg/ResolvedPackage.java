package quill.core.pkg;

import java.util.LinkedList;
import java.util.List;

public class ResolvedPackage<P extends IPackage, R extends IPackageRepository<P, R>> {
	
	public final List<ResolvedPackage> dependents = new LinkedList<>();
	public final List<ResolvedPackage> dependencies = new LinkedList<>();
	public final R repository;
	public final P pkg;
	public final String tag;
	
	public ResolvedPackage(R repository, P pkg) {
		this.repository = repository;
		this.pkg = pkg;
		this.tag = repository.getTag(pkg);
	}
	
	public void link(ResolvedPackage<P, R> r) {
		dependencies.add(r);
		r.dependents.add(this);
	}
	
	@Override
	public String toString() {
		return tag;
	}
	
	@Override
	public int hashCode() {
		return tag.hashCode();
	}
}
