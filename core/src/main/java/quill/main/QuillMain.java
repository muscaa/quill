package quill.main;

import java.io.File;
import java.util.Arrays;

import quill.local.LocalPackage;

public class QuillMain {
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Missing arguments");
			return;
		}
		
		String packageDir = args[0];
		String packageMainClass = args[1];
		String[] packageArgs = Arrays.copyOfRange(args, 2, args.length);
		
		LocalPackage p = LocalPackage.from(new File(packageDir));
		if (p == null) {
			System.out.println("Package not found");
			return;
		}
		
		// resolve dependencies
		
		p.run(packageMainClass, packageArgs);
	}
}
