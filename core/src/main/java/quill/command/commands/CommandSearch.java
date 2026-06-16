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
import quill.remote.RemotePackage;
import quill.text.Text;

public class CommandSearch extends Command {

	public CommandSearch() {
		super("search");
	}

	@Override
	public int onAction(QCommander c, CommandSource source, CommandArguments args) throws CommandException {
		List<RemotePackage> packages = Quill.INSTANCE.remoteRepositories.find(TagCriteria.of(TagCriteria.ANY));

		for (RemotePackage p : packages) {
			Release release = Release.of(p);
			
			source.print(Text.fg(Text.GREEN).s(release));
			source.print(Text.fg(Text.DARK_GRAY).tab(Text.TAB_LAST).s(p.getDescription()));
		}

		return SUCCESS;
	}
}
