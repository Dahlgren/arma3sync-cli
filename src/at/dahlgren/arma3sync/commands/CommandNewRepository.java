package at.dahlgren.arma3sync.commands;

import com.beust.jcommander.Parameter;

import fr.soe.a3s.constant.Protocol;

public class CommandNewRepository extends Command {
	
	@Parameter(names = "--url", description = "Url to repository, should be ftp:// or http:// format")
	private String url;
	
	@Parameter(names = "--port", description = "Port to repository, default 21 for ftp and 80 for http")
	private String port;
	
	@Parameter(names = "--login", description = "Login to repository, anonymous by default")
	private String login = "anonymous";
	
	@Parameter(names = "--password", description = "Password to repository, disabled by default")
	private String password = "";
	
	private Protocol protocol;

	public void execute() {
		readRepositories();
		
		formatUrl();
		
		try {
			repositoryService.createRepository(repository, url, port, login, password, protocol);
			
			if (path != null) {
				repositoryService.setRepositoryPath(repository, path);
			}
			
			repositoryService.write(repository);
		} catch (Exception e) {
			showError("Failed to create repository.", e);
		}
	}
	
	private void formatUrl() {
		if (url.toLowerCase().startsWith(Protocol.FTP.getPrompt())) {
			protocol = Protocol.FTP;
			
			if (port == null) {
				port = "21";
			}
		} else if (url.toLowerCase().startsWith(Protocol.HTTP.getPrompt())) {
			protocol = Protocol.HTTP;
			
			if (port == null) {
				port = "80";
			}
		} else {
			System.out.println("Url must start with either ftp:// or http://");
			System.exit(0);
		}
		
		url.replaceAll(Protocol.FTP.getPrompt(), "").replaceAll(Protocol.HTTP.getPrompt(), "");
	}
}
