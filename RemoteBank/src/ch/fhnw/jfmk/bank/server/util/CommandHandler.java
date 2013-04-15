package ch.fhnw.jfmk.bank.server.util;

import java.io.IOException;
import java.util.Iterator;

import ch.fhnw.jfmk.bank.server.handler.RequestHandler;
import ch.fhnw.jfmk.bank.server.handler.SocketHandler;

import bank.InactiveException;
import bank.OverdrawException;

public class CommandHandler {
	
	private MyBank bank;
	private RequestHandler rHandler;
	
	public CommandHandler(MyBank b, RequestHandler r) {
		bank = b;
		rHandler = r;
	}

	public String handleCommand(String command, String param) throws IOException {
		String result = "";
		if (param == null) param = "";
		String[] params = param.split(";");
		switch (command) {
			case "createAccount": 
				String number = bank.createAccount(param);
				result = number;
				break;
			case "closeAccount": 
				boolean success = bank.closeAccount(param);
				result = success ? "success" : "fail";
				break;
			case "getOwner":
				MyAccount acc = bank.getAccount(param);
				if (acc !=  null) result = acc.getOwner();
				break;
			case "getAccountNumbers":
				String strNumbers = new String();
				Iterator<String> it = bank.getAccountNumbers().iterator();
				while (it.hasNext()) strNumbers += it.next() + ",";
				if (strNumbers.length() > 0) strNumbers.substring(0, strNumbers.length() - 1);
				result = strNumbers;
				break;
			case "isActive": 
				MyAccount acc2 = bank.getAccount(param);
				boolean status = (acc2 != null ? acc2.isActive() : false );
				result = status?"active":(acc2 != null ?"inactive":"null");
				break;
			case "deposit": 
				try {
					bank.getAccount(params[0]).deposit(Double.valueOf(params[1]));
					result = "succeed";
				}
				catch (NumberFormatException e) { result = "NumberFormatException"; } 
				catch (InactiveException e) { result = "InactiveException"; } 
				catch (IllegalArgumentException e) { result = "IllegalArgumentException"; }
				break;
			case "withdraw": 
				try {
					bank.getAccount(params[0]).withdraw(Double.valueOf(params[1]));
					result = "succeed";
				} 
				catch (NumberFormatException e) { result = "NumberFormatException"; } 
				catch (OverdrawException e) { result = "OverdrawException"; } 
				catch (InactiveException e) { result = "InactiveException"; } 
				catch (IllegalArgumentException e) { result = "IllegalArgumentException"; }
				break;
			case "getBalance":
				MyAccount acc3 = bank.getAccount(param);
				double balance = acc3.getBalance();
				result = String.valueOf(balance);
				break;
			case "disconnect": 
				if (rHandler instanceof SocketHandler) ((SocketHandler) rHandler).stop();
				break;
			default: result = "unknown command"; break;
		}
		return result;
	}

}
