package quill.local;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import quill.AbstractRepositoryManager;
import quill.QFiles;

public class LocalRepositoryManager extends AbstractRepositoryManager<LocalPackage, LocalRepository> {

	@Override
	public void refresh() {
		super.refresh();

		Map<String, Integer> priorityMap = new LinkedHashMap<>();
		File configFile = new File(QFiles.CONFIG, "repositories.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
			int priority = 0;
			String rawLine;
			while ((rawLine = br.readLine()) != null) {
				String line = rawLine.strip();
				if (line.isEmpty() || line.startsWith("#"))
					continue;

				String[] split = line.split(" ", 2);
				if (split.length != 2)
					continue;

				String namespace = split[0];

				if (!priorityMap.containsKey(namespace)) {
					priorityMap.put(namespace, priority++);
				}
			}
		} catch (Exception e) {
		}

		File[] sortedSubdirs = QFiles.PACKAGES.listFiles(File::isDirectory);
		Arrays.sort(sortedSubdirs,
				Comparator.comparingInt(file -> priorityMap.getOrDefault(file.getName(), priorityMap.size())));

		for (File namespaceFile : sortedSubdirs) {
			String namespace = namespaceFile.getName();

			put(namespace, new LocalRepository(namespace));
		}
	}

//public static Map<String, QPackage> packages(File dir, List<QRepository> repositories) {
//	Map<String, QPackage> map = new LinkedHashMap<>();
//
//	List<QRepository> repoList = repositories != null ? repositories : QRepository.REPOSITORIES;
//	Map<String, Integer> priorityMap = new LinkedHashMap<>();
//	int ri = 0;
//	for (QRepository repo : repoList) {
//		priorityMap.putIfAbsent(repo.namespace, ri++);
//	}
//	int defaultPriority = priorityMap.size();
//
//	File[] sortedSubdirs = dir.listFiles(File::isDirectory);
//	Arrays.sort(sortedSubdirs,
//			Comparator.comparingInt(file -> priorityMap.getOrDefault(file.getName(), defaultPriority)));
//
//	for (File namespaceFile : sortedSubdirs) {
//		String namespace = namespaceFile.getName();
//
//		for (File specFile : namespaceFile.listFiles(File::isDirectory)) {
//			String[] split = split(specFile.getName());
//			String author = split[1];
//			String id = split[2];
//
//			if (id == null || author == null)
//				continue;
//
//			QPackage pkg = new QPackage(namespace, author, id);
//			map.put(pkg.tag, pkg);
//		}
//	}
//
//	return map;
//}
}
