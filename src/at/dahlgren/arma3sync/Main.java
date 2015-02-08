package at.dahlgren.arma3sync;

import java.util.HashMap;
import java.util.Map;

import at.dahlgren.arma3sync.commands.Command;
import at.dahlgren.arma3sync.commands.CommandAddEvent;
import at.dahlgren.arma3sync.commands.CommandBuild;
import at.dahlgren.arma3sync.commands.CommandEvents;
import at.dahlgren.arma3sync.commands.CommandNewRepository;
import at.dahlgren.arma3sync.commands.CommandRemoveEvent;

import com.beust.jcommander.JCommander;


public class Main {

	public static void main(String[] args) {
		JCommander jc = new JCommander();
		
		Map<String, Command> commands = new HashMap<String, Command>();
		commands.put("addevent", new CommandAddEvent());
		commands.put("build", new CommandBuild());
		commands.put("events", new CommandEvents());
		commands.put("new", new CommandNewRepository());
		commands.put("removeevent", new CommandRemoveEvent());
		
		for (String key : commands.keySet()) {
			jc.addCommand(key, commands.get(key));
		}
		
		jc.parse(args);

		String command = jc.getParsedCommand().toLowerCase();
		if (commands.containsKey(command)) {
			commands.get(command).execute();
		} else {
			System.out.println("Command not found");
		}
	}
}
