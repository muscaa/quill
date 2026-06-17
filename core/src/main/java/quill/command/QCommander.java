package quill.command;

import fluff.commander.Commander;
import fluff.commander.argument.ArgumentBuilder;
import fluff.commander.argument.IArgument;
import fluff.commander.argument.StringArrayArgumentInput;
import fluff.commander.command.CommandArguments;
import fluff.commander.command.CommandException;
import quill.Quill;
import quill.command.commands.CommandFetch;
import quill.command.commands.CommandInstall;
import quill.command.commands.CommandPackages;
import quill.command.commands.CommandRepositories;
import quill.command.commands.CommandSearch;

public class QCommander extends Commander<QCommander, CommandSource> {

	public static final IArgument<Boolean> ARG_VERSION = ArgumentBuilder.Boolean("--version", "-v").build();

	public final CommandSource console = new ConsoleCommandSource();

	public QCommander() {
		super(false, "quill");
	}

	@Override
	public void init() {
		argument(ARG_VERSION);

		command(new CommandInstall());
		command(new CommandPackages());
		command(new CommandRepositories());
		command(new CommandSearch());
		command(new CommandFetch());
	}

	@Override
	public int onPreAction(QCommander c, CommandSource source, CommandArguments args) throws CommandException {
		if (args.Boolean(ARG_VERSION)) {
			System.out.println(Quill.INSTANCE.quillPackage.getVersion());
			return SUCCESS;
		}
		return super.onPreAction(c, source, args);
	}

	public int executeConsole(String[] args) throws CommandException {
		return execute(console, new StringArrayArgumentInput(args));
	}
}
