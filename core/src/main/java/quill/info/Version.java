package quill.info;

import java.util.Objects;

public class Version implements Comparable<Version> {

	private final int major;
	private final int minor;
	private final int patch;

	private Version(int major, int minor, int patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getPatch() {
		return patch;
	}

	@Override
	public int compareTo(Version v) {
		if (major != v.major)
			return major - v.major;
		if (minor != v.minor)
			return minor - v.minor;
		if (patch != v.patch)
			return patch - v.patch;
		return 0;
	}

	@Override
	public String toString() {
		return major + "." + minor + "." + patch;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Version v)) {
			return false;
		}
		return major == v.major && minor == v.minor && patch == v.patch;
	}

	@Override
	public int hashCode() {
		return Objects.hash(major, minor, patch);
	}

	public static Version of(int major, int minor, int patch) {
		if (major < 0 || minor < 0 || patch < 0) {
			return null;
		}
		return new Version(major, minor, patch);
	}

	public static Version of(String version) {
		String[] split = version.split(".");
		if (split.length != 3) {
			return null;
		}

		try {
			int major = Integer.parseInt(split[0]);
			int minor = Integer.parseInt(split[1]);
			int patch = Integer.parseInt(split[2]);

			return of(major, minor, patch);
		} catch (NumberFormatException e) {
		}

		return null;
	}
}
