package server.util;

import java.io.IOException;

public interface RequestHandler {
	public void stop() throws IOException;
	public void sendCommand(String command, String param) throws IOException;
}
