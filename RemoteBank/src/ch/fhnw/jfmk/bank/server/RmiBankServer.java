package ch.fhnw.jfmk.bank.server;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import ch.fhnw.jfmk.bank.server.rmi.RmiBankProxy;

public class RmiBankServer {
	
	private final int port;
	
	public RmiBankServer(int p) {
		port = p;
	}
	
	public void start() throws IOException {
		
		try {
			LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			System.out.println(">> registry could not be exported"); 
			System.out.println(">> probably another registry already runs on 1099");
		}
		
		Naming.rebind("rmi://localhost:"+port+"/bank", new RmiBankProxy());
		
		System.out.print("server started on " + port);
	}
	
	public static void main(String[] args) throws IOException {
		RmiBankServer server = new RmiBankServer(Integer.valueOf(args[0]));
		server.start();
	}
}
