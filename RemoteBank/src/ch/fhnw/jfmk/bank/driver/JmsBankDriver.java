package ch.fhnw.jfmk.bank.driver;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;

import ch.fhnw.jfmk.bank.driver.util.RemoteBank;
import ch.fhnw.jfmk.bank.driver.util.RemoteDriver;

import bank.Bank;
import bank.BankDriver2;

public class JmsBankDriver implements BankDriver2, RemoteDriver {
	private Bank bank;
	private QueueSession session;
	private Connection connection;
	private transient QueueReceiver receiver;
	private transient QueueSender sender;
	private TemporaryQueue responseQueue;
	private List<UpdateHandler> listeners = new CopyOnWriteArrayList<>();
	
	@Override
	public void connect(String[] args) throws IOException {
		bank = new RemoteBank(this);
		
		
		Hashtable<String,String> properties = new Hashtable<>();
	    properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
	    properties.put(Context.PROVIDER_URL, args[0]);
	    properties.put("queue.BANK", "bank.BANK"); 
	    properties.put("topic.BANK.LISTENER", "bank.BANK.LISTENER"); 
	    try {
	    	Context context = new InitialContext(properties); 
	    	ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
			connection = factory.createConnection("","");
			connection.start();
			
			TopicSession topicSession = ((TopicConnection)connection).createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		    Topic topic = (Topic) context.lookup("BANK.LISTENER");
		    TopicSubscriber ts = topicSession.createSubscriber(topic);
		    ts.setMessageListener(new MessageListener() {
		    	@Override public void onMessage(Message msg) {
		    		TextMessage m = (TextMessage)msg;
		    		try {
		    			String id = m.getText();
		    			for(UpdateHandler h : listeners) try { h.accountChanged(id); }catch(IOException e){}
		    	      
		    		} catch (JMSException e1) { e1.printStackTrace(); }
		    	}
		    });
		    session = ((QueueConnection)connection).createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		    Queue queue = (Queue) context.lookup("BANK");
		    
		    responseQueue = session.createTemporaryQueue(); 
		    sender = session.createSender(queue); 
		    sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		    
		    receiver = session.createReceiver(responseQueue); 

		} catch (Exception e) { e.printStackTrace(); }
	}

	@Override
	public void disconnect() throws IOException {
		bank = null;
		System.out.println("disconnected...");
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	@Override
	public void registerUpdateHandler(UpdateHandler handler) throws IOException {
		listeners.add(handler);
	}

	@Override
	public String sendCommand(String command) throws IOException {
		return sendCommand(command, "");
	}

	@Override
	public String sendCommand(String command, String param) throws IOException {
		System.out.println("Client send: '"+command+":"+param+"'");
		String response = "";
		try {
			TextMessage request = session.createTextMessage();
			request.setText(command + ":" + param);		
			request.setJMSReplyTo(responseQueue);
			sender.send(request);
			
			TextMessage msg = (TextMessage) receiver.receive();
			response = msg.getText();
			System.out.println("Client receive: '"+response+"'");

			
		} catch (JMSException e) {
			throw new IOException(e);
		}
		
		return (response.length() > 0 ? response : null );
	}

}
