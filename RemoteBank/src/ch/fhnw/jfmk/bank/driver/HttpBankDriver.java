package ch.fhnw.jfmk.bank.driver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.fhnw.jfmk.bank.driver.util.RemoteBank;
import ch.fhnw.jfmk.bank.driver.util.RemoteDriver;

import bank.Bank;

public class HttpBankDriver implements RemoteDriver {
	
	private String address;
	private RemoteBank bank = null;
	private URL url = null;

	@Override
	public void connect(String[] args) throws IOException {
		bank = new RemoteBank(this);
		address = args[0];
		System.out.println(address);
		System.out.println("HTTPBankDriver started...");
	}

	@Override
	public void disconnect() throws IOException {
		sendCommand("disconnect");
		System.out.println("HTTPBankDriver end...");
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
		url = new URL(address);
		HttpURLConnection c = (HttpURLConnection) url.openConnection();
		c.setRequestProperty("User-Agent", "SocketBank/HTTPBankDriver");
		c.setRequestMethod("GET");
		c.setUseCaches (false);
	    c.setDoInput(true);
	    c.setDoOutput(true);
	    //Send request
	    DataOutputStream wr = new DataOutputStream(c.getOutputStream ());
	    wr.writeBytes("cmd="+command+"&param="+param);
	    wr.flush ();
	    wr.close ();
	    BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
	    String line;
		StringBuffer response = new StringBuffer(); 
	    while((line = r.readLine()) != null) {
	        response.append(line);
	    }
	    r.close();
	    String input = response.toString();
	    System.out.println("Client receive: '"+input+"'");
		String[] temp = input.split(":");
		return (temp.length > 1 ? temp[1] : null );
	}
}
