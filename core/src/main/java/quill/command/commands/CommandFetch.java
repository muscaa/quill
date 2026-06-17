package quill.command.commands;

import fluff.commander.command.CommandArguments;
import fluff.commander.command.CommandException;
import quill.Quill;
import quill.command.Command;
import quill.command.CommandSource;
import quill.command.QCommander;
import quill.text.Text;

public class CommandFetch extends Command {

	public CommandFetch() {
		super("fetch");
	}

	@Override
	public int onAction(QCommander c, CommandSource source, CommandArguments args) throws CommandException {
		source.print(Text.fg(Text.CYAN).s("Fetching packages..."));

		Quill.INSTANCE.remoteRepositories.fetch();

		int total = Quill.INSTANCE.remoteRepositories.getTotalPackages();
		source.print(Text.fg(Text.CYAN).s("Fetched " + total + " packages."));

		return SUCCESS;
	}
}
