package tp1.impl.clients.soap;

import java.net.URI;
import java.util.List;

import javax.xml.namespace.QName;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import tp1.api.FileInfo;
import tp1.api.service.java.Directory;
import tp1.api.service.java.Result;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.soap.SoapFiles;
import tp1.api.service.soap.SoapUsers;
import util.Url;

public class SoapDirectoryClient extends SoapClient implements Directory {

	private static final String SOAP_PORT = "SoapDirectoryWebServicePort";


	public SoapDirectoryClient( URI serverURI ) {
		super(serverURI);
	}

private SoapDirectory impl;
	
	
	synchronized private SoapDirectory impl() {
		if (impl == null) {
			QName QNAME = new QName(SoapUsers.NAMESPACE, SoapUsers.NAME);
			
			Service service = Service.create(Url.from(super.uri + WSDL), QNAME);

			QName port = new QName(SoapFiles.NAMESPACE, SOAP_PORT);
			this.impl = service.getPort(port, tp1.api.service.soap.SoapDirectory.class);
			super.setTimeouts( (BindingProvider)impl);
		}
		return impl;
	}


	@Override
	public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
		return super.tryCatchResult(() -> impl().writeFile(filename, data, userId, password));
	}


	@Override
	public Result<Void> deleteFile(String filename, String userId, String password) {
		return super.tryCatchVoid(() -> impl().deleteFile(filename, userId, password));
	}


	@Override
	public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
		return super.tryCatchVoid(() -> impl().deleteFile(filename, userId, password));
	}


	@Override
	public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
		return super.tryCatchVoid(() -> impl().deleteFile(filename, userId, password));
	}

	@Override
	public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
		return super.tryCatchResult(() -> impl().getFile(filename, userId, accUserId, password));
	}


	@Override
	public Result<List<FileInfo>> lsFile(String userId, String password) {
		return super.tryCatchResult(() -> impl().lsFile(userId, password ));
	}


	@Override
	public Result<Void> deleteUserFiles(String userId, String password, String token) {
		return super.tryCatchVoid(() -> impl().deleteUserFiles(userId, password, token));
	}
}
