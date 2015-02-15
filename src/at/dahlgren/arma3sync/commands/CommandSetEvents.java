package at.dahlgren.arma3sync.commands;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.google.gson.reflect.TypeToken;

import fr.soe.a3s.dto.EventDTO;

public class CommandSetEvents extends Command {
	
	@Parameter(names = "--events-json", description = "Events data")
	private String eventsJson;

	public void execute() {
		readRepositories();
		
		try {
			while (repositoryService.getEvents(repository) != null && repositoryService.getEvents(repository).size() > 0) {
				repositoryService.removeEvent(repository, repositoryService.getEvents(repository).get(0).getName());
			}
			
			if (eventsJson != null) {
				List<EventDTO> events = gson.fromJson(eventsJson, new TypeToken<List<EventDTO>>(){}.getType());
				for (EventDTO event : events) {
					repositoryService.addEvent(repository, event);
				}
			}
			
			if (path != null) {
				repositoryService.setRepositoryPath(repository, path);
			}
			
			repositoryService.saveToDiskEvents(repository);
			
			List<EventDTO> events = repositoryService.getEvents(repository);
			
			if (json) {
				System.out.println(gson.toJson(events));
			} else {
				System.out.println(events);
			}
		} catch (Exception e) {
			showError("Failed to set events.", e);
		}
	}
}
