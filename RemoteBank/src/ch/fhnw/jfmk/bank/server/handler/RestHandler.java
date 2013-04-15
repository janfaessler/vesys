package ch.fhnw.jfmk.bank.server.handler;

import java.io.IOException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ch.fhnw.jfmk.bank.server.util.CommandHandler;
import ch.fhnw.jfmk.bank.server.util.MyBank;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/bank")
public class RestHandler implements RequestHandler {
	
	private CommandHandler cHandler;
	
	public RestHandler(MyBank b) {
		cHandler = new CommandHandler(b, this);
	}

	@POST
	@Path("/accounts/create")
	@Produces("application/plain")
	public String postCreateAccount(@QueryParam("owner") String owner ) throws IOException {
		return cHandler.handleCommand("createAccount", owner);
	}
	
	@DELETE
	@Path("/accounts/close/{id}")
	@Produces("application/plain")
	public String deleteCloseAccount(@PathParam("id") String id ) throws IOException {
		return cHandler.handleCommand("closeAccount", id);
	}
	
	@GET
	@Path("/accounts/owner/{id}")
	@Produces("application/plain")
	public String getOwner(@PathParam("id") String id) throws IOException {
		return cHandler.handleCommand("getOwner", id);
	}
	
	@GET
	@Path("/accounts")
	@Produces("application/plain")
	public String getAccountNumbers() throws IOException {
		return cHandler.handleCommand("getAccountNumbers", "");
	}
	
	@GET
	@Path("/accounts/status/{id}")
	@Produces("application/plain")
	public String getStatus(@PathParam("id") String id) throws IOException {
		return cHandler.handleCommand("isActive", id);
	}
	
	@PUT
	@Path("accounts/deposit/{id}")
	@Produces("application/plain")
	public String putDeposit(@PathParam("id") String id, @QueryParam("value") String value) throws IOException {
		return cHandler.handleCommand("deposit", id+" "+value);
	}
	
	@PUT
	@Path("accounts/withdraw/{id}")
	@Produces("application/plain")
	public String putWithdraw(@PathParam("id") String id, @QueryParam("value") String value) throws IOException {
		return cHandler.handleCommand("withdraw", id+" "+value);
	}
	
	@GET
	@Path("/accounts/balance/{id}")
	@Produces("application/plain")
	public String getBalance(@PathParam("id") String id) throws IOException {
		return cHandler.handleCommand("getBalance", id);
	}
}
