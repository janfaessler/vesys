package ch.fhnw.jfmk.bank.server.rmi;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.fhnw.jfmk.bank.server.util.MyBank;


import bank.Account;
import bank.BankDriver2.UpdateHandler;
import bank.InactiveException;
import bank.OverdrawException;

public class RmiBankProxy extends UnicastRemoteObject implements RmiRemoteBank {
	
	MyBank bank;
	private Map<String, Reference<Account>> map = new HashMap<String, Reference<Account>>();
	private List<UpdateHandler> listeners = new CopyOnWriteArrayList<>();

	public RmiBankProxy() throws RemoteException {
		super();
		bank = new MyBank();
	}

	@Override
	public String createAccount(String owner) throws IOException {
		String id = bank.createAccount(owner);
	    notifyListeners(id);
	    return id;
	}

	@Override
	public boolean closeAccount(String number) throws IOException {
		boolean res = bank.closeAccount(number);
		if(res) notifyListeners(number);
		return res;
	}

	@Override
	public Set<String> getAccountNumbers() throws IOException {
		return bank.getAccountNumbers();
	}

	@Override
	public Account getAccount(String number) throws IOException {
		Account x;
		if(map.get(number) == null || map.get(number).get()==null) {
			x = bank.getAccount(number);
		    map.put(number, new WeakReference<Account>(x));
		}
		
		return map.get(number).get();
	}

	@Override
	public void transfer(Account a, Account b, double amount) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
		bank.transfer(bank.getAccount(a.getNumber()), bank.getAccount(b.getNumber()), amount);
	}
	
	void notifyListeners(String id) throws IOException {
	    for(UpdateHandler h : listeners) { h.accountChanged(id); }
	}
	
	@Override
	public void registerUpdateHandler(RmiUpdateHandler h) {
	    listeners.add(h);
	}

}