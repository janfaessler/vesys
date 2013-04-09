package ch.fhnw.jfmk.bank.server.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


import ch.fhnw.jfmk.bank.server.util.CommandHandler;
import ch.fhnw.jfmk.bank.server.util.MyBank;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpRequestHandler implements HttpHandler, RequestHandler {
	
	private CommandHandler cHandler;
	private HttpExchange exchange;
	
	public HttpRequestHandler(MyBank b) {
		cHandler = new CommandHandler(b, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		exchange = httpExchange;
		exchange.getResponseHeaders().add("Content-type", "text/html");
		
		Map<String, Object> params = (Map<String, Object>) httpExchange.getAttribute("parameters");
		
		String cmd = (String) params.get("cmd");
		String param = (String) params.get("param");
		
		System.out.println("Server receive: '"+cmd+":"+param+"'");
		String result = cHandler.handleCommand(cmd, param);
		
		String response =  cmd + ":" + result;
		System.out.println("Server send: '"+response+"'");
		exchange.sendResponseHeaders(200, response.length());
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	
	@Override
	public void stop() throws IOException { }

}
