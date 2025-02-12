package quill.bootstrap;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Arrays;

import fluff.loader.RuntimeClassLoader;

public class QuillBootstrap {
	
	public static void main(String[] args) throws Exception {
		String path = QuillBootstrap.class.getProtectionDomain()
				.getCodeSource()
				.getLocation()
				.getPath();
		File installDir = new File(URLDecoder.decode(path, "UTF-8"))
				.getParentFile()
				.getParentFile()
				.getParentFile();
		
		RuntimeClassLoader loader = new RuntimeClassLoader();
		loader.addFolder(new File(installDir, "system/quill-core/java"));
        
        Class<?> mainClass = loader.loadClass("quill.core.QuillCore");
        Method launch = mainClass.getDeclaredMethod("launch", String.class, String[].class);
        launch.setAccessible(true);
        launch.invoke(null, args[0], Arrays.copyOfRange(args, 1, args.length));
	}
}
