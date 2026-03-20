package quill.command.commands;

import java.io.File;

import fluff.commander.argument.ArgumentBuilder;
import fluff.commander.argument.IArgument;
import fluff.commander.argument.StringArgumentInput;
import fluff.commander.command.CommandArguments;
import fluff.commander.command.CommandException;
import fluff.files.FileHelper;
import fluff.json.JSON;
import fluff.json.JSONObject;
import quill.command.Command;
import quill.command.CommandSource;
import quill.command.QCommander;

public class CommandInstall extends Command {
	
    public static final IArgument<String> ARG_PACKAGE = ArgumentBuilder
            .String("--package", "-p")
            .inline()
            .build();
    public static final IArgument<String> ARG_FILE = ArgumentBuilder
            .String("--file", "-f")
            .build();
    // global arg?
    
	public CommandInstall() {
		super("install");
	}
	
	@Override
	public void init() {
		argument(ARG_PACKAGE);
		argument(ARG_FILE);
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
			File file = new File(argFile);
			
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
								.directory(file)
								.inheritIO();
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
				
				
			} else if (file.getName().endsWith(".zip")) {
				
			}
		}
		
		return FAIL;
	}
}
