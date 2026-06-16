package quill;

import org.fusesource.jansi.AnsiConsole;

import fluff.core.FluffCore;
import quill.bootstrap.QuillBootstrap;
import quill.command.QCommander;
import quill.local.LocalPackage;
import quill.local.LocalRepositoryManager;
import quill.remote.RemoteRepositoryManager;

public class Quill {

	public static final Quill INSTANCE = new Quill();

	public final LocalRepositoryManager localRepositories = new LocalRepositoryManager();
	public final LocalPackage quillPackage = LocalPackage.from(QuillBootstrap.QUILL);
	public final RemoteRepositoryManager remoteRepositories = new RemoteRepositoryManager();
	public final QCommander commands = new QCommander();

	private void init() throws Exception {
		FluffCore.init();
		AnsiConsole.systemInstall();

		localRepositories.refresh();
		remoteRepositories.refresh();
	}

	@SuppressWarnings("unused")
	private static void main(LocalPackage lp, String[] args) throws Exception {
		INSTANCE.init();

		INSTANCE.commands.executeConsole(args);
	}
}
