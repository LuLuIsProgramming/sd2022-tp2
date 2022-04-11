package tp1.impl.clients.rest;

import java.net.URI;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.java.Files;
import tp1.api.service.java.Result;
import tp1.api.service.java.Result.ErrorCode;
import tp1.api.service.rest.RestFiles;

public class RestFilesClient extends RestClient implements Files {


	private static final String USER = "user";

	public RestFilesClient(URI serverUri) {
		super(serverUri, RestFiles.PATH);
	}
	
	@Override
	public Result<byte[]> getFile(String fileId, String token) {
		return Result.error( ErrorCode.NOT_IMPLEMENTED );
	}

	@Override
	public Result<Void> deleteFile(String fileId, String token) {
		Response r = target.path(fileId)
				.request()
				.delete();
		
		return super.verifyResponse(r, Status.OK);
	}

	@Override
	public Result<Void> writeFile(String fileId, byte[] data, String token) {
		Response r = target.path(fileId)
				.request()
				.post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));
		
		return super.verifyResponse(r, Status.OK);
	}

	@Override
	public Result<Void> deleteUserFiles(String userId, String token) {
		Response r = target.path(USER).path(userId)
				.request()
				.delete();
		
		return super.verifyResponse(r, Status.OK);
	}

	@Override
	public Result<byte[]> getUrl(String url, String token) {
		var r = client.target( url )
				.request()
				.accept( MediaType.APPLICATION_OCTET_STREAM )
				.get();
		System.err.println( r );
		return super.responseContents(r, Status.OK, new GenericType<byte[]>() {});
	}
}
