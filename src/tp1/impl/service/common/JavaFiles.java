package tp1.impl.service.common;

import static tp1.api.service.java.Result.error;
import static tp1.api.service.java.Result.ok;
import static tp1.api.service.java.Result.ErrorCode.BAD_REQUEST;
import static tp1.api.service.java.Result.ErrorCode.CONFLICT;
import static tp1.api.service.java.Result.ErrorCode.FORBIDDEN;
import static tp1.api.service.java.Result.ErrorCode.NOT_FOUND;

import java.io.File;

import tp1.api.service.java.Files;
import tp1.api.service.java.Result;
import util.IO;

public class JavaFiles implements Files {

	final String rootDir;
	
	public JavaFiles( String rootDir ) {
		this.rootDir = rootDir.endsWith("/") ? rootDir : rootDir + "/";
		new File( this.rootDir ).mkdirs();
	}

	@Override
	public Result<byte[]> getFile(String fileId, String token) {
		byte[] data = IO.read( new File( rootDir + fileId ));
		return data != null ? ok( data) : error( NOT_FOUND );
	}

	@Override
	public Result<Void> deleteFile(String fileId, String token) {
		boolean res = IO.delete( new File( rootDir + fileId ));	
		return res ? ok() : error( NOT_FOUND );
	}

	@Override
	public Result<Void> writeFile(String fileId, byte[] data, String token) {
		IO.write( new File(rootDir + fileId), data);
		return ok();
	}	
}
