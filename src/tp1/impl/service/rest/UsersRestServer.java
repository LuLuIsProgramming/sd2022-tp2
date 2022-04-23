package tp1.impl.service.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.server.ResourceConfig;

import util.Debug;
import util.Token;


public class UsersRestServer extends AbstractRestServer {
	public static final int PORT = 3456;
	public static final String SERVICE_NAME = "users";
	
	private static Logger Log = Logger.getLogger(UsersRestServer.class.getName());

	UsersRestServer( int port ) {
		super( Log, SERVICE_NAME, port);
	}
	
	
	@Override
	void registerResources(ResourceConfig config) {
		config.register( UsersResources.class ); 
		config.register( GenericExceptionMapper.class);
//		config.register( CustomLoggingFilter.class);
	}
	
	
	public static void main(String[] args) throws Exception {

		Debug.setLogLevel( Level.INFO, Debug.TP1);
		
		Token.set( args[0 ] );
		
		new UsersRestServer(PORT).start();
	}	
}