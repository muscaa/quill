package quill.main;

import java.io.File;
import java.util.Arrays;

import quill.QPackage;

public class QuillMain {
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Missing arguments");
			return;
		}
		
		String packagePath = args[0];
		String packageMainClass = args[1];
		String[] packageArgs = Arrays.copyOfRange(args, 2, args.length);
		
		QPackage pkg = QPackage.fromFile(new File(packagePath));
		if (pkg == null) {
			System.out.println("Package not found");
			return;
		}
		
		// resolve dependencies
		
		pkg.run(packageMainClass, packageArgs);
	}
}
