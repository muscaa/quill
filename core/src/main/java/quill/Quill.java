package quill;

import fluff.commander.argument.StringArrayArgumentInput;
import fluff.core.FluffCore;
import quill.command.CommandSource;
import quill.command.QCommander;
import quill.local.LocalPackage;
import quill.local.LocalRepositoryManager;
import quill.remote.RemoteRepositoryManager;

public class Quill {
	
	public static final Quill INSTANCE = new Quill();
	
	public final LocalRepositoryManager localRepositories = new LocalRepositoryManager();
	public final RemoteRepositoryManager remoteRepositories = new RemoteRepositoryManager();
	public final CommandSource console = new CommandSource();
	public final QCommander commands = new QCommander();
	
	private void init() throws Exception {
		FluffCore.init();
		
		localRepositories.refresh();
		remoteRepositories.refresh();
	}
	
	@SuppressWarnings("unused")
	private static void main(LocalPackage lp, String[] args) throws Exception {
		INSTANCE.init();
		
		INSTANCE.commands.execute(INSTANCE.console, new StringArrayArgumentInput(args));
	}
}
