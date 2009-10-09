package javaRisk;

import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * The Risk class is the class the user runs.
 * It contains code to connect to the server and start the UI.
 */
public class Risk {
	
	public static void main (String args[])
	{
		try {
			String host = JOptionPane.showInputDialog("Connect to server: ");
			Socket s = new Socket(host, Constants.PORT);

			ServerProxy server = new ServerProxy(s);
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
}
