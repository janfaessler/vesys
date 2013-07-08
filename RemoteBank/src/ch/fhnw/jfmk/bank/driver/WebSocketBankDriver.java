package ch.fhnw.jfmk.bank.driver;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import bank.Bank;
import bank.BankDriver2;
import ch.fhnw.jfmk.bank.driver.util.RemoteBank;
import ch.fhnw.jfmk.bank.driver.util.RemoteDriver;

public class WebSocketBankDriver implements RemoteDriver, BankDriver2 {
	private RemoteBank bank = null;
	private WebSocketClient c;
	BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
	
	@Override
	public void connect(String[] args) throws IOException {
		bank = new RemoteBank(this);
		URI uri;
		try {
			uri = new URI("ws://" + args[0]);
			c = new WebSocketClientHandler(uri); 
			c.connectBlocking();
		} catch (Exception e) { throw new IOException(e); }
		
	}

	@Override
	public void disconnect() throws IOException {
		sendCommand("disconnect");
		c.close();
		bank = null;
		System.out.println("disconnected...");
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
		c.send(command + ":" + param);
		String result = "";

		try {
			result =  queue.take();
		} catch (InterruptedException e) { throw new IOException(e); }

		return (result.length() == 0 ? null : result);
	}
	
	private List<UpdateHandler> uh = new CopyOnWriteArrayList<>(); 
	@Override
	public void registerUpdateHandler(UpdateHandler handler) {
	  uh.add(handler);
	}
	
	private class WebSocketClientHandler extends WebSocketClient {
		public WebSocketClientHandler(URI uri) {
			super(uri, new Draft_17());
		}
		@Override public void onOpen(ServerHandshake handshakedata) { }
		@Override public void onError(Exception ex) { }
		@Override public void onClose(int code, String reason, boolean rem) { } 
		@Override public void onMessage(String message) {
			System.out.println("Client receive: '"+message+"'");
			
			if (message != null) {
				String[] temp = message.split(":");
				if (temp[0].equals("withdraw") || temp[0].equals("deposit") || temp[0].equals("closeAccount")) {
					for (UpdateHandler h : uh) try { h.accountChanged(temp[1]); } catch (IOException e) { }
				}
				message = (temp.length > 1 ? temp[1] : "" );
			} 
			queue.offer(message);
		}
	}

}
