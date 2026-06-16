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
			
			System.out.println(key);
			for (RemoteRepository r : value) {
				System.out.println("    " + r.getUrl());
			}
		}

		return SUCCESS;
	}
}
