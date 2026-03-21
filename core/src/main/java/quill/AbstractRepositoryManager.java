package quill;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import quill.info.SpecCriteria;
import quill.info.TagCriteria;

public abstract class AbstractRepositoryManager<P extends IPackage, R extends AbstractRepository<P>>
		implements IRepositoryManager<P, R> {

	protected final Map<String, List<R>> repositories = new LinkedHashMap<>();

	protected void find(List<P> to, List<R> repos, SpecCriteria criteria) {
		if (repos == null) {
			return;
		}

		for (R r : repos) {
			r.find(to, criteria);
		}
	}

	protected void find(List<P> to, TagCriteria criteria) {
		if (criteria.hasNamespace()) {
			find(to, repositories.get(criteria.getNamespace()), criteria);
			return;
		}

		for (Map.Entry<String, List<R>> e : repositories.entrySet()) {
			find(to, e.getValue(), criteria);
		}
	}

	@Override
	public List<P> find(TagCriteria criteria) {
		List<P> list = new LinkedList<>();
		find(list, criteria);
		return list;
	}

	@Override
	public void refresh() {
		repositories.clear();
	}
}
