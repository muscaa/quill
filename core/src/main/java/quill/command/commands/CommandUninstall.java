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
import quill.info.TagCriteria;
import quill.local.LocalPackage;
import quill.text.Text;

public class CommandUninstall extends Command {
	
	public static final IArgument<String> ARG_PACKAGE = ArgumentBuilder.String("--package", "-p").inline().build();

	public CommandUninstall() {
		super("uninstall");
	}

	@Override
	public void init() {
		argument(ARG_PACKAGE);
	}
	
	@Override
	public int onAction(QCommander c, CommandSource source, CommandArguments args) throws CommandException {
		String argPackage = args.get(ARG_PACKAGE);
		TagCriteria criteria = TagCriteria.of(argPackage);
		if (criteria == null) {
			source.print(Text.fg(Text.RED).s("Invalid package."));
			return FAIL;
		}

		List<LocalPackage> packages = Quill.INSTANCE.localRepositories.find(criteria);

		if (packages.isEmpty()) {
			source.print(Text.fg(Text.CYAN).s("No packages found."));
			return FAIL;
		} else if (packages.size() > 1) {
			source.print(Text.fg(Text.CYAN).s("Found multiple packages with '" + argPackage + "'"));
			return FAIL;
		}

		LocalPackage pkg = packages.getFirst();

		try {
			Quill.INSTANCE.localRepositories.uninstall(pkg);
		} catch (Exception e) {
			source.print(Text.fg(Text.RED).s(e.getMessage()));
			return FAIL;
		}

		return SUCCESS;
	}
}
