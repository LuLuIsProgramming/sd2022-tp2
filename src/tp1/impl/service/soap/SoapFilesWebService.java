package tp1.impl.service.soap;

import java.util.logging.Logger;

import jakarta.jws.WebService;
import tp1.api.service.java.Files;
import tp1.api.service.java.Result;
import tp1.api.service.soap.FilesException;
import tp1.api.service.soap.SoapFiles;
import tp1.impl.service.common.JavaFiles;

@WebService(serviceName = SoapFiles.NAME, targetNamespace = SoapFiles.NAMESPACE, endpointInterface = SoapFiles.INTERFACE)
public class SoapFilesWebService implements SoapFiles {

	private static Logger Log = Logger.getLogger(SoapFilesWebService.class.getName());

	final Files impl ;
	
	public SoapFilesWebService() {
		impl = new JavaFiles();
	}

	private <T> T resultOrThrow(Result<T> result) throws FilesException {
		if (result.isOK())
			return result.value();
		else
			throw new FilesException(result.error().name());
	}

	@Override
	public void writeFile(String fileId, byte[] data, String token) throws FilesException {
		Log.info(String.format("SOAP writeFile: fileId = %s, data.length = %d, token = %s \n", fileId, data.length, token));

		resultOrThrow( impl.writeFile(fileId, data, token));
	}
	
	@Override
	public void deleteFile(String fileId, String token) throws FilesException {
		Log.info(String.format("SOAP deleteFile: fileId = %s, token = %s \n", fileId, token));

		resultOrThrow( impl.deleteFile(fileId, token));
	}
	
	@Override
	public byte[] getFile(String fileId, String token) throws FilesException {
		Log.info(String.format("SOAP getFile: fileId = %s,  token = %s \n", fileId, token));

		return resultOrThrow( impl.getFile(fileId, token));
	}

	@Override
	public void deleteUserFiles(String userId, String token) throws FilesException {
		Log.info(String.format("SOAP deleteUserFiles: userId = %s, token = %s \n", userId, token));

		resultOrThrow( impl.deleteUserFiles(userId, token));
	}
}
