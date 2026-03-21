package quill.local;

import java.io.File;
import java.util.Objects;

import fluff.core.utils.StringUtils;
import quill.AbstractRepository;
import quill.QFiles;
import quill.info.Spec;

public class LocalRepository extends AbstractRepository<LocalPackage> {

	protected final File dir;

	public LocalRepository(String namespace, File dir) {
		super(namespace);
		this.dir = dir;

		refresh();
	}

	public LocalRepository(String namespace) {
		this(namespace, new File(QFiles.PACKAGES, namespace));
	}

	@Override
	public void refresh() {
		super.refresh();

		for (File f : dir.listFiles(File::isDirectory)) {
			Spec spec = Spec.of(f.getName());
			if (spec == null) {
				continue;
			}

			LocalPackage p = LocalPackage.from(f, namespace);
			if (p == null) {
				continue;
			}

			packages.add(p);
		}
	}

	public File getDir() {
		return dir;
	}

	@Override
	public String toString() {
		return StringUtils.format("LocalRepository(namespace=\"${}\", dir=\"${}\")", namespace, dir);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LocalRepository r)) {
			return false;
		}
		return super.equals(r) && dir.equals(r.dir);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), dir);
	}
}
