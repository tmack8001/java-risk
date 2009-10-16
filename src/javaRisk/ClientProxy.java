package javaRisk;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The ClientProxy is the interface between the Risk game back end
 * and the network.
 */
public class ClientProxy {

	private ServerModel model;
	private Player me;

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	
	public ClientProxy(Socket socket) {
		this.socket = socket;
		try {
			this.input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setPlayer(Player player) {
		me = player;
	}
	
	public void setModel(ServerModel model) {
		this.model = model;
	}
	
	public void attackLaunched(int src, int dest) {
		model.attack(src, dest);
	}
	
	public void signalEndTurn() {
		model.endTurn(me);
	}
	
	public void surrender() {
		model.surrender(me);
	}
	
	public void signalReady() {
		model.ready(me);
	}
	
	public void start()
	{
		new Reader().start();
	}
	
	private class Reader extends Thread {

		public void run() {
			try
			{
				while(true)
				{

					byte curr = input.readByte();

					switch(curr)
					{
					case Constants.ATTACK:
						int src = input.readInt();
						int dest = input.readInt();
						
						attackLaunched(src, dest);
						break;
					case Constants.END_TURN:
						signalEndTurn();
						break;
					case Constants.READY:
						signalReady();
						break;
					case Constants.SURRENDER:
						surrender();	
						break;
					default:
						break;
					}

				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}


	}
	
}

