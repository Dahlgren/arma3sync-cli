package at.dahlgren.arma3sync.commands;

import fr.soe.a3s.controller.ObserverFileSize2;

public class CommandBuild extends Command {
	
	private double value;

	public void execute() {
		readRepositories();

		this.value = 0.0D;

		repositoryService.getRepositoryBuilderDAO().addObserverFileSize2(
				new ObserverFileSize2() {
					public void update(long v) {
						if (v > CommandBuild.this.value) {
							CommandBuild.this.value = v;
							
							if (!json) {
								System.out.println("Build complete: " + CommandBuild.this.value + " %");
							}
						}
					}
				});
		
		try {
			if (!json) {
				System.out.println("Repository build starting.");
			}
			
			if (path != null) {
				repositoryService.buildRepository(repository, path);
			} else {
				repositoryService.buildRepository(repository);
			}
			
			if (!json) {
				System.out.println("Repository build finished.");
			}
		} catch (Exception e) {
			showError("Failed to build repository.", e);
		}
	}
}
