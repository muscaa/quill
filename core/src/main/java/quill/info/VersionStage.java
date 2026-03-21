package quill.info;

public enum VersionStage {
	UNKNOWN,
	SNAPSHOT,
	ALPHA,
	BETA,
	STABLE,
	;

	public static VersionStage of(String stage) {
		if (stage == null || stage.isEmpty()) {
			return STABLE;
		}

		for (VersionStage s : VersionStage.values()) {
			if (s.name().equalsIgnoreCase(stage)) {
				return s;
			}
		}
		return UNKNOWN;
	}
}
