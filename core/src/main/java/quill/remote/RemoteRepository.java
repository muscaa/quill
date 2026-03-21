package quill.remote;

import java.net.URL;
import java.util.Objects;

import fluff.core.utils.StringUtils;
import quill.AbstractRepository;

public class RemoteRepository extends AbstractRepository<RemotePackage> {

	protected final URL url;

	public RemoteRepository(String namespace, URL url) {
		super(namespace);
		this.url = url;

		refresh();
	}

	@Override
	public void refresh() {
		super.refresh();

		// TODO walk
	}

	public URL getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return StringUtils.format("RemoteRepository(namespace=\"${}\", url=\"${}\")", namespace, url);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RemoteRepository r)) {
			return false;
		}
		return super.equals(r) && url.equals(r.url);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), url);
	}
}
