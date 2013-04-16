package ch.fhnw.jfmk.bank.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ApplicationAdapter;
import com.sun.jersey.api.core.ResourceConfig;



import ch.fhnw.jfmk.bank.server.handler.RestHandler;
import ch.fhnw.jfmk.bank.server.util.MyBank;

public class RestBankServer {
	
	private final int port;
	private HttpServer server;
	
	public RestBankServer(int p) throws IllegalArgumentException, NullPointerException, IOException {
		port = p;
		ResourceConfig rc = new ApplicationAdapter(new BankApplication(new MyBank()));
		server = GrizzlyServerFactory.createHttpServer("http://localhost:"+port, rc);
	}
	
	public void start() throws IOException {
		server.start();
		System.out.print("server started on " + port);
		System.out.println(" - press ENTER to stop the server");
		System.in.read();
		server.stop();
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		RestBankServer server = new RestBankServer(Integer.valueOf(args[0]));
		server.start();
	}
	
	public class BankApplication extends Application {
		private Set<Object> singletons = new HashSet<Object>();
		private Set<Class<?>> classes = new HashSet<Class<?>>();

		public BankApplication(MyBank b) {
			singletons.add(new RestHandler(b));
		}

		@Override
		public Set<Class<?>> getClasses() {
			return classes;
		}

		@Override
		public Set<Object> getSingletons() {
			return singletons;
		}
	}

}
