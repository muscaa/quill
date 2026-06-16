package quill.command;

import fluff.commander.command.ICommandSource;
import quill.text.TextBuilder;

public abstract class CommandSource implements ICommandSource {
	
	public abstract void print(TextBuilder text);
}
