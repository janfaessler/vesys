package ch.fhnw.jfmk.bank.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import bank.BankDriver2.UpdateHandler;

import bank.Bank;

public interface RmiRemoteBank extends Bank, Remote {
	public String NAME = RmiRemoteBank.class.getName();
	
	interface RmiUpdateHandler extends UpdateHandler, Remote {}
	public void registerUpdateHandler(RmiUpdateHandler handler) throws RemoteException;
}
