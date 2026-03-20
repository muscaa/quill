package quill.info;

import java.util.Objects;
import java.util.regex.Pattern;

import quill.IPackage;

public class Spec {

	public static final String DELIMITER = "@";
	public static final Pattern PATTERN_AUTHOR = Pattern.compile("^[a-zA-Z0-9-_]+$");
	public static final Pattern PATTERN_ID = Pattern.compile("^[a-zA-Z0-9-_]+$");

	private final String author;
	private final String id;

	private Spec(String author, String id) {
		this.author = author;
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return author + DELIMITER + id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Spec s)) {
			return false;
		}
		return author.equals(s.author) && id.equals(s.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, id);
	}

	public static Spec of(String author, String id) {
		if (author == null || !PATTERN_AUTHOR.matcher(author).matches() || id == null || !PATTERN_ID.matcher(id).matches()) {
			return null;
		}
		return new Spec(author, id);
	}
	
	public static Spec of(String spec) {
		String[] split = spec.split(DELIMITER, 2);
		if (split.length != 2) {
			return null;
		}
		return of(split[0], split[1]);
	}
	
	public static Spec of(IPackage p) {
		return of(p.getAuthor(), p.getId());
	}
}
