package quill.info;

import java.util.Objects;

public class Version implements Comparable<Version> {

	public static final String DELIMITER = "-";

	private final int major;
	private final int minor;
	private final int patch;
	private final VersionStage stage;

	private Version(int major, int minor, int patch, VersionStage stage) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.stage = stage;
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

	public VersionStage getStage() {
		return stage;
	}

	@Override
	public int compareTo(Version v) {
		if (stage != v.stage)
			return stage.ordinal() - v.stage.ordinal();
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
		return major + "." + minor + "." + patch + DELIMITER + stage.name();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Version v)) {
			return false;
		}
		return major == v.major && minor == v.minor && patch == v.patch && stage == v.stage;
	}

	@Override
	public int hashCode() {
		return Objects.hash(major, minor, patch, stage);
	}

	public static Version of(int major, int minor, int patch, VersionStage stage) {
		if (major < 0 || minor < 0 || patch < 0 || stage == null) {
			return null;
		}
		return new Version(major, minor, patch, stage);
	}

	public static Version of(String version) {
		String[] split1 = version.split(DELIMITER, 2);
		VersionStage stage = VersionStage.of(split1.length == 2 ? split1[1] : null);
		
		String[] split2 = split1[0].split("\\.");
		if (split2.length != 3) {
			return null;
		}

		try {
			int major = Integer.parseInt(split2[0]);
			int minor = Integer.parseInt(split2[1]);
			int patch = Integer.parseInt(split2[2]);

			return of(major, minor, patch, stage);
		} catch (NumberFormatException e) {
		}

		return null;
	}
}
