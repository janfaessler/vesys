package ch.fhnw.jfmk.bank.server;

import java.io.IOException;
import java.net.InetSocketAddress;


import ch.fhnw.jfmk.bank.server.handler.HttpRequestHandler;
import ch.fhnw.jfmk.bank.server.util.MyBank;
import ch.fhnw.jfmk.bank.server.util.ParameterParser;

import com.sun.net.httpserver.HttpServer;

public class HttpBankServer {
	
	private final int port;
	private MyBank bank;
	
	public HttpBankServer(int p) {
		port = p;
		bank = new MyBank();
	}
	
	public void start() throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/bank", new HttpRequestHandler(bank)).getFilters().add(new ParameterParser());
		server.start();
		System.out.println("server started on " + server.getAddress());
	}

	public static void main(String[] args) throws IOException {
		HttpBankServer server = new HttpBankServer(Integer.valueOf(args[0]));
		server.start();
	}

}
