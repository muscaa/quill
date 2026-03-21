package quill;

import java.util.List;

import quill.info.SpecCriteria;

public interface IRepository<P extends IPackage> {

	List<P> find(SpecCriteria criteria);

	void refresh();

	String getNamespace();
}
