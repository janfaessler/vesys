package ch.fhnw.jfmk.bank.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ch.fhnw.jfmk.bank.server.handler.SocketHandler;
import ch.fhnw.jfmk.bank.server.util.MyBank;


public class SocketBankServer {
	private final int port;
	private MyBank bank;
	
	public SocketBankServer(int p) {
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
		SocketBankServer server = new SocketBankServer(Integer.valueOf(args[0]));
		server.start();
	}
}
