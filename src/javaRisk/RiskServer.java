package javaRisk;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The RiskServer class contains code to accept incoming connections
 * and delegate them.
 * 
 * @author Dylan Hall
 * @author Trevor Mack
 */
public class RiskServer {

	/**
	 * Flag to determining if new clients should be accepted.
	 */
	private static boolean acceptingClients = true;
	
	/**
	 * Main method. Creates and starts a client proxy for incoming connections.
	 * @param args Command-line args (ignored)
	 */
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(Constants.PORT);
			System.out.println("Server waiting on port " + ss.getLocalPort());
			
			while (acceptingClients) {
				Socket socket = ss.accept();
				System.out.println("Connect: " + socket.getRemoteSocketAddress().toString());
				
				new ClientProxy( socket ).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops the server from accepting new clients.
	 */
	public static void stopServer() {
		acceptingClients = false;
	}
	
}
