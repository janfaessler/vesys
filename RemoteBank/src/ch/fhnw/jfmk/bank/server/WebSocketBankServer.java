package ch.fhnw.jfmk.bank.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import ch.fhnw.jfmk.bank.server.handler.WebSocketHandler;
import ch.fhnw.jfmk.bank.server.util.MyBank;
import ch.fhnw.jfmk.bank.server.util.Request;

public class WebSocketBankServer extends WebSocketServer  {
	
	private final int port;
	private final WebSocketHandler handler;
	
	public WebSocketBankServer(int p) {
		super( new InetSocketAddress(p) );
		port = p;
		handler = new WebSocketHandler(new MyBank());
	}
	
	public void start() {
		super.start();
		System.out.println("Startet Server on port " + port);
	}

	public static void main(String[] args) {
		WebSocketBankServer server = new WebSocketBankServer(Integer.valueOf(args[0]));
	    server.start();
	}
	
	@Override
	public void onClose(WebSocket c, int id, String s, boolean rem) {
		System.out.println("closed " + c.getRemoteSocketAddress());
	}
	@Override
	public void onError(WebSocket c, Exception ex) {
		System.out.println("onError " + c + ":" + ex.getMessage() );
	}
	@Override
	public void onMessage(WebSocket c, String message) {
		System.out.println("onMessage from" + c.getRemoteSocketAddress() + ": " + message );
		
		String[] temp = message.split(":");
		try {
			handler.handle(c, new Request(temp[0],temp.length > 1 ? temp[1] : ""));
		} catch (IOException e) { e.printStackTrace(); }
	}
	@Override
	public void onOpen(WebSocket c, ClientHandshake arg1) {
		System.out.println("onOpen " + c.getRemoteSocketAddress());
	}

}
