package ch.fhnw.jfmk.bank.server.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ch.fhnw.jfmk.bank.server.util.CommandHandler;
import ch.fhnw.jfmk.bank.server.util.MyBank;
import ch.fhnw.jfmk.bank.server.util.Request;

public class SocketHandler implements RequestHandler, Runnable {
	
	private final Socket socket;
	private CommandHandler cHandler;
	private boolean running = false;
	
	public SocketHandler(Socket s, MyBank b) {
		socket = s;
		cHandler = new CommandHandler(b, this);
	}

	@Override
	public void run() {
		running = true;
		System.out.println("handle connection from " + socket);
		try {
			while (running) {
				Request req = receiveResult();
				String result = cHandler.handleCommand(req.getCommand(), req.getParam());
				if (!req.getCommand().equals("disconnect")) sendResponse(req.getCommand(), result);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new RuntimeException(e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	
	public void stop() throws IOException {
		socket.close(); 
		running = false;
	}

	
	public void sendResponse(String command, String param) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		System.out.println("Server send: '"+command+":"+param+"'");
		out.println(command + ":" + param);
		out.flush();
	}
	
	public Request receiveResult() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String input = in.readLine();
		System.out.println("Server receive: '"+input+"'");
		String[] temp = input.split(":");
		return new Request(temp[0],temp.length > 1 ? temp[1] : "");
	}
}
