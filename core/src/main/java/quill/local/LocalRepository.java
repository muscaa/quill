package quill.local;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import fluff.core.utils.StringUtils;

public class LocalRepository {

	public static final List<LocalRepository> REPOSITORIES = repositories(new File(QFiles.CONFIG, "repositories.txt"));

	public final String namespace;
	public final String url;

	public LocalRepository(String namespace, String url) {
		this.namespace = namespace;
		this.url = url;
	}

	public File getPath() {
		return new File(QFiles.PACKAGES, namespace);
	}

	@Override
	public String toString() {
		return StringUtils.format("QRepository(namespace=\"${}\", url=\"${}\")", namespace, url);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LocalRepository r))
			return false;

		return namespace.equals(namespace) && url.equals(r.url);
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, url);
	}

	public static List<LocalRepository> repositories(File file) {
		List<LocalRepository> list = new LinkedList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String rawLine;
			while ((rawLine = br.readLine()) != null) {
				String line = rawLine.strip();
				if (line.isEmpty() || line.startsWith("#"))
					continue;

				String[] split = line.split(" ", 2);
				if (split.length != 2)
					continue;

				list.add(new LocalRepository(split[0], split[1]));
			}
		} catch (Exception e) {
		}

		return list;
	}
}
