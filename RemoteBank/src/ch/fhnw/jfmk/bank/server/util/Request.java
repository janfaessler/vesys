package ch.fhnw.jfmk.bank.server.util;

public class Request {
	private final String command;
	private final String param;
	public Request(String c, String p) {
		command = c;
		param = p;
	}
	public String getCommand() { return command; }
	public String getParam() { return param; }
}
