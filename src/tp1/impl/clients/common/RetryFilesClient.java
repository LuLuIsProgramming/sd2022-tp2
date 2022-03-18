package tp1.impl.clients.common;

import tp1.api.service.java.Files;
import tp1.api.service.java.Result;

public class RetryFilesClient extends RetryClient implements Files {

	final Files impl;

	public RetryFilesClient( Files impl ) {
		this.impl = impl;	
	}

	@Override
	public Result<byte[]> getFile(String fileId, String token) {
		return reTry( () -> impl.getFile(fileId, token));
	}

	@Override
	public Result<Void> deleteFile(String fileId, String token) {
		return reTry( () -> impl.deleteFile(fileId, token));
	}

	@Override
	public Result<Void> writeFile(String fileId, byte[] data, String token) {
		return reTry( () -> writeFile(fileId, data, token));
	}
	
}
