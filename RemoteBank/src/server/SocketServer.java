package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.util.*;

public class SocketServer {
	private final int port;
	private MyBank bank;
	
	public SocketServer(int p) {
		port = p;
		bank = new MyBank();
	}
	
	public void start() {
		try (ServerSocket server = new ServerSocket(port)) {
			System.out.println("Startet Server on port " + port);
			while (true) {
				Socket s = server.accept();
				Thread t = new Thread(new SocketHandler(s, bank));
				t.start();
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void main (String args[]) {
		SocketServer server = new SocketServer(Integer.valueOf(args[0]));
		server.start();
	}
}
