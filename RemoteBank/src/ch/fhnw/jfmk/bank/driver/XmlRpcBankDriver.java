package ch.fhnw.jfmk.bank.driver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bank.Bank;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import ch.fhnw.jfmk.bank.driver.util.RemoteBank;
import ch.fhnw.jfmk.bank.driver.util.RemoteDriver;


public class XmlRpcBankDriver implements RemoteDriver {
	
	private RemoteBank bank = null;
	private XmlRpcClient client = null;

	@Override
	public void connect(String[] args) throws IOException {
		bank = new RemoteBank(this);
		String address = args[0];
		
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(address));
		
		client = new XmlRpcClient();
		client.setConfig(config);
		
		System.out.println(address);
		System.out.println("XmlRpcBankDriver started...");
	}

	@Override
	public void disconnect() throws IOException {
		sendCommand("disconnect");
		System.out.println("XmlRpcBankDriver end...");
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	@Override
	public String sendCommand(String command) throws IOException {
		return sendCommand(command, "");
	}

	@Override
	public String sendCommand(String command, String param) throws IOException {
		System.out.println("Client send: '"+command+":"+param+"'");
		List<Object> params = new ArrayList<Object>();
		params.add(command);
		params.add(param);
		String result;
		try {
			result = (String) client.execute("Bank.handle", params );
			System.out.println("Client receive: '"+result+"'");
		} catch (XmlRpcException e) {
			throw new IOException(e.getCause());
		}
		return (result.length() > 0 ? result : null );
	}

}
