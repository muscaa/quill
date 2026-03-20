package quill.command;

import fluff.commander.command.AbstractCommand;

public abstract class Command extends AbstractCommand<QCommander, CommandSource> {
	
	public Command(String name, String... alias) {
		super(name, alias);
	}
}
