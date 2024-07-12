package quill.core.pkg;

public class PackageInfo {
	
	private final String name;
	private final String description;
	
	public PackageInfo(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
}
