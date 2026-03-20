package quill.command;

import fluff.commander.command.AbstractCommand;

public abstract class QCommand extends AbstractCommand<QCommander, QCommandSource> {
	
	public QCommand(String name, String... alias) {
		super(name, alias);
	}
}
