package quill.core.main;

import java.io.File;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

import quill.core.QuillCore;

public class QuillCoreMain {
	
	public static final File INSTALL_DIR;
	public static final File CALL_DIR;
	
	static {
		try {
			String path = Class.forName("quill.loader.QuillLoader")
					.getProtectionDomain()
					.getCodeSource()
					.getLocation()
					.getPath();
			String pathDecoded = URLDecoder.decode(path, "UTF-8");
			
			INSTALL_DIR = new File(pathDecoded).getParentFile();
			CALL_DIR = new File(System.getProperty("user.dir"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void launch(String qPackage, String[] args) throws Exception {
		System.out.println("launching " + qPackage + " with " + Arrays.asList(args));
		
		QuillCore.LOCAL.resolve(List.of(QuillCore.LOCAL.get(qPackage))).forEach(qpkg -> {
			System.out.println(qpkg.getID());
		});
	}
}
