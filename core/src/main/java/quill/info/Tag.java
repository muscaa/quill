package quill.info;

import java.util.Objects;
import java.util.regex.Pattern;

import quill.IPackage;

public class Tag {
	
	public static final String DELIMITER = "/";
	public static final Pattern PATTERN_NAMESPACE = Pattern.compile("^[a-zA-Z0-9-_]+$");
	
	private final String namespace;
	private final Spec spec;
	
	private Tag(String namespace, Spec spec) {
		this.namespace = namespace;
		this.spec = spec;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public Spec getSpec() {
		return spec;
	}
	
	@Override
	public String toString() {
		return namespace + DELIMITER + spec.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Tag t)) {
			return false;
		}
		return namespace.equals(t.namespace) && spec.equals(t.spec);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(namespace, spec);
	}
	
	public static Tag of(String namespace, Spec spec) {
		if (!PATTERN_NAMESPACE.matcher(namespace).matches() || spec == null) {
			return null;
		}
		return new Tag(namespace, spec);
	}
	
	public static Tag of(String tag) {
		String[] split = tag.split(DELIMITER, 2);
		if (split.length != 2) {
			return null;
		}
		return of(split[0], Spec.of(split[1]));
	}
	
	public static Tag of(IPackage p) {
		return of(p.getNamespace(), Spec.of(p));
	}
}
