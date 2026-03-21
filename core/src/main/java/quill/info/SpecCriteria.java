package quill.info;

import java.util.Objects;

public class SpecCriteria extends Criteria {

	private final String author;

	protected SpecCriteria(String author, String id) {
		super(id);
		this.author = author;
	}

	public boolean hasAuthor() {
		return !author.equals(ANY);
	}

	public String getAuthor() {
		return author;
	}

	public boolean matches(Spec spec) {
		return (!hasAuthor() || author.equals(spec.getAuthor())) && super.matches(spec.getId());
	}

	@Override
	public String toString() {
		return author + Spec.DELIMITER + super.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SpecCriteria c)) {
			return false;
		}
		return author.equals(c.author) && super.equals(c);
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, super.hashCode());
	}

	public static SpecCriteria of(String author, String id) {
		if (author == null) {
			author = ANY;
		}

		if (!author.equals(ANY) && !Spec.PATTERN_AUTHOR.matcher(author).matches()) {
			return null;
		}

		Criteria c = Criteria.of(id);
		if (c == null) {
			return null;
		}

		return new SpecCriteria(author, c.getId());
	}

	public static SpecCriteria of(String criteria) {
		String[] split = criteria.split(Spec.DELIMITER);
		if (split.length > 2) {
			return null;
		}

		Criteria c = Criteria.of(split[split.length - 1]);
		if (c == null) {
			return null;
		}

		String author = split.length == 2 ? split[0] : ANY;
		String id = c.getId();

		return of(author, id);
	}
}
