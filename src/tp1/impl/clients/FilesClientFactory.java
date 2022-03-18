package tp1.impl.clients;

import java.net.URI;
import java.util.Random;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import tp1.api.service.java.Files;
import tp1.impl.discovery.Discovery;

import tp1.impl.clients.rest.RestFilesClient;
import tp1.impl.clients.soap.SoapFilesClient;
import tp1.impl.clients.common.RetryFilesClient;

public class FilesClientFactory {
	static private Random rg = new Random();
	
	private static final String SERVICE = "files";
	private static final String REST = "/rest";
	private static final String SOAP = "/soap";

	private static final long CACHE_CAPACITY = 10;

	static LoadingCache<URI, Files> files = CacheBuilder.newBuilder().maximumSize(CACHE_CAPACITY)
			.build(new CacheLoader<>() {
				@Override
				public Files load(URI uri) throws Exception {
					Files client;
					if (uri.toString().endsWith(REST))
						client = new RestFilesClient(uri);
					else if (uri.toString().endsWith(SOAP))
						client = new SoapFilesClient(uri);
					else
						throw new RuntimeException("Unknown service type..." + uri);

					return new RetryFilesClient(client);
				}
			});

	public static Files get()  {
		URI[] uris = Discovery.getInstance().findUrisOf(SERVICE, 1);
		return getByUri(uris[0]);
	}

	
	public static Files getByUri(URI uri) {
		try {
			return files.get(uri);
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
