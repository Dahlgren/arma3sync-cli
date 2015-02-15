package at.dahlgren.arma3sync.commands;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

import fr.soe.a3s.dto.EventDTO;

public class CommandAddEvent extends Command {
	
	@Parameter(names = "--description", description = "Description of event")
	private String description;
	
	@Parameter(names = "--name", description = "Name of event")
	private String name;
	
	@Parameter(names = "--addons", description = "List of addons")
	private List<String> addons = new ArrayList<String>();

	public void execute() {
		readRepositories();
		
		try {
			EventDTO event = new EventDTO();
			event.setDescription(description);
			event.setName(name);
			
			for (String addon : addons) {
				event.getAddonNames().put(addon, false);
			}
			
			repositoryService.addEvent(repository, event);
			
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
			showError("Failed to create event.", e);
		}
	}
}
