package quill.local;

import java.io.File;
import java.util.Objects;

import fluff.core.utils.StringUtils;
import quill.AbstractRepository;
import quill.QFiles;
import quill.info.Spec;
import quill.info.Version;

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

			// TODO read package.json

			packages.add(new LocalPackage(namespace, spec.getAuthor(), spec.getId(), Version.of("0.0.0"), null));
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

//	public static Map<String, QPackage> packages(File dir, List<QRepository> repositories) {
//		Map<String, QPackage> map = new LinkedHashMap<>();
//
//		List<QRepository> repoList = repositories != null ? repositories : QRepository.REPOSITORIES;
//		Map<String, Integer> priorityMap = new LinkedHashMap<>();
//		int ri = 0;
//		for (QRepository repo : repoList) {
//			priorityMap.putIfAbsent(repo.namespace, ri++);
//		}
//		int defaultPriority = priorityMap.size();
//
//		File[] sortedSubdirs = dir.listFiles(File::isDirectory);
//		Arrays.sort(sortedSubdirs,
//				Comparator.comparingInt(file -> priorityMap.getOrDefault(file.getName(), defaultPriority)));
//
//		for (File namespaceFile : sortedSubdirs) {
//			String namespace = namespaceFile.getName();
//
//			for (File specFile : namespaceFile.listFiles(File::isDirectory)) {
//				String[] split = split(specFile.getName());
//				String author = split[1];
//				String id = split[2];
//
//				if (id == null || author == null)
//					continue;
//
//				QPackage pkg = new QPackage(namespace, author, id);
//				map.put(pkg.tag, pkg);
//			}
//		}
//
//		return map;
//	}
}
