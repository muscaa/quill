package quill.core.pkg;

import quill.core.QException;

public class QVersion {
	
	private final int major;
	private final int minor;
	private final int patch;
	
	public QVersion(String version) throws QException {
		String[] split = version.split("\\.");
		if (split.length != 3) throw new QException("Invalid version!");
		
		try {
			major = Integer.parseInt(split[0]);
			minor = Integer.parseInt(split[1]);
			patch = Integer.parseInt(split[2]);
		} catch (NumberFormatException e) {
			throw new QException(e);
		}
	}
	
	public QVersion(int major, int minor, int patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}
	
	public int compare(QVersion version) {
		int majorDiff = major - version.major;
		if (majorDiff != 0) return majorDiff;
		
		int minorDiff = minor - version.minor;
		if (minorDiff != 0) return minorDiff;
		
		int patchDiff = patch - version.patch;
		
		return patchDiff;
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
	public String toString() {
		return major + "." + minor + "." + patch;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof QVersion version)) return false;
		
		return compare(version) == 0;
	}
}
