package bank.driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bank.driver.util.RemoteBank;
import bank.driver.util.RemoteDriver;

public class SocketBankDriver implements RemoteDriver {
	private RemoteBank bank = null;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	@Override
	public void connect(String[] args) throws IOException {
		socket = new Socket(args[0], Integer.valueOf(args[1]));
		bank = new RemoteBank(this);
		out = new PrintWriter(socket.getOutputStream());
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println("connected...");
	}
	
	@Override
	public void disconnect() throws IOException{
		sendCommand("disconnect");
		socket.close();
		bank = null;
		System.out.println("disconnected...");
	}
	
	@Override
	public bank.Bank getBank(){
		return bank;
	}
	
	public void sendCommand(String command) throws IOException {
		sendCommand(command, "");
	}
	
	public void sendCommand(String command, String param) throws IOException {
		System.out.println("Client send: '"+command+":"+param+"'");
		out.println(command + ":" + param);
		out.flush();
	}
	
	public  String receiveResult(String expected) throws IOException {
		String input = in.readLine();
		System.out.println("Client receive: '"+input+"'");
		String[] temp = input.split(":");
		if (temp[0].equals(expected)) {
			return (temp.length > 1 ? temp[1] : null );
		} throw new IOException("expected result type "+expected+" not found (found '"+temp[0]+"')");
	}
}
