package quill.info;

import java.util.Objects;

public class TagCriteria extends SpecCriteria {

	private final String namespace;

	private TagCriteria(String namespace, String author, String id) {
		super(author, id);
		this.namespace = namespace;
	}

	public boolean hasNamespace() {
		return !namespace.equals(ANY);
	}

	public String getNamespace() {
		return namespace;
	}

	public boolean matches(Tag tag) {
		return (!hasNamespace() || namespace.equals(tag.getNamespace())) && super.matches(tag.getSpec());
	}

	@Override
	public String toString() {
		return namespace + Tag.DELIMITER + super.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TagCriteria c)) {
			return false;
		}
		return namespace.equals(c.namespace) && super.equals(c);
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, super.hashCode());
	}

	public static TagCriteria of(String namespace, String author, String id) {
		if (namespace == null) {
			namespace = ANY;
		}

		if (!namespace.equals(ANY) && !Tag.PATTERN_NAMESPACE.matcher(namespace).matches()) {
			return null;
		}

		SpecCriteria c = SpecCriteria.of(author, id);
		if (c == null) {
			return null;
		}

		return new TagCriteria(namespace, author, id);
	}

	public static TagCriteria of(String criteria) {
		String[] split = criteria.split(Tag.DELIMITER);
		if (split.length > 2) {
			return null;
		}

		SpecCriteria c = SpecCriteria.of(split[split.length - 1]);
		if (c == null) {
			return null;
		}

		String namespace = split.length == 2 ? split[0] : ANY;
		String author = c.getAuthor();
		String id = c.getId();

		return of(namespace, author, id);
	}
}
