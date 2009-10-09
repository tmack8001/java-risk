package javaRisk;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The ServerProxy is the interface between the Risk game UI
 * and the network.
 */
public class ServerProxy {

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	
	public ServerProxy(Socket socket) {
		this.socket = socket;
		try {
			this.input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	private class Reader extends Thread {

		public void run() {

			//		while(true)
			//		{
			//			
			//		}

		}


	}
	
}