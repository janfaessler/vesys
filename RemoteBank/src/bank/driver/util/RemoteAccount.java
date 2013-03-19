package bank.driver.util;

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
		driver.sendCommand("getOwner", number);
		return driver.receiveResult("owner");
	}

	@Override
	public boolean isActive() throws IOException {
		driver.sendCommand("isActive", number);
		return driver.receiveResult("status").equals("active");
	}

	@Override
	public void deposit(double amount) throws InactiveException, IOException {
		driver.sendCommand("deposit",  number +";"+String.valueOf(amount));
		String result = driver.receiveResult("deposit");
		switch (result) {
			case "InactiveException": throw new InactiveException(); 
			case "succeed": break;
			default: throw new IOException();
		}
	}

	@Override
	public void withdraw(double amount) throws OverdrawException, InactiveException, IOException {
		driver.sendCommand("withdraw", number +";"+String.valueOf(amount));
		String result = driver.receiveResult("withdraw");
		switch (result) {
			case "InactiveException": throw new InactiveException(); 
			case "OverdrawException": throw new OverdrawException();
			case "succeed": break;
			default: throw new IOException();
		}
	}

	@Override
	public double getBalance() throws IOException {
		driver.sendCommand("getBalance", number);
		return Double.valueOf(driver.receiveResult("balance"));
	}
}