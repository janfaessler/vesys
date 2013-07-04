package ch.fhnw.jfmk.bank.driver.util;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import ch.fhnw.jfmk.bank.server.rmi.RmiRemoteBank;

import bank.BankDriver2.UpdateHandler;

public class RmiHandler extends UnicastRemoteObject implements RmiRemoteBank.RmiUpdateHandler {
	private UpdateHandler handler;
	
	public RmiHandler(UpdateHandler handler) throws RemoteException {
		super();
		this.handler = handler;
	}

	@Override
	public void accountChanged(String id) throws IOException {
		handler.accountChanged(id);
	}

}
