package tp1.impl.servers.rest;

import tp1.api.service.rest.RestFiles;
import tp1.impl.clients.rest.RestDropboxClient;

import java.util.logging.Logger;

public class FilesDropboxResources extends RestResource implements RestFiles {

	public static final int PORT = 5678;

	private static Logger Log = Logger.getLogger(FilesRestServer.class.getName());
	private static String ROOT = "/root";

	private static final int HTTP_SUCCESS = 200;
	private static final String CONTENT_TYPE_HDR = "Content-Type";
	private RestDropboxClient client;
		
	public FilesDropboxResources() {
		client = new RestDropboxClient(null, "literally who");
	}

	@Override
	public void writeFile(String fileId, byte[] data, String token) {
		try {
			client.upload(ROOT + fileId, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteFile(String fileId, String token) {
		try {
			client.delete(ROOT + fileId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getFile(String fileId, String token) {
		try {
			return client.download(ROOT + fileId).value();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteUserFiles(String userId, String token) {

	}
}
