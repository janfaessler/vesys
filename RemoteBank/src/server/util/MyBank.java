package server.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

public class MyBank implements Bank  {
	private Map<String, MyAccount> accounts = new HashMap<String, MyAccount>();
	

	@Override
	public String createAccount(String owner) {
		String number;
		synchronized (accounts) {
			MyAccount acc = new MyAccount(owner, "0-"+accounts.size()+"-1");
			number = acc.getNumber();
			accounts.put(number, acc);
		}
		System.out.println("MyBank->createAccount:"+owner+":"+number);
		return number;
	}

	@Override
	public boolean closeAccount(String number) {
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
	public Set<String> getAccountNumbers(){
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
	public MyAccount getAccount(String number){
		System.out.println("MyBank->getAccount:"+number);
		return accounts.get(number);
	}
	
	@Override
	public void transfer(Account a, Account b, double amount) throws InactiveException, IllegalArgumentException, IOException, OverdrawException {
		System.out.println("MyBank->getAccount:"+a.getNumber()+":"+b.getNumber()+":"+String.valueOf(amount));
		a.withdraw(amount);
		b.deposit(amount);
	}
}