package quill;

import fluff.commander.argument.StringArrayArgumentInput;
import fluff.core.FluffCore;
import quill.command.QCommandSource;
import quill.command.QCommander;

public class Quill {
	
	public static final Quill INSTANCE = new Quill();
	
	public final QCommander commands = new QCommander();
	
	@SuppressWarnings("unused")
	private static void main(QPackage pkg, String[] args) throws Exception {
		FluffCore.init();
		
		int exit = INSTANCE.commands.execute(new QCommandSource(), new StringArrayArgumentInput(args));
		System.out.println("exit: " + exit);
	}
}
