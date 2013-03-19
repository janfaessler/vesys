package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import server.util.MyBank;
import server.util.ParameterParser;

import com.sun.net.httpserver.HttpServer;

public class RemoteHttpServer {
	
	private final int port;
	private MyBank bank;
	
	public RemoteHttpServer(int p) {
		port = p;
		bank = new MyBank();
	}
	
	public void start() throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/bank", new RemoteHttpHandler(bank)).getFilters().add(new ParameterParser());
		server.start();
		System.out.println("server started on " + server.getAddress());
	}

	public static void main(String[] args) throws IOException {
		RemoteHttpServer server = new RemoteHttpServer(Integer.valueOf(args[0]));
		server.start();
	}

}
