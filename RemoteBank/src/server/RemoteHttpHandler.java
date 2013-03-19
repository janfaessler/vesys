package server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import server.util.CommandHandler;
import server.util.MyBank;
import server.util.RequestHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RemoteHttpHandler implements HttpHandler, RequestHandler {
	
	private CommandHandler cHandler;
	private HttpExchange exchange;
	
	public RemoteHttpHandler(MyBank b) {
		cHandler = new CommandHandler(b, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		exchange = httpExchange;
		exchange.getResponseHeaders().add("Content-type", "text/html");
		
		Map<String, Object> params = (Map<String, Object>) httpExchange.getAttribute("parameters");
		cHandler.handleCommand((String) params.get("cmd"), (String) params.get("param"));
	}
	
	@Override
	public void stop() throws IOException { }

	@Override
	public void sendCommand(String command, String param) throws IOException {
		String response =  command + ":" + param;
		exchange.sendResponseHeaders(200, response.length());
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
