package quill.command.commands;

import java.util.List;

import fluff.commander.command.CommandArguments;
import fluff.commander.command.CommandException;
import quill.Quill;
import quill.command.Command;
import quill.command.CommandSource;
import quill.command.QCommander;
import quill.info.Release;
import quill.info.TagCriteria;
import quill.local.LocalPackage;

public class CommandPackages extends Command {

	public CommandPackages() {
		super("packages");
	}

	@Override
	public int onAction(QCommander c, CommandSource source, CommandArguments args) throws CommandException {
		List<LocalPackage> packages = Quill.INSTANCE.localRepositories.find(TagCriteria.of(TagCriteria.ANY));

		for (LocalPackage p : packages) {
			Release release = Release.of(p);

			System.out.println(release);
			System.out.println("    " + p.getDescription());
		}

		return SUCCESS;
	}
}
