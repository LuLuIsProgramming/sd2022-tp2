package tp1.impl.servers.rest;

import jakarta.inject.Singleton;
import tp1.api.service.rest.RestFiles;
import tp1.impl.clients.rest.RestDropboxClient;
import util.Flag;

import java.util.logging.Logger;

@Singleton
public class FilesDropboxResources extends RestResource implements RestFiles {

	public static final int PORT = 5678;

	private static final String DELIMITER = "$$$";

	private static Logger Log = Logger.getLogger(FilesRestServer.class.getName());
	private static String ROOT = "/root";

	private RestDropboxClient client;
		
	public FilesDropboxResources() {
		client = new RestDropboxClient();
		if(Flag.get())
			deleteFile("", "");
	}

	@Override
	public void writeFile(String fileId, byte[] data, String token) {
		try {
			client.writeFile(ROOT + "/" + fileId.replace(DELIMITER, "/"), data, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteFile(String fileId, String token) {
		try {
			if(!fileId.equals(""))
				client.deleteFile(ROOT + "/" + fileId.replace(DELIMITER, "/"), "");
			else
				client.deleteFile(ROOT, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getFile(String fileId, String token) {
		return resultOrThrow(client.getFile(ROOT + "/" + fileId.replace(DELIMITER, "/"), token));
	}

	@Override
	public void deleteUserFiles(String userId, String token) {
		try {
			client.deleteUserFiles(ROOT + "/" + userId, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
