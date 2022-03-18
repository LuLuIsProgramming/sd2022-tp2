package tp1.impl.service.rest.soap;

import java.util.List;
import java.util.logging.Logger;

import jakarta.jws.WebService;
import tp1.api.FileInfo;
import tp1.api.service.java.Directory;
import tp1.api.service.java.Result;
import tp1.api.service.soap.DirectoryException;
import tp1.api.service.soap.SoapDirectory;
import tp1.impl.service.common.JavaDirectory;

@WebService(serviceName = SoapDirectory.NAME, targetNamespace = SoapDirectory.NAMESPACE, endpointInterface = SoapDirectory.INTERFACE)
public class SoapDirectoryWebService implements SoapDirectory {

	static Logger Log = Logger.getLogger(SoapDirectoryWebService.class.getName());

	final Directory impl;

	public SoapDirectoryWebService() {
		impl = new JavaDirectory();
	}

	/*
	 * Given a Result<T> returns T value or throws a DirectoryException with the corresponding error message
	 */
	private <T> T resultOrThrow(Result<T> result) throws DirectoryException {
		if (result.isOK())
			return result.value();
		else
			throw new DirectoryException(result.error().name());
	}

	@Override
	public FileInfo writeFile(String filename, byte[] data, String userId, String password) throws DirectoryException {
		return resultOrThrow( impl.writeFile(filename, data, userId, password));
	};

	@Override
	public void deleteFile(String filename, String userId, String password) throws DirectoryException {
		resultOrThrow( impl.deleteFile(filename, userId, password));
	}

	@Override
	public void shareFile(String filename, String userId, String userIdShare, String password)
			throws DirectoryException {
		resultOrThrow( impl.shareFile(filename, userId, userIdShare, password));	
	}

	@Override
	public void unshareFile(String filename, String userId, String userIdShare, String password)
			throws DirectoryException {
		resultOrThrow( impl.unshareFile(filename, userId, userIdShare, password));
	}

	@Override
	public byte[] getFile(String filename, String userId, String accUserId, String password) throws DirectoryException {
		return resultOrThrow( impl.getFile(filename, userId, accUserId, password));
	}

	@Override
	public List<FileInfo> lsFile(String userId, String password) throws DirectoryException {
		return resultOrThrow( impl.lsFile(userId, password));
	}
}
