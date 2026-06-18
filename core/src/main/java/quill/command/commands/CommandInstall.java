package quill.command.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import fluff.commander.argument.ArgumentBuilder;
import fluff.commander.argument.IArgument;
import fluff.commander.command.CommandArguments;
import fluff.commander.command.CommandException;
import quill.QFiles;
import quill.Quill;
import quill.command.Command;
import quill.command.CommandSource;
import quill.command.QCommander;
import quill.info.TagCriteria;
import quill.remote.RemotePackage;
import quill.text.Text;

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
		String argNamespace = args.get(ARG_NAMESPACE);

		if (argPackage != null) {
			TagCriteria criteria = TagCriteria.of(argPackage);
			if (criteria == null) {
				source.print(Text.fg(Text.RED).s("Invalid package."));
				return FAIL;
			}

			List<RemotePackage> packages = Quill.INSTANCE.remoteRepositories.find(criteria);

			if (packages.isEmpty()) {
				source.print(Text.fg(Text.CYAN).s("No packages found."));
				return FAIL;
			} else if (packages.size() > 1) {
				source.print(Text.fg(Text.CYAN).s("Found multiple packages with '" + argPackage + "'"));
				return FAIL;
			}

			RemotePackage pkg = packages.getFirst();
			URI uri = pkg.getVersions().get(pkg.getVersion());
			File file = new File(QFiles.TEMP_DOWNLOAD, UUID.randomUUID().toString() + ".zip");

			try (InputStream is = uri.toURL().openStream();
					FileOutputStream fos = new FileOutputStream(file)) {
				is.transferTo(fos);
				
				Quill.INSTANCE.localRepositories.install(file, argNamespace != null ? argNamespace : pkg.getNamespace());
			} catch (Exception e) {
				source.print(Text.fg(Text.RED).s(e.getMessage()));
				return FAIL;
			}
			
			return SUCCESS;
		} else if (argFile != null) {
			if (argNamespace == null) {
				source.print(Text.fg(Text.RED).s("Namespace argument missing"));
				return FAIL;
			}

			File file = new File(argFile);

			try {
				Quill.INSTANCE.localRepositories.install(file, argNamespace);
			} catch (Exception e) {
				source.print(Text.fg(Text.RED).s(e.getMessage()));
				return FAIL;
			}
		}

		return FAIL;
	}
}
