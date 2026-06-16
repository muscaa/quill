package quill.command;

import org.fusesource.jansi.Ansi;

import quill.text.TextBuilder;
import quill.text.TextToAnsiTransform;

public class ConsoleCommandSource extends CommandSource {
	
	@Override
	public void print(TextBuilder text) {
		Ansi ansi = text.to(new TextToAnsiTransform()).ansi;
		
		System.out.println(ansi.reset());
	}
}
