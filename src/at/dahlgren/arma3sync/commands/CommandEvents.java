package at.dahlgren.arma3sync.commands;

import java.util.List;

import fr.soe.a3s.dto.EventDTO;

public class CommandEvents extends Command {

	public void execute() {
		readRepositories();
		
		try {
			List<EventDTO> events = repositoryService.getEvents(repository);
			
			if (json) {
				System.out.println(gson.toJson(events));
			} else {
				System.out.println(events);
			}
		} catch (Exception e) {
			showError("Failed to get events.", e);
		}
	}
}
