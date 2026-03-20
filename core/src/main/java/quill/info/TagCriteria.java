package quill.info;

import java.util.Objects;

public class TagCriteria {

	public static final String ANY = "*";

	private final String namespace;
	private final String author;
	private final String id;

	private TagCriteria(String namespace, String author, String id) {
		this.namespace = namespace;
		this.author = author;
		this.id = id;
	}

	public boolean hasNamespace() {
		return namespace != null;
	}

	public String getNamespace() {
		return namespace;
	}

	public boolean hasAuthor() {
		return author != null;
	}

	public String getAuthor() {
		return author;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return (hasNamespace() ? namespace : ANY) + Tag.DELIMITER + (hasAuthor() ? author : ANY) + Spec.DELIMITER + id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TagCriteria tc)) {
			return false;
		}
		return namespace.equals(tc.namespace) && author.equals(tc.author) && id.equals(tc.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, author, id);
	}

	public static TagCriteria of(String namespace, String author, String id) {
		if ((namespace != null && !namespace.equals(ANY) && !Tag.PATTERN_NAMESPACE.matcher(namespace).matches())
				|| (author != null && !author.equals(ANY) && !Spec.PATTERN_AUTHOR.matcher(author).matches())
				|| id == null || Spec.PATTERN_ID.matcher(id).matches()) {
			return null;
		}

		return new TagCriteria(namespace, author, id);
	}

	public static TagCriteria of(String criteria) {
		String[] split1 = criteria.split(Tag.DELIMITER);
		if (split1.length > 2) {
			return null;
		}

		String[] split2 = split1[split1.length - 1].split(Spec.DELIMITER);
		if (split2.length > 2) {
			return null;
		}

		String namespace = split1.length == 2 ? split1[0] : null;
		String author = split2.length == 2 ? split2[0] : null;
		String id = split2[split2.length - 1];
		
		return of(namespace, author, id);
	}
}
