package quill.command.commands;

import java.util.List;
import java.util.Map;

import fluff.commander.command.CommandArguments;
import fluff.commander.command.CommandException;
import quill.Quill;
import quill.command.Command;
import quill.command.CommandSource;
import quill.command.QCommander;
import quill.remote.RemoteRepository;
import quill.text.Text;

public class CommandRepositories extends Command {

	public CommandRepositories() {
		super("repositories");
	}

	@Override
	public int onAction(QCommander c, CommandSource source, CommandArguments args) throws CommandException {
		Map<String, List<RemoteRepository>> repositories = Quill.INSTANCE.remoteRepositories.getRepositories();
		
		for (Map.Entry<String, List<RemoteRepository>> e : repositories.entrySet()) {
			String key = e.getKey();
			List<RemoteRepository> value = e.getValue();
			
			source.print(Text.fg(Text.GREEN).s(key));
			for (RemoteRepository r : value) {
				source.print(Text.fg(Text.DARK_GRAY).tab(Text.TAB_LAST).s(r.getUrl()));
			}
		}

		return SUCCESS;
	}
}
