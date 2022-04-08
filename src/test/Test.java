package test;

import java.util.logging.Level;

import tp1.api.User;
import tp1.api.service.java.Directory;
import tp1.api.service.java.Users;
import tp1.impl.clients.DirectoryClientFactory;
import tp1.impl.clients.UsersClientFactory;
import tp1.impl.service.rest.DirectoryRestServer;
import tp1.impl.service.rest.FilesRestServer;
import tp1.impl.service.rest.UsersRestServer;
import util.Debug;

public class Test {

	public static void main(String[] args) throws Exception {
		UsersRestServer.main( new String[] { "xxx" });
		DirectoryRestServer.main( new String[] {"xxx"});
		FilesRestServer.main(new String[] {"xxx"});

		
		Debug.setLogLevel(Level.INFO, "");
		
		Users us = UsersClientFactory.get();
		
		us.createUser( new User("smd", "Sérgio Duarte", "smd@fct.unl.pt", "12345"));
		us.createUser( new User("nmp", "Nuno Preguiça", "nmp@fct.unl.pt", "54321"));
		
		us.searchUsers("").value().forEach( System.out::println );
		
		Directory dir = DirectoryClientFactory.get();
		
		var x1 = dir.writeFile("file1", "xpto1".getBytes(), "simone", "12345");
		System.err.println( x1 );
		
		dir.writeFile("file1", "xpto1".getBytes(), "smd", "12345");
		
		dir.writeFile("file2", "xpto2".getBytes(), "nmp", "54321");
		
		dir.writeFile("file3", "xpto3".getBytes(), "smd", "12345");
		
		dir.shareFile("file1", "smd", "nmp", "12345");
		
		dir.lsFile("smd", "12345").value().forEach( System.out::println );
		dir.lsFile("nmp", "54321").value().forEach( System.out::println );
		
		var xxx = dir.getFile("file1", "smd", "smd", "12345");
		
		System.err.println("##########GETFILE: " + xxx);
//		
		dir.unshareFile("file1", "smd", "nmp", "12345");

		dir.lsFile("nmp", "54321").value().forEach( System.out::println );

		var x = dir.getFile("file1", "smd", "smd", "12345");

		System.out.println( new String(x.value()));
		System.out.println("Done...");
		
		us.deleteUser("smd", "12345");

//		dir.lsFile("nmp", "54321").value().forEach( System.out::println );

	}

}
