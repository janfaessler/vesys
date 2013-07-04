package ch.fhnw.jfmk.bank.server.rmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;

public class RmiAccountProxy extends UnicastRemoteObject implements Remote, Account {

	private Account acc;
	
	protected RmiAccountProxy(Account a) throws RemoteException {
		super();
		acc = a;
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
	public void deposit(double amount) throws IOException, IllegalArgumentException, InactiveException {
		acc.deposit(amount);
	}

	@Override
	public void withdraw(double amount) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
		acc.withdraw(amount);
	}

	@Override
	public double getBalance() throws IOException {
		return acc.getBalance();
	}

}
