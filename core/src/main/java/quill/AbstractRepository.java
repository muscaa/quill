package quill;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import fluff.core.utils.StringUtils;
import quill.info.Spec;
import quill.info.SpecCriteria;

public abstract class AbstractRepository<P extends IPackage> implements IRepository<P> {

	protected final String namespace;
	protected final List<P> packages = new LinkedList<>();

	public AbstractRepository(String namespace) {
		this.namespace = namespace;
	}
	
	protected void find(List<P> to, SpecCriteria criteria) {
		for (P p : packages) {
			Spec spec = Spec.of(p);
			
			if (criteria.matches(spec)) {
				to.add(p);
			}
		}
	}

	@Override
	public List<P> find(SpecCriteria criteria) {
		List<P> list = new LinkedList<>();
		find(list, criteria);
		return list;
	}

	@Override
	public void refresh() {
		packages.clear();
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String toString() {
		return StringUtils.format("AbstractRepository(namespace=\"${}\")", namespace);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractRepository r)) {
			return false;
		}
		return namespace.equals(r.namespace);
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace);
	}
}
