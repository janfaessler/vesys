package ch.fhnw.jfmk.bank.server.jms;

import java.io.IOException;

import bank.Account;
import bank.BankDriver2.UpdateHandler;
import bank.InactiveException;
import bank.OverdrawException;

public class JmsAccountProxy implements Account {
	
	private Account acc;
	private UpdateHandler handler;

	public JmsAccountProxy(Account a, UpdateHandler h) {
		acc = a;
		handler = h;
	}

	@Override
	public String getNumber() throws IOException {
		return acc.getNumber();
	}

	@Override
	public String getOwner() throws IOException {
		return acc.getOwner();
	}

	@Override
	public boolean isActive() throws IOException {
		return acc.isActive();
	}

	@Override
	public void deposit(double amount) throws IOException,
			IllegalArgumentException, InactiveException {
		acc.deposit(amount);
		handler.accountChanged(acc.getNumber());
		
	}

	@Override
	public void withdraw(double amount) throws IOException,
			IllegalArgumentException, OverdrawException, InactiveException {
		acc.withdraw(amount);
		handler.accountChanged(acc.getNumber());
	}

	@Override
	public double getBalance() throws IOException {
		return acc.getBalance();
	}

	public Account getAccount() {
		return acc;
	}

}
