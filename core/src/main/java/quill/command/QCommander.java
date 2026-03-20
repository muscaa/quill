package quill.command;

import fluff.commander.Commander;
import quill.command.commands.CommandInstall;

public class QCommander extends Commander<QCommander, QCommandSource> {

	public QCommander() {
		super(false, "quill");
	}
	
	@Override
	public void init() {
		command(new CommandInstall());
	}
}
