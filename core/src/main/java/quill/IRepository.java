package quill;

import java.util.List;

import quill.info.TagCriteria;

public interface IRepository<P extends IPackage> {
	
	List<P> find(TagCriteria criteria);
}
