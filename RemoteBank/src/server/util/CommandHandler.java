package server.util;

import java.io.IOException;
import java.util.Iterator;

import bank.InactiveException;
import bank.OverdrawException;

public class CommandHandler {
	
	private MyBank bank;
	private RequestHandler rHandler;
	
	public CommandHandler(MyBank b, RequestHandler r) {
		bank = b;
		rHandler = r;
	}

	public void handleCommand(String command, String param) throws IOException {
		if (param == null) param = "";
		String[] params = param.split(";");
		switch (command) {
			case "createAccount": 
				String number = bank.createAccount(param);
				rHandler.sendCommand("accountCreated", number); 
				break;
			case "closeAccount": 
				boolean success = bank.closeAccount(param);
				rHandler.sendCommand("accountClosed", success ? "success" : "fail");
				break;
			case "getOwner":
				MyAccount acc = bank.getAccount(param);
				if (acc !=  null) rHandler.sendCommand("owner", acc.getOwner());
				else rHandler.sendCommand("owner", "");
				break;
			case "getAccountNumbers":
				String strNumbers = new String();
				Iterator<String> it = bank.getAccountNumbers().iterator();
				while (it.hasNext()) strNumbers += it.next() + ",";
				if (strNumbers.length() > 0) strNumbers.substring(0, strNumbers.length() - 1);
				rHandler.sendCommand("accountNumbers", strNumbers);
				break;
			case "isActive": 
				MyAccount acc2 = bank.getAccount(param);
				boolean status = (acc2 != null ? acc2.isActive() : false );
				rHandler.sendCommand("status",status?"active":(acc2 != null ?"inactive":"null"));
				break;
			case "deposit": 
				try {
					bank.getAccount(params[0]).deposit(Double.valueOf(params[1]));
					rHandler.sendCommand("deposit","succeed");
				} catch (NumberFormatException e) {
					rHandler.sendCommand("deposit","NumberFormatException");
				} catch (InactiveException e) {
					rHandler.sendCommand("deposit","InactiveException");
				} catch (IllegalArgumentException e) {
					rHandler.sendCommand("deposit","IllegalArgumentException");
				}
				break;
			case "withdraw": 
				try {
					bank.getAccount(params[0]).withdraw(Double.valueOf(params[1]));
					rHandler.sendCommand("withdraw","succeed");
				} catch (NumberFormatException e) {
					rHandler.sendCommand("deposit","NumberFormatException");
				} catch (OverdrawException e) {
					rHandler.sendCommand("deposit","OverdrawException");
				} catch (InactiveException e) {
					rHandler.sendCommand("deposit","InactiveException");
				} catch (IllegalArgumentException e) {
					rHandler.sendCommand("deposit","IllegalArgumentException");
				}
				break;
			case "getBalance":
				MyAccount acc3 = bank.getAccount(param);
				double balance = acc3.getBalance();
				rHandler.sendCommand("balance",String.valueOf(balance));
				break;
			case "disconnect": 
				rHandler.stop();
				break;
			default: rHandler.sendCommand("error", "unknown command"); break;
		}
	}

}
