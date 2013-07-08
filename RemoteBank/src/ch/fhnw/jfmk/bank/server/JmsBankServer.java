package ch.fhnw.jfmk.bank.server;

import java.io.IOException;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;

import ch.fhnw.jfmk.bank.server.handler.JmsRequestHandler;
import ch.fhnw.jfmk.bank.server.jms.JmsBankProxy;
import ch.fhnw.jfmk.bank.server.util.MyBank;

import bank.Bank;
import bank.BankDriver2.UpdateHandler;

public class JmsBankServer {
	
	private Bank bank;
	private Session session;
	private Context context;
	private Connection connection;
	private JmsRequestHandler reqHandler;

	public JmsBankServer(Integer port) {
		Hashtable<String,String> properties = new Hashtable<>();
	    properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
	    properties.put(Context.PROVIDER_URL, "tcp://localhost:"+port);
	    properties.put("queue.BANK", "bank.BANK"); 
	    properties.put("topic.BANK.LISTENER", "bank.BANK.LISTENER");
		try {
			context = new InitialContext(properties);
			ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		    connection = factory.createConnection("", ""); 
			session = connection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			
			Topic topic = (Topic) context.lookup("BANK.LISTENER"); 
			final TopicPublisher publisher = ((TopicSession)session).createPublisher(topic);
			
			UpdateHandler handler = new UpdateHandler() {
				@Override public void accountChanged(String id) throws IOException {
					try {
						TextMessage request = session.createTextMessage(); 
						request.setText(id);
						publisher.send(request);
					} catch (JMSException e) {
					      e.printStackTrace();
					}
				}
			};
			
			bank = new JmsBankProxy(new MyBank(), handler);
			reqHandler =  new JmsRequestHandler(bank);
			
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void start() throws IOException {
		Queue queue;
		try {
			queue = (Queue) context.lookup("BANK");
			QueueReceiver receiver = ((QueueSession)session).createReceiver(queue);
			connection.start();
			System.out.println("JMS server is running...");
			
			while(true){
				TextMessage request = (TextMessage) receiver.receive(); 
				reqHandler.handle(request, session);
			}
			
		} catch (Exception e) { throw new IOException(e); } 		
	}

	public static void main(String[] args) throws IOException {
		JmsBankServer server = new JmsBankServer(Integer.valueOf(args[0]));
		server.start();
	}

}
