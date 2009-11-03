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
	 * @param args	command line arguments (ignored)
	 */
	public static void main(String[] args) {

		String playerName = JOptionPane.showInputDialog("Enter your name:");
		if (playerName == null)System.exit(0);
		
		String hostName = JOptionPane.showInputDialog("Enter the address of the Risk Server:", "localhost");
		if (hostName == null) System.exit(0);
		
		String gameName = JOptionPane.showInputDialog("Enter the room name:");
		if (gameName == null) System.exit(0);
		
		
		Socket socket = new Socket();
		try {
			socket.connect( new InetSocketAddress( hostName, Constants.PORT ));
		
			ServerProxy proxy = new ServerProxy( socket );
			proxy.start();

			ClientModel model = new ClientModel();
			
			RiskGUI gui = new RiskGUI();
			
			proxy.setGUI(gui);
			
			proxy.setModel(model);
			
			proxy.joinGame(gameName);
			proxy.playerInfo(playerName);
			
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
			proxy.ready();
			
			while (!proxy.gameIsStarted()) {
				try {Thread.sleep(1000);}  // wait 1 second for players
				catch(InterruptedException e) {}
			} 
			
			gui.setListener( proxy );

			gui.setVisible(true);
			
		} catch (IOException e) {
			System.err.println("Couldn't connect to server:port, " + Constants.HOST + ":" + Constants.PORT);
			System.exit(1);
		}
		
	}

}
