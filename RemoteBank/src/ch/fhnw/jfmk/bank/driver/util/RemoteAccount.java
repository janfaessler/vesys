package ch.fhnw.jfmk.bank.driver.util;

import java.io.IOException;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;

public class RemoteAccount implements Account {
	private final String number;
	private RemoteDriver driver;
	
	public  RemoteAccount (String number, RemoteDriver d) {
		this.number = number;
		this.driver = d;
	}

	@Override
	public String getNumber() {
		return number;
	}

	@Override
	public String getOwner() throws IOException {
		return driver.sendCommand("getOwner", number);
	}

	@Override
	public boolean isActive() throws IOException {
		String status = driver.sendCommand("isActive", number);
		return status.equals("active");
	}

	@Override
	public void deposit(double amount) throws InactiveException, IOException {
		String result = driver.sendCommand("deposit",  number +";"+String.valueOf(amount));
		switch (result) {
			case "InactiveException": throw new InactiveException(); 
			case "succeed": break;
			default: throw new IOException();
		}
	}

	@Override
	public void withdraw(double amount) throws OverdrawException, InactiveException, IOException {
		String result = driver.sendCommand("withdraw", number +";"+String.valueOf(amount));
		switch (result) {
			case "InactiveException": throw new InactiveException(); 
			case "OverdrawException": throw new OverdrawException();
			case "succeed": break;
			default: throw new IOException();
		}
	}

	@Override
	public double getBalance() throws IOException {
		return Double.valueOf(driver.sendCommand("getBalance", number));
	}
}