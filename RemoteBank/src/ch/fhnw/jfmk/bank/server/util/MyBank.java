
package ch.fhnw.jfmk.bank.server.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

public class MyBank implements Bank, Serializable  {
	private Map<String, MyAccount> accounts = new ConcurrentHashMap<String, MyAccount>();
	private int id = 0;
	
	private synchronized String getNewId(){
		return "0-"+id+++"-1"; 
	}
	
	@Override
	public String createAccount(String owner) throws IOException {
		String number;
		synchronized (accounts) {
			MyAccount acc = new MyAccount(owner, getNewId());
			number = acc.getNumber();
			accounts.put(number, acc);
		}
		System.out.println("MyBank->createAccount:"+owner+":"+number);
		return number;
	}

	@Override
	public boolean closeAccount(String number) throws IOException {
		if (accounts.containsKey(number)) {
			MyAccount acc = accounts.get(number);
			synchronized (acc) {
				if (acc.getBalance() == 0.0) {
					acc.setInactive();
					System.out.println("MyBank->closeAccount:"+number);
					return true;
				}
			}
		} 
		return false;
	}

	@Override
	public Set<String> getAccountNumbers() throws IOException{
		System.out.println("MyBank->getAccountNumbers");
		Set<String> result = new HashSet<String>();
		Iterator<String> it = accounts.keySet().iterator();
		while (it.hasNext()) {
			MyAccount acc = accounts.get(it.next());
			if (acc.isActive()) result.add(acc.getNumber());
		}
		return result;
	}
	
	@Override
	public MyAccount getAccount(String number) throws IOException{
		System.out.println("MyBank->getAccount:"+number);
		return accounts.get(number);
	}
	
	@Override
	public void transfer(Account a, Account b, double amount) throws InactiveException, IllegalArgumentException, IOException, OverdrawException {
		System.out.println("MyBank->getAccount:"+a.getNumber()+":"+b.getNumber()+":"+String.valueOf(amount));
		Account lock1, lock2;
		synchronized(this) {
			lock1 = (a.getBalance() > b.getBalance()? a : b);
			lock2 = (a.getBalance() > b.getBalance()? b : a);
		
			synchronized(lock1) {
				synchronized(lock2) {
					a.withdraw(amount);
					b.deposit(amount);
				}
			}
		}
	}
}