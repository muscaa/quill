package quill.info;

import java.util.Objects;

public class Criteria {

	public static final String ANY = "*";

	private final String id;

	protected Criteria(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public boolean matches(String id) {
		return this.id.equals(id);
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Criteria c)) {
			return false;
		}
		return id.equals(c.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public static Criteria of(String id) {
		if (id == null || !Spec.PATTERN_ID.matcher(id).matches()) {
			return null;
		}
		return new Criteria(id);
	}
}
