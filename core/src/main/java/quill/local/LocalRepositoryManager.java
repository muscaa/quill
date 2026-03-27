package quill.local;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fluff.files.FileHelper;
import fluff.files.Folder;
import quill.AbstractRepositoryManager;
import quill.QFiles;

public class LocalRepositoryManager extends AbstractRepositoryManager<LocalPackage, LocalRepository> {

	public boolean install(File packageZip, String namespace) {
		LocalPackage p = extract(packageZip, namespace);
		if (p == null) {
			return false;
		}

		return true;
	}

	public LocalPackage extract(File packageZip, String namespace) {
		Folder dest = new Folder(QFiles.TEMP, "install/" + namespace + "/" + packageZip.getName());
		FileHelper.deleteContents(dest);

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(packageZip))) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				File file = new File(dest, entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					file.getParentFile().mkdirs();

					try (FileOutputStream fos = new FileOutputStream(file)) {
						zis.transferTo(fos);
					}
				}

				zis.closeEntry();
			}
		} catch (IOException e) {
			return null;
		}

		return LocalPackage.from(dest, namespace);
	}
	
	public void clean() {
		File dest = new File(QFiles.TEMP, "install");
		FileHelper.delete(dest);
	}

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
}
