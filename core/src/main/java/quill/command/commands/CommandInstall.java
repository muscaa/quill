package quill.command.commands;

import java.io.File;

import fluff.commander.argument.ArgumentBuilder;
import fluff.commander.argument.IArgument;
import fluff.commander.argument.StringArgumentInput;
import fluff.commander.command.CommandArguments;
import fluff.commander.command.CommandException;
import fluff.files.FileHelper;
import fluff.files.Folder;
import fluff.json.JSON;
import fluff.json.JSONObject;
import quill.QFiles;
import quill.Quill;
import quill.bootstrap.QuillBootstrap;
import quill.command.Command;
import quill.command.CommandSource;
import quill.command.QCommander;
import quill.info.Tag;
import quill.local.LocalPackage;
import quill.local.LocalRepositoryManager;

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
			
			if (QuillBootstrap.ENV_QPOST) {
				return SUCCESS;
			}

			File file = new File(argFile);
			File packageZip = null;

			if (file.isDirectory()) {
				File quillJsonFile = new File(file, "quill.json");
				if (!quillJsonFile.exists() || !quillJsonFile.isFile()) {
					System.out.println("quill.json file not found");
					return FAIL;
				}

				JSONObject quill = JSON.object(FileHelper.read(quillJsonFile).String());
				JSONObject install = quill.getObject("install");
				String run = install.getString("run");
				String from = install.getString("from");

				if (run != null) {
					try {
						ProcessBuilder builder = new ProcessBuilder(StringArgumentInput.parseArgsFromString(run))
								.directory(file).inheritIO();
						Process process = builder.start();
						int exit = process.waitFor();

						if (exit != 0) {
							return FAIL;
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return FAIL;
					}
				}

				File zip = new File(file, from);
				if (!zip.getName().endsWith(".zip")) {
					return FAIL;
				}

				packageZip = zip;
			} else if (file.getName().endsWith(".zip")) {
				packageZip = file;
			}

			if (packageZip == null) {
				return FAIL;
			}

			LocalPackage quillPackage = LocalPackage.from(QuillBootstrap.QUILL);
			LocalRepositoryManager repos = Quill.INSTANCE.localRepositories;
			LocalPackage p = repos.extract(packageZip, argNamespace);
			if (p == null) {
				return FAIL;
			}
			
			Tag tag = Tag.of(p);
			if (tag.equals(Tag.of(quillPackage))) {
				Folder quillUpdate = new Folder(QFiles.TEMP, "quill-update");
				FileHelper.deleteContents(quillUpdate);
				FileHelper.copy(p.getDir(), quillUpdate);
				
				System.exit(10);
			}
			
			// Quill.INSTANCE.localRepositories.install(packageZip, argNamespace);
		}

		return FAIL;
	}
}
