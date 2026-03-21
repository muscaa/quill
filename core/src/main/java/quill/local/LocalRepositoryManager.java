package quill.local;

import quill.AbstractRepositoryManager;

public class LocalRepositoryManager extends AbstractRepositoryManager<LocalPackage, LocalRepository> {

	@Override
	public void refresh() {
		super.refresh();

//		File configFile = new File(QFiles.CONFIG, "repositories.txt");
//
//		try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
//			String rawLine;
//			while ((rawLine = br.readLine()) != null) {
//				String line = rawLine.strip();
//				if (line.isEmpty() || line.startsWith("#"))
//					continue;
//
//				String[] split = line.split(" ", 2);
//				if (split.length != 2)
//					continue;
//
//				String namespace = split[0];
//				URL url = new URI(split[1]).toURL();
//
//				List<LocalRepository> list = repositories.getOrDefault(namespace, new LinkedList<>());
//				list.add(new LocalRepository(namespace, url));
//				repositories.put(namespace, list);
//			}
//		} catch (Exception e) {
//		}
	}
}
