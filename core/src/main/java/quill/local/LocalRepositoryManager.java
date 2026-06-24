package quill.local;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fluff.commander.argument.StringArgumentInput;
import fluff.files.FileHelper;
import fluff.files.Folder;
import fluff.json.JSON;
import fluff.json.JSONObject;
import fluff.platform.os.OS;
import quill.AbstractRepositoryManager;
import quill.QFiles;
import quill.Quill;
import quill.info.Tag;

public class LocalRepositoryManager extends AbstractRepositoryManager<LocalPackage, LocalRepository> {
	
	public Process start(String bin, Object... args) throws IOException {
		Map<String, OS> extensions = Map.of(
				"null", OS.LINUX,
				".sh", OS.LINUX,
				".exe", OS.WINDOWS,
				".cmd", OS.WINDOWS,
				".bat", OS.WINDOWS);
		String foundBin = bin;
		for (File file : QFiles.BIN.listFiles((file) -> FileHelper.getNameNoExtension(file).equals(bin))) {
			String ext = FileHelper.getExtension(file);
			OS extOs = extensions.get(Objects.toString(ext));
			if (extOs == null) continue;
			
			if (extOs == OS.SYSTEM || (OS.SYSTEM != OS.WINDOWS && extOs != OS.WINDOWS)) {
				foundBin = file.getName();
				break;
			}
		}
		
		List<String> command = new LinkedList<>();
		command.add(foundBin);
		for (Object arg : args) {
			command.add(Objects.toString(arg));
		}
		ProcessBuilder builder = new ProcessBuilder(command).inheritIO();
		Process process = builder.start();
		return process;
	}

	public void install(File file, String namespace) throws Exception {
		File packageZip = getInstallZip(file);

		Tag qtag = Tag.of(Quill.INSTANCE.quillPackage);
		LocalPackage p = extract(packageZip, namespace);
		if (p == null) {
			throw new Exception("Invalid package");
		}

		Tag tag = Tag.of(p);
		if (tag.equals(qtag)) {
			Folder quillUpdate = new Folder(QFiles.TEMP, "quill-update");
			FileHelper.deleteContents(quillUpdate);
			FileHelper.copy(p.getDir(), quillUpdate);
			FileHelper.delete(p.getDir());

//			System.exit(10); // TODO schedule to python instead
		}
		
		int exit = start("quillx", qtag + ":install", p.getDir().getAbsolutePath(), namespace).waitFor();
		FileHelper.delete(p.getDir());

		if (exit != 0) {
			throw new Exception("Package install command returned " + exit);
		}
	}
	
	public void uninstall(LocalPackage p) throws Exception {
		Tag qtag = Tag.of(Quill.INSTANCE.quillPackage);
		Tag tag = Tag.of(p);
		if (tag.equals(qtag)) {
//			System.exit(11); // TODO schedule to python instead
		}
		
		int exit = start("quillx", qtag + ":uninstall", tag).waitFor();
		if (exit != 0) {
			throw new Exception("Package uninstall command returned " + exit);
		}
	}

	protected File getInstallZip(File file) throws Exception {
		if (file.isDirectory()) {
			File quillJsonFile = new File(file, "quill.json");
			if (!quillJsonFile.exists() || !quillJsonFile.isFile()) {
				throw new Exception("quill.json file not found");
			}

			JSONObject quill = JSON.object(FileHelper.read(quillJsonFile).String());
			JSONObject install = quill.getObject("install");
			JSONObject run = install.getObject("run");
			Map<OS, String> commands = new HashMap<>();
			for (Map.Entry<String, String> e : run.iterate(JSONObject::getString)) {
				OS os = OS.getByName(e.getKey());
				commands.put(os, e.getValue());
			}
			String command = commands.getOrDefault(OS.SYSTEM, commands.get(OS.UNKNOWN));
			String from = install.getString("from");

			if (command != null) {
				ProcessBuilder builder = new ProcessBuilder(StringArgumentInput.parseArgsFromString(command))
						.directory(file).inheritIO();
				Process process = builder.start();
				int exit = process.waitFor();

				if (exit != 0) {
					throw new Exception("Project install command returned " + exit);
				}
			}

			File zip = new File(file, from);
			if (!zip.getName().endsWith(".zip")) {
				throw new Exception("Invalid package zip");
			}

			return zip;
		} else if (file.getName().endsWith(".zip")) {
			return file;
		}

		throw new Exception("Invalid install file");
	}

	protected LocalPackage extract(File packageZip, String namespace) {
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
		} finally {
			try {
				if (packageZip.getCanonicalFile().toPath().startsWith(QFiles.TEMP.getCanonicalFile().toPath())) {
					FileHelper.delete(packageZip);
				}
			} catch (IOException e) {}
		}

		return LocalPackage.from(dest, namespace);
	}

	@Override
	public void refresh() {
		super.refresh();

		Map<String, Integer> priorityMap = new LinkedHashMap<>();
		File configFile = new File(QFiles.CONFIG, "repositories.conf");
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
