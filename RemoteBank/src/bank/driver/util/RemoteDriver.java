package bank.driver.util;

import java.io.IOException;

import bank.BankDriver;

public interface RemoteDriver extends BankDriver {
	public void sendCommand(String command) throws IOException;
	public void sendCommand(String command, String param) throws IOException;
	public  String receiveResult(String expected) throws IOException;
}
