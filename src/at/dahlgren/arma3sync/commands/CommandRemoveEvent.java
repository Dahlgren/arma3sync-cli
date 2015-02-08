package at.dahlgren.arma3sync.commands;

import java.util.List;

import com.beust.jcommander.Parameter;

import fr.soe.a3s.dto.EventDTO;

public class CommandRemoveEvent extends Command {
	
	@Parameter(names = "--name", description = "Name of event")
	private String name;

	public void execute() {
		readRepositories();
		
		try {
			repositoryService.removeEvent(repository, name);
			
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
			showError("Failed to remove event.", e);
		}
	}
}
