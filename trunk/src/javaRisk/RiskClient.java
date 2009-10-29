/**
 * RiskClient.java
 */
package javaRisk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Class RiskClient is the client main program for the java implementation
 * of the classic multi-player board game, Risk.
 * 
 * @author Trevor Mack
 *
 */
public class RiskClient {

	/**
	 * Main program.
	 * 
	 * @param args	command line arguments
	 */
	public static void main(String[] args) {
		if( args.length > 1 ) usage();
		
		String playerName = "";
		if( args.length == 1) {
			playerName = args[0];
		}
		
		Socket socket = new Socket();
		try {
			socket.connect( new InetSocketAddress( Constants.HOST, Constants.PORT ));
		} catch (IOException e) {
			System.err.println("Couldn't connect to server:port, " + Constants.HOST + ":" + Constants.PORT);
			System.exit(1);
		}
		
		RiskGUI gui = new RiskGUI( ); 
		ServerProxy proxy = new ServerProxy( socket );
		//proxy.addPlayer(playerName);
		
		gui.setListener( proxy );
		proxy.start();
	}
	
	/**
	 * Prints a usage message and exits program.
	 */
	private static void usage() {
		System.err.println("Usage: java RiskClient [playerName]");
		System.exit(1);
	}

}