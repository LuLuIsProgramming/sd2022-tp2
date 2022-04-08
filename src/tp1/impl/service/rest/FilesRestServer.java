package tp1.impl.service.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.server.ResourceConfig;

import util.Token;

public class FilesRestServer extends AbstractRestServer {
	public static final int PORT = 5678;
	public static final String SERVICE_NAME = "files";
	
	private static Logger Log = Logger.getLogger(FilesRestServer.class.getName());

	
	FilesRestServer( int port ) {
		super(Log, SERVICE_NAME, port);
	}
	
	@Override
	void registerResources(ResourceConfig config) {
		config.register( FilesResources.class ); 
		config.register( GenericExceptionMapper.class );
//		config.register( CustomLoggingFilter.class);
	}
	
	public static void main(String[] args) throws Exception {

		Log.setLevel( Level.INFO );
		
		Token.set( args[0 ] );

		new FilesRestServer(PORT).start();
	}	
}