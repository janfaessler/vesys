package ch.fhnw.jfmk.bank.server.handler;

import java.io.IOException;

import org.java_websocket.WebSocket;

import ch.fhnw.jfmk.bank.server.util.CommandHandler;
import ch.fhnw.jfmk.bank.server.util.MyBank;
import ch.fhnw.jfmk.bank.server.util.Request;

public class WebSocketHandler implements RequestHandler {
	
	private CommandHandler cHandler;
	
	public WebSocketHandler(MyBank b) {
		cHandler = new CommandHandler(b, this);
	}
	
	public void handle(WebSocket socket, Request req) throws IOException {
		String result = cHandler.handleCommand(req.getCommand(), req.getParam());
		socket.send(req.getCommand()+":"+result);
	}


}
