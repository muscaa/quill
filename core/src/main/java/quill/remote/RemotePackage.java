package quill.remote;

import java.net.URL;
import java.util.Objects;

import fluff.core.utils.StringUtils;
import quill.IPackage;
import quill.info.Version;

public class RemotePackage implements IPackage {

	private final String namespace;
	private final String author;
	private final String id;
	private final Version version;
	private final String description;
	private final URL url;

	public RemotePackage(String namespace, String author, String id, Version version, String description, URL url) {
		this.namespace = namespace;
		this.author = author;
		this.id = id;
		this.version = version;
		this.description = description;
		this.url = url;
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
		return version;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public URL getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return StringUtils.format(
				"RemotePackage(namespace=\"${}\", author=\"${}\", id=\"${}\", version=\"${}\", description=\"${}\", url=\"${}\")",
				namespace, author, id, version, description, url);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RemotePackage rp)) {
			return false;
		}
		return namespace.equals(rp.namespace) && author.equals(rp.author) && id.equals(rp.id)
				&& version.equals(rp.version) && description.equals(rp.description) && url.equals(rp.url);
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, author, id, version, description, url);
	}
}
