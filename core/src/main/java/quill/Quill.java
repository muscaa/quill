package quill;

import fluff.core.FluffCore;

public class Quill {
	
	public static void main(QPackage pkg, String[] args) throws Exception {
		FluffCore.init();
		
		System.out.println(pkg);
		System.out.println("it works!");
	}
}
