package ch.fhnw.jfmk.bank.server.handler;

import java.io.IOException;

import ch.fhnw.jfmk.bank.server.XmlRpcBankServer;
import ch.fhnw.jfmk.bank.server.util.CommandHandler;


public class XmlRpcHandler implements RequestHandler{
	
	private CommandHandler cHandler;
	
	public XmlRpcHandler () {
		cHandler = new CommandHandler(XmlRpcBankServer.getBank(), this);
	}

	public String handle(String command, String param) throws IOException {
		System.out.println("Server receive: '"+command+":"+param+"'");
		String result = cHandler.handleCommand(command, param);
		System.out.println("Server send: '"+command+":"+result+"'");
		return result;
	}

}
