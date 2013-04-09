package ch.fhnw.jfmk.bank.server;

import java.io.IOException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;

import ch.fhnw.jfmk.bank.server.util.MyBank;

public class XmlRpcBankServer {
	private final int port;
	private static MyBank bank;
	
	public XmlRpcBankServer(int p) {
		port = p;
		bank = new MyBank();
	}
	
	public static MyBank getBank() {
		return bank;
	}
	
	public void start() throws XmlRpcException, IOException {
		WebServer server = new WebServer(port);

		XmlRpcServer xmlRpcServer = server.getXmlRpcServer();

		PropertyHandlerMapping phm = new PropertyHandlerMapping();
		phm.addHandler("Bank", ch.fhnw.jfmk.bank.server.handler.XmlRpcHandler.class);
		xmlRpcServer.setHandlerMapping(phm);
		
		server.start();
		System.out.println("server started on " + server.getPort());
	}
	
	public static void main(String[] args) throws XmlRpcException, IOException {
		XmlRpcBankServer server = new XmlRpcBankServer(Integer.valueOf(args[0]));
		server.start();
	}
}
