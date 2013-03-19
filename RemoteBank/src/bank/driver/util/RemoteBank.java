package bank.driver.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

public class RemoteBank implements Bank  {
	private RemoteDriver driver;
	
	public RemoteBank(RemoteDriver d) throws IOException {
		driver = d;
	}

	@Override
	public String createAccount(String owner) throws IOException {
		driver.sendCommand("createAccount", owner);
		String number = driver.receiveResult("accountCreated");
		return number;
	}

	@Override
	public boolean closeAccount(String number) throws IOException {
		driver.sendCommand("closeAccount", number);
		if (driver.receiveResult("accountClosed").equals("success")) {
			return true;
		}
		return false;
	}

	@Override
	public Set<String> getAccountNumbers() throws IOException{
		driver.sendCommand("getAccountNumbers");
		String result = driver.receiveResult("accountNumbers");
		
		Set<String> numbers = new HashSet<String>();
		String[] tmp = (result != null ? result.split(",") : new String[0]);
		for (String n : tmp) numbers.add(n);
		
		return numbers;
	}
	
	@Override
	public Account getAccount(String number) throws IOException{
		driver.sendCommand("isActive", number);
		if (!driver.receiveResult("status").equals("null")) 
			return new RemoteAccount(number, driver);
		else return null;
	}
	
	@Override
	public void transfer(Account a, Account b, double amount) throws InactiveException, IllegalArgumentException, IOException, OverdrawException {
		a.withdraw(amount);
		try {
			b.deposit(amount);
		} catch (InactiveException e) {
			a.deposit(amount);
			throw e;
		}
	}
}