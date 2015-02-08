package at.dahlgren.arma3sync.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.zip.GZIPInputStream;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;

import fr.soe.a3s.dao.RepositoryDAO;
import fr.soe.a3s.domain.repository.Events;
import fr.soe.a3s.exception.LoadingException;
import fr.soe.a3s.service.RepositoryService;

public abstract class Command {
	
	@Parameter(names = "--repository", description = "Name of repository")
	protected String repository;
	
	@Parameter(names = "--path", description = "Path to repository")
	protected String path;
	
	@Parameter(names = "--json", description = "JSON output mode")
	protected boolean json = false;
	
	protected Gson gson = new Gson();
	protected RepositoryService repositoryService = new RepositoryService();
	
	public abstract void execute();
	
	protected Events readEvents() {
		Events events = null;
		try {
			File eventsFile = new File(path + "/.a3s/events");
			if (eventsFile.exists()) {
				ObjectInputStream fRo = new ObjectInputStream(new GZIPInputStream(new FileInputStream(eventsFile)));
				if (fRo != null) {
					events = (Events)fRo.readObject();
					fRo.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return events;
	}
	
	protected void readEventsFromDiskToRepository(String repository) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = repositoryService.getClass().getDeclaredField("repositoryDAO");
		f.setAccessible(true);
		RepositoryDAO dao = (RepositoryDAO) f.get(repositoryService);
		dao.getMap().get(repository).setEvents(readEvents());
	}
	
	protected void readRepositories() {
		try {
			repositoryService.readAll();
			
			if (repository != null) {
				try {
					readEventsFromDiskToRepository(repository);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (LoadingException e) {
			System.out.println("Failed to read on or more repositories");
			System.exit(0);
		}
	}
	
	protected void showError(String error, Exception e) {
		if (json) {
			System.out.println(gson.toJson(e));
		} else {
			System.out.println(error);
			System.out.println(e.getMessage());
		}
	}
}
