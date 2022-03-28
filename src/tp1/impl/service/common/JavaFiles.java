package tp1.impl.service.common;

import static tp1.api.service.java.Result.error;
import static tp1.api.service.java.Result.ok;
import static tp1.api.service.java.Result.ErrorCode.NOT_FOUND;
import static tp1.api.service.java.Result.ErrorCode.INTERNAL_ERROR;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;

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
		File file = new File(rootDir + fileId);
		file.getParentFile().mkdirs();
		IO.write( file, data);
		return ok();
	}

	@Override
	public Result<Void> deleteUserFiles(String userId, String token) {
		File file = new File(rootDir + userId);
		try {
			java.nio.file.Files.walk(file.toPath())
			.sorted(Comparator.reverseOrder())
			.map(Path::toFile)
			.forEach(File::delete);
		} catch (IOException e) {
			e.printStackTrace();
			return error(INTERNAL_ERROR);
		}
		return ok();
	}
	
	
}
