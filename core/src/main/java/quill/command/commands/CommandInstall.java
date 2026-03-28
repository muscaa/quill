package quill.command.commands;

import java.io.File;

import fluff.commander.argument.ArgumentBuilder;
import fluff.commander.argument.IArgument;
import fluff.commander.command.CommandArguments;
import fluff.commander.command.CommandException;
import quill.Quill;
import quill.command.Command;
import quill.command.CommandSource;
import quill.command.QCommander;

public class CommandInstall extends Command {

	public static final IArgument<String> ARG_PACKAGE = ArgumentBuilder.String("--package", "-p").inline().build();
	public static final IArgument<String> ARG_FILE = ArgumentBuilder.String("--file", "-f").build();
	public static final IArgument<String> ARG_NAMESPACE = ArgumentBuilder.String("--namespace", "-n").build();
	// global arg?

	public CommandInstall() {
		super("install");
	}

	@Override
	public void init() {
		argument(ARG_PACKAGE);
		argument(ARG_FILE);
		argument(ARG_NAMESPACE);
	}

	@Override
	public int onAction(QCommander c, CommandSource source, CommandArguments args) throws CommandException {
		String argPackage = args.get(ARG_PACKAGE);
		String argFile = args.get(ARG_FILE);

		if (argPackage != null) {
			// search in repos, download & install
			System.out.println("PKG: " + argPackage);

			return SUCCESS;
		} else if (argFile != null) {
			String argNamespace = args.get(ARG_NAMESPACE);
			if (argNamespace == null) {
				System.out.println("Namespace argument missing");
				return FAIL;
			}

			File file = new File(argFile);

			try {
				Quill.INSTANCE.localRepositories.install(file, argNamespace);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return FAIL;
			}
		}

		return FAIL;
	}
}
