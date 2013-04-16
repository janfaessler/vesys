package ch.fhnw.jfmk.bank.driver;

import java.io.IOException;

import javax.ws.rs.core.MultivaluedMap;

import bank.Bank;
import ch.fhnw.jfmk.bank.driver.util.RemoteBank;
import ch.fhnw.jfmk.bank.driver.util.RemoteDriver;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class RestBankDriver implements RemoteDriver {

	private RemoteBank bank;
	private Client client;
	private String address;
	private WebResource res;

	@Override
	public void connect(String[] args) throws IOException {
		bank = new RemoteBank(this);

		client = Client.create();
		address = args[0];
		if (!address.endsWith("/"))
			address += "/";

		System.out.println(address);
		System.out.println("RestBankDriver started...");
	}

	@Override
	public void disconnect() throws IOException {
		if (client != null)
			client.destroy();
		System.out.println("RestBankDriver DESTROYED...");
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
		System.out.println("Client send: '" + command + ":" + param + "'");
		String mime = "application/plain", result = "";
		MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
		String[] params = param.split(";");
		switch (command) {
		case "createAccount":
			res = client.resource(address + "accounts/create");
			formData.add("owner", param);
			result = res.accept(mime).post(String.class, formData);
			break;
		case "closeAccount":
			res = client.resource(address + "accounts/close/" + param);
			result = res.accept(mime).delete(String.class);
			break;
		case "getOwner":
			res = client.resource(address + "accounts/owner/" + param);
			result = res.accept(mime).get(String.class);
			break;
		case "getAccountNumbers":
			res = client.resource(address + "accounts");
			result = res.accept(mime).get(String.class);
			break;
		case "isActive":
			res = client.resource(address + "accounts/status/" + param);
			result = res.accept(mime).get(String.class);
			break;
		case "deposit":
			res = client.resource(address + "accounts/deposit/" + params[0]);
			formData.add("value", params[1]);
			result = res.accept(mime).put(String.class, formData);
			break;
		case "withdraw":
			res = client.resource(address + "accounts/withdraw/" + params[0]);
			formData.add("value", params[1]);
			result = res.accept(mime).put(String.class, formData);
			break;
		case "getBalance":
			res = client.resource(address + "accounts/balance/" + param);
			result = res.accept(mime).get(String.class);
			break;
		case "disconnect":
			bank = null;
			break;
		default:
			result = "unknown command";
			break;
		}

		System.out.println("Client receive: '" + result + "'");
		
		return (result.length() > 0) ? result : null;
	}

}
