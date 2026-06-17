package quill.remote;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import fluff.core.utils.StringUtils;
import quill.IPackage;
import quill.info.Version;

public class RemotePackage implements IPackage {

	private final String namespace;
	private final String author;
	private final String id;
	private final Version latest;
	private final String description;
	private final Map<Version, URI> versions;

	public RemotePackage(String namespace, String author, String id, Version latest, String description, Map<Version, URI> versions) {
		this.namespace = namespace;
		this.author = author;
		this.id = id;
		this.latest = latest;
		this.description = description;
		this.versions = versions;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Version getVersion() {
		return latest;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public Map<Version, URI> getVersions() {
		return versions;
	}

	@Override
	public String toString() {
		return StringUtils.format(
				"RemotePackage(namespace=\"${}\", author=\"${}\", id=\"${}\", latest=\"${}\", description=\"${}\", versions=\"${}\")",
				namespace, author, id, latest, description, versions);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RemotePackage rp)) {
			return false;
		}
		return namespace.equals(rp.namespace) && author.equals(rp.author) && id.equals(rp.id)
				&& latest.equals(rp.latest) && description.equals(rp.description) && versions.equals(rp.versions);
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, author, id, latest, description, versions);
	}
}
