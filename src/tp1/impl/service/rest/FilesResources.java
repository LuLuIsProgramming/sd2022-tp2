package tp1.impl.service.rest;

import jakarta.inject.Singleton;
import tp1.api.service.java.Files;
import tp1.api.service.rest.RestFiles;
import tp1.impl.service.common.JavaFiles;

@Singleton
public class FilesResources extends RestResource implements RestFiles {

	private static final String ROOT_DIR = "/tmp/";
	final Files impl;

	public FilesResources() {
		impl = new JavaFiles(ROOT_DIR);
	}

	@Override
	public void writeFile(String fileId, byte[] data, String token) {
		super.resultOrThrow( impl.writeFile(fileId, data, token));
	}

	@Override
	public void deleteFile(String fileId, String token) {
		super.resultOrThrow( impl.deleteFile(fileId, token));
	}

	@Override
	public byte[] getFile(String fileId, String token) {
		return resultOrThrow( impl.getFile(fileId, token));
	}

	@Override
	public void deleteUserFiles(String userId, String token) {
		super.resultOrThrow( impl.deleteUserFiles(userId, token));
	}
}
