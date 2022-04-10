package tp1.impl.service.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.server.ResourceConfig;

import util.Token;

public class DirectoryRestServer extends AbstractRestServer {
	public static final int PORT = 4567;
	public static final String SERVICE_NAME = "directory";
	
	private static Logger Log = Logger.getLogger(DirectoryRestServer.class.getName());

	
	DirectoryRestServer( int port ) {
		super(Log, SERVICE_NAME, port);
	}
	
	@Override
	void registerResources(ResourceConfig config) {
		config.register( DirectoryResources.class ); 
//		config.register( GenericExceptionMapper.class );
		
//		config.register( CustomLoggingFilter.class);
	}
	
	public static void main(String[] args) throws Exception {
		Log.setLevel( Level.INFO );

		Token.set( args[0 ] );

		new DirectoryRestServer(PORT).start();
	}	
}