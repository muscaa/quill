package quill.remote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import quill.AbstractRepositoryManager;
import quill.QFiles;

public class RemoteRepositoryManager extends AbstractRepositoryManager<RemotePackage, RemoteRepository> {

	@Override
	public void refresh() {
		super.refresh();

		File configFile = new File(QFiles.CONFIG, "repositories.txt");

		try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
			String rawLine;
			while ((rawLine = br.readLine()) != null) {
				String line = rawLine.strip();
				if (line.isEmpty() || line.startsWith("#"))
					continue;

				String[] split = line.split(" ", 2);
				if (split.length != 2)
					continue;

				String namespace = split[0];
				URL url = new URI(split[1]).toURL();

				List<RemoteRepository> list = repositories.getOrDefault(namespace, new LinkedList<>());
				list.add(new RemoteRepository(namespace, url));
				repositories.put(namespace, list);
			}
		} catch (Exception e) {
		}
	}
}
