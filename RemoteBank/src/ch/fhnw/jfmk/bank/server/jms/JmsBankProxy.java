package ch.fhnw.jfmk.bank.server.jms;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import bank.Account;
import bank.Bank;
import bank.BankDriver2.UpdateHandler;
import bank.InactiveException;
import bank.OverdrawException;

public class JmsBankProxy implements Bank {
	
	private Bank bank;
	private UpdateHandler handler;
	
	public JmsBankProxy(Bank b, UpdateHandler h) {
		bank = b;
		handler = h;
	}
	
	public String createAccount(String owner) throws IOException  {
		String id = bank.createAccount(owner);
		handler.accountChanged(id);
		return id;
	}
	
	public Set<String> getAccountNumbers() throws IOException {
		return new HashSet<String>(bank.getAccountNumbers());
	}
	
	public boolean closeAccount(String number) throws IOException {
		boolean res = bank.closeAccount(number);
		if(res) handler.accountChanged(number);
		return res;
	}
	
	public Account getAccount(String number) throws IOException { 
		Account acc = bank.getAccount(number);
		return acc == null ? null : new JmsAccountProxy(acc, handler);
	}
	
	public void transfer(Account a, Account b, double amount) throws IOException, IllegalArgumentException, InactiveException, OverdrawException {
		bank.transfer(((JmsAccountProxy)a).getAccount(), ((JmsAccountProxy)b).getAccount(), amount);
	    handler.accountChanged(a.getNumber());
	    handler.accountChanged(b.getNumber());
	}

}
