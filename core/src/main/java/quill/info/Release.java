package quill.info;

import java.util.Objects;

import quill.IPackage;

public class Release {
	
	private final Tag tag;
	private final Version version;
	
	private Release(Tag tag, Version version) {
		this.tag = tag;
		this.version = version;
	}
	
	public Tag getTag() {
		return tag;
	}
	
	public Version getVersion() {
		return version;
	}
	
	@Override
	public String toString() {
		return tag.toString() + ":" + version.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Release r)) {
			return false;
		}
		return tag.equals(r.tag) && version.equals(r.version);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(tag, version);
	}
	
	public static Release of(Tag tag, Version version) {
		if (tag == null || version == null) {
			return null;
		}
		return new Release(tag, version);
	}
	
	public static Release of(String release) {
		String[] split = release.split(":", 2);
		if (split.length != 2) {
			return null;
		}
		return of(Tag.of(split[0]), Version.of(split[1]));
	}
	
	public static Release of(IPackage p) {
		return of(Tag.of(p), p.getVersion());
	}
}
