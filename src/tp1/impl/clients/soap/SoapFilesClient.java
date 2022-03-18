package tp1.impl.clients.soap;

import java.net.URI;

import tp1.api.service.java.Files;
import tp1.api.service.java.Result;

public class SoapFilesClient implements Files {

	public SoapFilesClient( URI serverURI ) {
		
	}

	@Override
	public Result<byte[]> getFile(String fileId, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<Void> deleteFile(String fileId, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<Void> writeFile(String fileId, byte[] data, String token) {
		// TODO Auto-generated method stub
		return null;
	}
}
