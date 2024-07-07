package quill.core.pkg;

import java.util.LinkedList;
import java.util.List;

public class QDependency {
	
	public final List<QDependency> dependents = new LinkedList<>();
	public final List<QDependency> dependencies = new LinkedList<>();
	public final String tag;
	public final QPackage qpkg;
	
	public int unresolved = 0;
	
	public QDependency(String tag, QPackage qpkg) {
		this.tag = tag;
		this.qpkg = qpkg;
	}
	
	public void link(QDependency dep) {
		dependencies.add(dep);
		dep.dependents.add(this);
		
		unresolved++;
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
