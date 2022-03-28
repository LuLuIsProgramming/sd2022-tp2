package tp1.impl.service.soap;

import jakarta.jws.WebService;
import tp1.api.service.java.Files;
import tp1.api.service.java.Result;
import tp1.api.service.soap.FilesException;
import tp1.api.service.soap.SoapFiles;
import tp1.impl.service.common.JavaFiles;

@WebService(serviceName = SoapFiles.NAME, targetNamespace = SoapFiles.NAMESPACE, endpointInterface = SoapFiles.INTERFACE)
public class SoapFilesWebService implements SoapFiles {

	private static final String ROOT_DIR = "/tmp/";
	final Files impl ;
	
	public SoapFilesWebService() {
		impl = new JavaFiles(ROOT_DIR);
	}

	private <T> T resultOrThrow(Result<T> result) throws FilesException {
		if (result.isOK())
			return result.value();
		else
			throw new FilesException(result.error().name());
	}

	@Override
	public byte[] getFile(String fileId, String token) throws FilesException {
		return resultOrThrow( impl.getFile(fileId, token));
	}

	@Override
	public void deleteFile(String fileId, String token) throws FilesException {
		resultOrThrow( impl.deleteFile(fileId, token));
	}

	@Override
	public void writeFile(String fileId, byte[] data, String token) throws FilesException {
		resultOrThrow( impl.writeFile(fileId, data, token));
	}

	@Override
	public void deleteUserFiles(String userId, String token) throws FilesException {
		resultOrThrow( impl.deleteUserFiles(userId, token));
	}
}
