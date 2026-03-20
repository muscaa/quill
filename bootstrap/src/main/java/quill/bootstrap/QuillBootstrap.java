package quill.bootstrap;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import fluff.loader.RuntimeClassLoader;

public class QuillBootstrap {

	public static final RuntimeClassLoader LOADER = new RuntimeClassLoader();
	public static final List<String> LIBRARIES = new LinkedList<>();
	public static final File CWD;
	public static final File QUILL;
	public static final File HOME;

	static {
		try {
			CWD = new File(System.getProperty("user.dir"));
			QUILL = new File(QuillBootstrap.class.getProtectionDomain().getCodeSource().getLocation().toURI())
					.getParentFile().getParentFile();
			HOME = QUILL.getParentFile().getParentFile().getParentFile();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		addLibrary(QUILL);

		Class<?> clazz = LOADER.loadClass("quill.main.QuillMain");
		Method main = clazz.getDeclaredMethod("main", String[].class);
		main.setAccessible(true);
		main.invoke(null, new Object[] { args });
	}

	public static boolean addLibrary(File dir) {
		String dirPath = dir.getAbsolutePath();
		if (LIBRARIES.contains(dirPath))
			return true;

		File java = new File(dir, "java");
		Path basePath = java.toPath();
		try (Stream<Path> paths = Files.walk(basePath).parallel()) {
			paths.forEach(path -> {
				File file = path.toFile();
				if (file.isDirectory() || !file.getName().endsWith(".jar"))
					return;

				LOADER.addJar(file);
			});

			LIBRARIES.add(dirPath);
			
			return true;
		} catch (IOException e) {}

		return false;
	}
}
