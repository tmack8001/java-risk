package javaRisk;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The RiskServer class contains code to accept incoming connections
 * and delegate them.
 */
public class RiskServer {

	/**
	 * Flag to determining if new clients should be accepted.
	 */
	private static boolean acceptingClients = true;
	
	/**
	 * Main method. Accepts
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(Constants.PORT);
			System.out.println("Server waiting on port " + ss.getLocalPort());
			while (acceptingClients) {
				Socket sock = ss.accept();
				System.out.println("Connect: " + sock.getRemoteSocketAddress().toString());
				new ClientProxy(sock).start();
				
			}
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
		
	}

	/**
	 * Stops the server from accepting new clients.
	 */
	void stopServer() {
		// no-modifier so only this package can call it
		acceptingClients = false;
	}
	
}
