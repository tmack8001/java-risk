package javaRisk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * RiskClient is the client main program for the Java implementation
 * of the classic multiplayer board game, Risk.
 * 
 * @author Trevor Mack
 * @author Dylan Hall
 *
 */
public class RiskClient {
	
	/**
	 * Main program. Displays 3 input dialogs for the user to enter
	 * user name, server address, and game name, then a dialog for
	 * the user to confirm he is ready. Once all connected users are
	 * ready, the game is started and the GUI is launched.
	 * 
	 * @param args	command line arguments (ignored)
	 */
	public static void main(String[] args) {

		// null checks exit the game if the user clicked cancel
		String playerName = JOptionPane.showInputDialog("Enter your name:");
		if (playerName == null)
			System.exit(0);
		
		
		if (playerName.equals("")) 
		{
			int digits = (int) (System.currentTimeMillis() % 100);
			playerName = "Risk Player " + digits;
		}
		
		String hostName = JOptionPane.showInputDialog("Enter the address of the Risk Server:", "localhost");
		if (hostName == null)
			System.exit(0);
		
		String gameName = JOptionPane.showInputDialog("Enter the game name:");
		if (gameName == null) 
			System.exit(0);
		
		
		Socket socket = new Socket();
		try {
			socket.connect( new InetSocketAddress( hostName, Constants.PORT ));
		
			ServerProxy proxy = new ServerProxy( socket );
			proxy.start();

			ClientModel model = new ClientModel();
			
			RiskGUI gui = new RiskGUI();
			gui.setTitle("Risk - " + playerName);
			proxy.setGUI(gui);
			
			proxy.setModel(model);
			
			proxy.joinGame(gameName);
			
			
			while (proxy.gameStatus() == null)
			{
				try {Thread.sleep(100);}  // wait 1/10 second for response
				catch(InterruptedException e) {}
			}
			
			while (proxy.gameStatus().booleanValue())
			{
				gameName = JOptionPane.showInputDialog("That game has already started. Enter a new game name:");
				if (gameName == null) 
					System.exit(0);
				proxy.joinGame(gameName);
				proxy.resetStatus();
				while (proxy.gameStatus() == null)
				{
					try {Thread.sleep(100);}  // wait 1/10 second for response
					catch(InterruptedException e) {}
				}
				
			}
			
			proxy.playerInfo(playerName);
			gui.setListener( proxy );
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

			gui.setVisible(true);
			
		} catch (IOException e) {
			System.err.println("Couldn't connect to server:port, " + Constants.HOST + ":" + Constants.PORT);
			System.exit(1);
		} 
		
	}

}
