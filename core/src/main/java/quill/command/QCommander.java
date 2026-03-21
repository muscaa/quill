package quill.command;

import fluff.commander.Commander;
import quill.command.commands.CommandInstall;
import quill.command.commands.CommandPackages;

public class QCommander extends Commander<QCommander, CommandSource> {

	public QCommander() {
		super(false, "quill");
	}
	
	@Override
	public void init() {
		command(new CommandInstall());
		command(new CommandPackages());
	}
}
