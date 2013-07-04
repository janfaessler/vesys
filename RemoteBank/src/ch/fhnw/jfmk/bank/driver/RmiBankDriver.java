package ch.fhnw.jfmk.bank.driver;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import ch.fhnw.jfmk.bank.driver.util.RmiHandler;
import ch.fhnw.jfmk.bank.server.rmi.RmiRemoteBank;

import bank.Bank;
import bank.BankDriver2;

public class RmiBankDriver implements BankDriver2 {
	
	private RmiRemoteBank bank;
	
	@Override
	public void connect(String[] args) throws IOException {

		try {
			bank = (RmiRemoteBank) Naming.lookup(args[0]);
		} catch (NotBoundException e) {	
			System.out.println("Couldn't bind Bank!");
		}
	}

	@Override
	public void disconnect() throws IOException {
		bank = null;
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	@Override
	public void registerUpdateHandler(UpdateHandler handler) throws IOException {
		bank.registerUpdateHandler(new RmiHandler(handler));
	}

}
