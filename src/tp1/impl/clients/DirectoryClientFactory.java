package tp1.impl.clients;

import java.net.URI;
import java.util.Random;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import tp1.api.service.java.Directory;
import tp1.impl.discovery.Discovery;

import tp1.impl.clients.rest.RestDirectoryClient;
import tp1.impl.clients.soap.SoapDirectoryClient;
import tp1.impl.clients.common.RetryDirectoryClient;

public class DirectoryClientFactory {
	static private Random rg = new Random();
	
	private static final String SERVICE = "dir";
	private static final String REST = "/rest";
	private static final String SOAP = "/soap";

	private static final long CACHE_CAPACITY = 10;

	static LoadingCache<URI, Directory> directory = CacheBuilder.newBuilder().maximumSize(CACHE_CAPACITY)
			.build(new CacheLoader<>() {
				@Override
				public Directory load(URI uri) throws Exception {
					Directory client;
					if (uri.toString().endsWith(REST))
						client = new RestDirectoryClient(uri);
					else if (uri.toString().endsWith(SOAP))
						client = new SoapDirectoryClient(uri);
					else
						throw new RuntimeException("Unknown service type..." + uri);

					return new RetryDirectoryClient(client);
				}
			});

	public static Directory get()  {
		URI[] uris = Discovery.getInstance().findUrisOf(SERVICE, 1);
		return getByUri(uris[0]);
	}

	
	public static Directory getByUri(URI uri) {
		try {
			return directory.get(uri);
		} catch (Exception x) {
			x.printStackTrace();
		}
		return null;
	}
	
	public static URI randomServerURI()  {
		URI[] uris = Discovery.getInstance().findUrisOf(SERVICE, 1);
		return uris[ rg.nextInt( uris.length )];
	}
}
