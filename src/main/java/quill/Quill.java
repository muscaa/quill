package quill;

import java.net.URL;

public class Quill {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Java args: " + String.join(", ", args));
		
		try {
			Class<?> c = Class.forName("quill.core.QuillCore");
			
			System.out.println(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		URL url = Quill.class.getProtectionDomain().getCodeSource().getLocation();
		
		System.out.println(url);
	}
}
