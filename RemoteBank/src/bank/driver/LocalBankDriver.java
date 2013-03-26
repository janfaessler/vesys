package bank.driver;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import server.util.MyAccount;

import bank.Account;
import bank.Bank;
import bank.BankDriver;
import bank.InactiveException;
import bank.OverdrawException;

/*
* This class implements a dummy driver which can be used to start and test
* the GUI application. With this implementation no new accounts can be created
* nor can accounts be removed. The implementation provides one account which
* supports the deposit and withdraw operations.
*
* @see BankDriver
*/


public class LocalBankDriver implements BankDriver {
	private Bank bank = null;

	@Override
	public void connect(String[] args){
		bank = new MyBank();
		System.out.println("connected...");
	}
	
	@Override
	public void disconnect(){
		bank = null;
		System.out.println("disconnected...");
	}
	
	@Override
	public bank.Bank getBank(){
		return bank;
	}

	static class MyBank implements Bank  {
		private Map<String, MyAccount> accounts = new HashMap<String, MyAccount>();
		

		@Override
		public String createAccount(String owner) {
			MyAccount acc = new MyAccount(owner, "0-"+accounts.size()+"-1");
			accounts.put(acc.getNumber(), acc);
			return acc.getNumber();
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
			Set<String> result = new HashSet<String>();
			Iterator<String> it = accounts.keySet().iterator();
			while (it.hasNext()) {
				MyAccount acc = accounts.get(it.next());
				if (acc.isActive()) result.add(acc.getNumber());
			}
			return result;
		}
		
		@Override
		public Account  getAccount(String number){
			return accounts.get(number);
		}
		
		@Override
		public void transfer(Account a, Account b, double amount) throws InactiveException, IllegalArgumentException, IOException, OverdrawException {
			a.withdraw(amount);
			b.deposit(amount);
		}
	}

	static class MyAccount implements Account {
		private final String owner;
		private final String number;
		private double balance;
		private volatile boolean active;
		
		public MyAccount (String owner, String number) {
			this.owner = owner;
			this.number = number;
			this.balance = 0;
			this.active = true;
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public String getOwner() {
			return owner;
		}

		@Override
		public boolean isActive() {
			return active;
		}
		
		public void setInactive() {
			active = false;
		}

		@Override
		public void deposit(double amount) throws InactiveException {
			synchronized (this) {
				if (!active) throw new InactiveException("unable to withdraw from inactive account " + number);
				if (amount < 0.0) throw new IllegalArgumentException("unable to deposit a amount of " + amount);
				balance += amount;
			}
		}

		@Override
		public void withdraw(double amount) throws OverdrawException, InactiveException {
			synchronized (this) {
				if (balance - amount < 0.0) throw new OverdrawException("unable to withdraw " + amount + " from " + number + " (balance=" + balance + ")");
				if (!active) throw new InactiveException("unable to withdraw from inactive account " + number);
				if (amount < 0.0) throw new IllegalArgumentException("unable to withdraw a amount of " + amount);
				balance -= amount;
			}
		}

		@Override
		public double getBalance() {
			return balance;
		}

	}

}
