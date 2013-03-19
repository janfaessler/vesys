package bank.driver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import bank.Bank;
import bank.driver.util.RemoteBank;
import bank.driver.util.RemoteDriver;

public class HTTPBankDriver implements RemoteDriver {
	
	private String address;
	private RemoteBank bank = null;
	private URL url = null;
	private String input;

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
	public void sendCommand(String command) throws IOException {
		sendCommand(command, "");
	}

	@Override
	public void sendCommand(String command, String param) throws IOException {
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
	    input = response.toString();
}

	@Override
	public String receiveResult(String expected) throws IOException {

		System.out.println("Client receive: '"+input+"'");
		String[] temp = input.split(":");
		
		if (temp[0].equals(expected)) {
			return (temp.length > 1 ? temp[1] : null );
		} throw new IOException("expected result type "+expected+" not found (found '"+temp[0]+"')");
	}

}
