package tp1.impl.servers.rest;

import org.glassfish.jersey.server.ResourceConfig;
import tp1.api.service.java.Files;
import tp1.impl.servers.rest.util.GenericExceptionMapper;
import util.Debug;
import util.Flag;
import util.Token;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RestFilesDropboxServer extends AbstractRestServer{


    public static final int PORT = 5678;

    private static Logger Log = Logger.getLogger(FilesRestServer.class.getName());

    RestFilesDropboxServer(Logger log, String service, int port) {
        super(log, service, port);
    }

    @Override
    void registerResources(ResourceConfig config) {
        config.register( FilesDropboxResources.class );
        config.register( GenericExceptionMapper.class );
//		config.register( CustomLoggingFilter.class);
    }

    public static void main(String[] args) throws Exception {

        /*if(args[0].equalsIgnoreCase("true"))
            Flag.set(true);
        else
            Flag.set(false);*/
        Flag.set(Boolean.parseBoolean(args[0]));


        Debug.setLogLevel( Level.INFO, Debug.TP1);

        Token.set( args.length <= 1 ? "" : args[1] );

        new RestFilesDropboxServer(Log, Files.SERVICE_NAME, PORT).start();
    }


}
