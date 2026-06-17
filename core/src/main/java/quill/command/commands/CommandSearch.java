package quill.command.commands;

import java.util.List;

import fluff.commander.argument.ArgumentBuilder;
import fluff.commander.argument.IArgument;
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
	
	public static final IArgument<String> ARG_PACKAGE = ArgumentBuilder.String("--package", "-p").inline().build();

	public CommandSearch() {
		super("search");
	}

	@Override
	public void init() {
		argument(ARG_PACKAGE);
	}
	
	@Override
	public int onAction(QCommander c, CommandSource source, CommandArguments args) throws CommandException {
		String argPackage = args.get(ARG_PACKAGE);
		TagCriteria criteria = argPackage != null ? TagCriteria.of(argPackage) : TagCriteria.of(TagCriteria.ANY);
		if (criteria == null) {
			source.print(Text.fg(Text.RED).s("Invalid package."));
			return FAIL;
		}
		
		List<RemotePackage> packages = Quill.INSTANCE.remoteRepositories.find(criteria);
		
		if (packages.isEmpty()) {
			source.print(Text.fg(Text.CYAN).s("No packages found."));
		}

		for (RemotePackage p : packages) {
			Release release = Release.of(p);
			
			source.print(Text.fg(Text.GREEN).s(release));
			source.print(Text.fg(Text.DARK_GRAY).tab(Text.TAB_LAST).s(p.getDescription()));
		}

		return SUCCESS;
	}
}
