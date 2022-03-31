package tp1.impl.clients.rest;

import java.net.URI;
import java.util.List;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.FileInfo;
import tp1.api.service.java.Directory;
import tp1.api.service.java.Result;
import tp1.api.service.rest.RestDirectory;

public class RestDirectoryClient extends RestClient implements Directory {


	private static final String USER = "user";
	private static final String SHARE = "share";
	private static final String PASSWORD = "password";
	private static final String ACC_USER_ID = "accUserId";
	private static final String TOKEN = "token";	
	
	public RestDirectoryClient(URI serverUri) {
		super(serverUri, RestDirectory.PATH);
	}

	@Override
	public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
		System.err.println(target);
		Response r = target.path(userId)
				.path(filename)
				.path("/")
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity( data, MediaType.APPLICATION_OCTET_STREAM));
		return super.responseContents(r, Status.OK, new GenericType<FileInfo>() {});
	}

	@Override
	public Result<Void> deleteFile(String filename, String userId, String password) {
		Response r = target.path(userId)
				.path(filename)
				.queryParam(PASSWORD, password)
				.request()
				.delete();
		return super.verifyResponse(r, Status.OK);
	}

	@Override
	public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
		Response r = target.path(userId)
				.path(filename)
				.path(SHARE)
				.path( userIdShare)
				.queryParam(PASSWORD, password)
				.request()
				.post(Entity.json(null));		
		return super.verifyResponse(r, Status.OK);
	}

	@Override
	public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
		Response r = target.path(userId)
				.path(filename)
				.path(SHARE)
				.path( userIdShare)
				.queryParam(PASSWORD, password)
				.request()
				.delete();
		return super.verifyResponse(r, Status.OK);
	}

	@Override
	public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
		Response r = target.path(userId)
				.path(filename)
				.queryParam(ACC_USER_ID, accUserId)
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_OCTET_STREAM)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<byte[]>() {});
	}

	@Override
	public Result<List<FileInfo>> lsFile(String userId, String password) {
		Response r = target.path(userId)
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<List<FileInfo>>() {});
	}

	@Override
	public Result<List<String>> deleteUserFiles(String userId, String token) {
		Response r = target.path(USER).path(userId)
				.queryParam(TOKEN, token)
				.request()
				.accept( MediaType.APPLICATION_JSON)
				.delete();
		return super.responseContents(r, Status.OK, new GenericType<List<String>>() {});
	}
	

}
