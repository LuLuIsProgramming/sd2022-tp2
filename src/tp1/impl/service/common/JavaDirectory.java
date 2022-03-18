package tp1.impl.service.common;

import static tp1.api.service.java.Result.error;
import static tp1.api.service.java.Result.ok;
import static tp1.api.service.java.Result.ErrorCode.BAD_REQUEST;
import static tp1.api.service.java.Result.ErrorCode.FORBIDDEN;
import static tp1.api.service.java.Result.ErrorCode.NOT_FOUND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.java.Directory;
import tp1.api.service.java.Result;
import tp1.impl.clients.FilesClientFactory;
import tp1.impl.clients.UsersClientFactory;

public class JavaDirectory implements Directory {
	final ExecutorService executor = Executors.newCachedThreadPool();

	private static final long USER_CACHE_CAPACITY = 100;
	private static final long USER_CACHE_EXPIRATION = 120;

//	final String baseUri;
	final AtomicInteger counter = new AtomicInteger();

	final Map<String, FileInfo> files = new ConcurrentHashMap<>();
	final Map<String, Set<String>> userFiles = new ConcurrentHashMap<>();
	final Map<String, Set<String>> userOtherFiles = new ConcurrentHashMap<>();

	LoadingCache<String, User> users = CacheBuilder.newBuilder().maximumSize(USER_CACHE_CAPACITY)
			.expireAfterWrite(USER_CACHE_EXPIRATION, TimeUnit.SECONDS).build(new CacheLoader<>() {
				@Override
				public User load(String userId) throws Exception {
					return UsersClientFactory.get().fetchUser(userId).value();
				}
			});

	public JavaDirectory() {
//		this.baseUri = baseUri;
	}

	@Override
	public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
		if (badParam(filename) || badParam(userId))
			return error(BAD_REQUEST);

		if (badParam(password) || wrongPassword(userId, password))
			return error(FORBIDDEN);

		FileInfo info;
		synchronized (files) {
			var fileId = fileId(filename, userId);

			info = files.get( fileId );
			if( info == null ) {
				info = new FileInfo();
				info.setOwner(userId);
				info.setFilename(filename);
				info.setSharedWith(ConcurrentHashMap.newKeySet());
				var uri = FilesClientFactory.randomServerURI();
				info.setFileURL(String.format("%s/%s", uri, fileId));
				files.put(fileId, info);
				userFiles.computeIfAbsent(userId, (k) -> ConcurrentHashMap.newKeySet()).add(fileId);
			}
			//TODO
			FilesClientFactory.get().writeFile(fileId, data, password);
			return ok(info);
		}
	}

	@Override
	public Result<Void> deleteFile(String filename, String userId, String password) {
		if (badParam(filename) || badParam(userId))
			return error(BAD_REQUEST);

		var fileId = fileId(filename, userId);

		var info = files.get(filename);

		if (fileId == null)
			return error(NOT_FOUND);

		if (badParam(password) || wrongPassword(info.getOwner(), password))
			return error(FORBIDDEN);

		else {
			files.remove(fileId);
			userFiles.computeIfAbsent(userId, (k) -> ConcurrentHashMap.newKeySet()).remove(fileId);
			executor.execute(() -> {
				info.getSharedWith().forEach( u -> {
					userOtherFiles.remove( fileId );
				});
				FilesClientFactory.get().deleteFile(fileId, password);
			});
			return ok();
		}
	}

	@Override
	public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
		if (badParam(filename) || badParam(userId) || badParam(userIdShare))
			return error(BAD_REQUEST);

		var fileId = fileId(filename, userId);

		var info = files.get(fileId);
		if (info == null)
			return error(NOT_FOUND);

		if (badParam(password) || wrongPassword(info.getOwner(), password))
			return error(FORBIDDEN);

		synchronized( info ) {
			info.getSharedWith().add(userIdShare);
			userOtherFiles.compute(userIdShare, (k,v) -> ConcurrentHashMap.newKeySet()).add(fileId);
		}

		return ok();
	}

	@Override
	public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
		if (badParam(filename) || badParam(userId) || badParam(userIdShare))
			return error(BAD_REQUEST);

		var fileId = fileId(filename, userId);

		var info = files.get(fileId);
		if (info == null)
			return error(NOT_FOUND);

		if (badParam(password) || wrongPassword(info.getOwner(), password))
			return error(FORBIDDEN);

		synchronized(info) {
			info.getSharedWith().remove(userIdShare);			
			userOtherFiles.compute(userIdShare, (k,v) -> ConcurrentHashMap.newKeySet()).remove(fileId);
		}

		return ok();
	}

	@Override
	public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
		if (badParam(filename) || badParam(userId) || badParam(accUserId))
			return error(BAD_REQUEST);

		var fileId = fileId(filename, userId);

		var info = files.get(fileId);

		if (info == null || getUser(userId) == null || getUser(accUserId) == null)
			return error(NOT_FOUND);

		if (badParam(password) || wrongPassword(accUserId, password) || !info.hasAccess(accUserId))
			return error(FORBIDDEN);
		else
			return FilesClientFactory.get().getFile(fileId, password);
	}

	@Override
	public Result<List<FileInfo>> lsFile(String userId, String password) {
		if (badParam(userId) )
			return error(BAD_REQUEST);
		
		var user = getUser(userId);
		if( user == null )
			return error(NOT_FOUND);
		
		if (badParam(password) || wrongPassword(userId, password))
			return error(FORBIDDEN);

		var f1 = userFiles.getOrDefault(userId, Collections.emptySet());
		var f2 = userOtherFiles.getOrDefault(userId, Collections.emptySet());
		var infos = Stream.concat( f1.stream(), f2.stream()).map( files::get ).collect( Collectors.toSet());
		return ok( new ArrayList<>(infos) );
	}

	private User getUser(String userId) {
		try {
			return users.get(userId);
		} catch (Exception x) {
			x.printStackTrace();
			return null;
		}
	}

	private String fileId(String filename, String userId) {
		return userId + "-" + filename;
	}

	private boolean badParam(String str) {
		return str == null || str.length() == 0;
	}

	private boolean wrongPassword(String userId, String password) {
		var user = getUser(userId);
		return user == null || !user.getPassword().equals(password);
	}
}