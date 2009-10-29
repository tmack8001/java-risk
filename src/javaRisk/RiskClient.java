/**
 * RiskClient.java
 */
package javaRisk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

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
		if( args.length < 2) usage();
		
		String hostName = args[0];
		String gameName = args[1];
		
		String playerName = "";
		if( args.length == 3) {
			playerName = args[2];
		}
		
		Socket socket = new Socket();
		try {
			socket.connect( new InetSocketAddress( Constants.HOST, Constants.PORT ));
		

			ServerProxy proxy = new ServerProxy( socket );
			proxy.start();

			proxy.joinGame(gameName);
			
			int result = -1;
			
			while (result != JOptionPane.YES_OPTION)
			{
				result = JOptionPane.showConfirmDialog(null, "Are you ready?");
				if (result == JOptionPane.CANCEL_OPTION)
				{
					socket.shutdownInput();
					socket.close();
					System.exit(0);
				}
			}
			proxy.ready(); //TODO fix this

			while (!proxy.gameIsStarted()) {} // wait for game to be ready


			RiskGUI gui = new RiskGUI( proxy.getPlayerNames()); 

			//proxy.addPlayer(playerName);

			gui.setListener( proxy );

		} catch (IOException e) {
			System.err.println("Couldn't connect to server:port, " + Constants.HOST + ":" + Constants.PORT);
			System.exit(1);
		}
		
	}
	
	/**
	 * Prints a usage message and exits program.
	 */
	private static void usage() {
		System.err.println("Usage: java RiskClient <host> <gameName> [playerName]");
		System.exit(1);
	}

}
