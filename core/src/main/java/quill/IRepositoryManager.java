package quill;

import java.util.List;

import quill.info.TagCriteria;

public interface IRepositoryManager<P extends IPackage, R extends IRepository<P>> {

	List<P> find(TagCriteria criteria);

	void refresh();
}
