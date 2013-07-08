package ch.fhnw.jfmk.bank.server.handler;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import bank.Bank;

import ch.fhnw.jfmk.bank.server.util.CommandHandler;
import ch.fhnw.jfmk.bank.server.util.Request;


public class JmsRequestHandler implements RequestHandler {
	
	private CommandHandler cHandler;

	public JmsRequestHandler(Bank bank) {
		cHandler = new CommandHandler(bank, this);
	}


	public void handle(TextMessage message, Session session) throws IOException {
		String[] temp;
		try {
			temp = message.getText().split(":");
			Request req = new Request(temp[0],temp.length > 1 ? temp[1] : "");
			String result = cHandler.handleCommand(req.getCommand(), req.getParam());
			
			TextMessage response = session.createTextMessage();
			response.setText(result);
			((QueueSession)session).createSender((Queue) message.getJMSReplyTo()).send(response);

		} catch (JMSException e) { throw new IOException(e); }
	}
}
