package ch.fhnw.jfmk.bank.driver.util;

import java.io.IOException;

import bank.BankDriver;

public interface RemoteDriver extends BankDriver {
	public String sendCommand(String command) throws IOException;
	public String sendCommand(String command, String param) throws IOException;
}
