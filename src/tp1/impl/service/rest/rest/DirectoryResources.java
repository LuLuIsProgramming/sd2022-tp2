package tp1.impl.service.rest.rest;

import java.util.List;

import jakarta.inject.Singleton;
import tp1.api.FileInfo;
import tp1.api.service.java.Directory;
import tp1.api.service.rest.RestDirectory;
import tp1.impl.service.common.JavaDirectory;

@Singleton
public class DirectoryResources extends RestResource implements RestDirectory {

	final Directory impl;

	public DirectoryResources() {
		impl = new JavaDirectory();
	}

	@Override
	public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
		return super.resultOrThrow(impl.writeFile(filename, data, userId, password));
	}

	@Override
	public void deleteFile(String filename, String userId, String password) {
		super.resultOrThrow(impl.deleteFile(filename, userId, password));
	}

	@Override
	public void shareFile(String filename, String userId, String userIdShare, String password) {
		super.resultOrThrow( impl.shareFile(filename, userId, userIdShare, password));
	}

	@Override
	public void unshareFile(String filename, String userId, String userIdShare, String password) {
		super.resultOrThrow(impl.unshareFile(filename, userId, userIdShare, password));
	}

	@Override
	public byte[] getFile(String filename, String userId, String accUserId, String password) {
		return super.resultOrThrow( impl.getFile(filename, userId, accUserId, password));
	}

	@Override
	public List<FileInfo> lsFile(String userId, String password) {
		return super.resultOrThrow( impl.lsFile(userId, password));
	}
}
