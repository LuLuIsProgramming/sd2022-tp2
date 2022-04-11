package tp1.impl.service.soap;

import java.util.List;
import java.util.logging.Logger;

import jakarta.jws.WebService;
import tp1.api.FileInfo;
import tp1.api.service.java.Directory;
import tp1.api.service.java.Result;
import tp1.api.service.java.Result.ErrorCode;
import tp1.api.service.soap.DirectoryException;
import tp1.api.service.soap.SoapDirectory;
import tp1.impl.clients.FilesClientFactory;
import tp1.impl.service.common.JavaDirectory;

@WebService(serviceName = SoapDirectory.NAME, targetNamespace = SoapDirectory.NAMESPACE, endpointInterface = SoapDirectory.INTERFACE)
public class SoapDirectoryWebService implements SoapDirectory {

	static Logger Log = Logger.getLogger(SoapDirectoryWebService.class.getName());

	final Directory impl;

	public SoapDirectoryWebService() {
		impl = new JavaDirectory();
	}

	/*
	 * Given a Result<T> returns T value or throws a DirectoryException with the
	 * corresponding error message
	 */
	private <T> T resultOrThrow(Result<T> result) throws DirectoryException {
		if (result.isOK())
			return result.value();
			throw new DirectoryException(result.error().name());
	}

	@Override
	public FileInfo writeFile(String filename, byte[] data, String userId, String password) throws DirectoryException {
		Log.info(String.format("SOAP writeFile: filename = %s, data.length = %d, userId = %s, password = %s \n", filename, data.length, userId, password));

		return resultOrThrow(impl.writeFile(filename, data, userId, password));
	}

	@Override
	public void deleteFile(String filename, String userId, String password) throws DirectoryException {
		Log.info(String.format("SOAP deleteFile: filename = %s, userId = %s, password =%s\n", filename, userId, password));

		resultOrThrow(impl.deleteFile(filename, userId, password));
	}

	@Override
	public void shareFile(String filename, String userId, String userIdShare, String password)
			throws DirectoryException {
		Log.info(String.format("SOAP shareFile: filename = %s, userId = %s, userIdShare = %s, password =%s\n", filename, userId, userIdShare, password));

		resultOrThrow(impl.shareFile(filename, userId, userIdShare, password));
	}

	@Override
	public void unshareFile(String filename, String userId, String userIdShare, String password)
			throws DirectoryException {
		Log.info(String.format("SOAP unshareFile: filename = %s, userId = %s, userIdShare = %s, password =%s\n", filename, userId, userIdShare, password));

		resultOrThrow(impl.unshareFile(filename, userId, userIdShare, password));
	}

	@Override
	public byte[] getFile(String filename, String userId, String accUserId, String password) throws DirectoryException {
		Log.info(String.format("SOAP getFile: filename = %s, userId = %s, accUserId = %s, password =%s\n", filename, userId, accUserId, password));

		var res = impl.getFile(filename, userId, accUserId, password);
		if( res.error() == ErrorCode.REDIRECT )
			res = FilesClientFactory.get().getFile( JavaDirectory.fileId(filename, userId), password);

		return resultOrThrow( res );
	}

	@Override
	public List<FileInfo> lsFile(String userId, String password) throws DirectoryException {
		Log.info(String.format("SOAP lsFile: userId = %s, password = %s\n", userId, password));

		return resultOrThrow(impl.lsFile(userId, password));
	}

	@Override
	public void deleteUserFiles(String userId, String password, String token) throws DirectoryException {
		Log.info(String.format("SOAP deleteUserFiles: user = %s, password = %s, token = %s\n", userId, password, token));

		resultOrThrow(impl.deleteUserFiles(userId, password, token));
	}
}
