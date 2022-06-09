package tp1.impl.servers.common;

import util.InsecureHostnameVerifier;

import javax.net.ssl.HttpsURLConnection;
import java.util.logging.Logger;

public abstract class AbstractServer {
	protected static final String INETADDR_ANY = "0.0.0.0";

	final protected int port;
	final protected Logger Log;
	final protected String service;
	
	protected AbstractServer(Logger log, String service, int port) {
		this.service = service;
		this.port = port;
		this.Log = log;
		HttpsURLConnection.setDefaultHostnameVerifier(new InsecureHostnameVerifier());
	}
	
	abstract protected void start();
	
	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s");
	}
}
