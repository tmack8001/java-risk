package javaRisk;
import java.io.IOException;
import java.net.Socket;

/**
 * The Risk class is the class the user runs.
 * It contains code to connect to the server and start the UI.
 */
public class Risk {
	
	public static void main (String args[])
	{
		try {
			//TODO dont use constants
			Socket s = new Socket("localhost", Constants.PORT);

			new ServerProxy(s).start();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
}
