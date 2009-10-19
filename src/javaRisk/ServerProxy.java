package javaRisk;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The ServerProxy is the interface between the Risk game UI
 * and the network.
 */
public class ServerProxy implements UIListener {

	private GUI gui;
	private ClientModel model;
	
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
	
	public void endTurn() {
		output.writeByte(Constants.END_TURN);
	}
	
	public void startAttack(int territory) {
		// check with local model to see what to do?
	}
	
	public void launchAttack(int src, int dest) {
		output.writeByte(Constants.ATTACK);
		output.writeInt(src);
		output.writeInt(dest);
	}
	
	public void surrender() {
		output.writeByte(Constants.SURRENDER);
	}
	
	public void turnIndicator(int player)
	{
		gui.showPlayerTurn(player);
		boolean b = (player == me);
		gui.showGamePlayable(b);
	}
	
	public void showAttack(int src, int dest, int a_roll, int d_roll)
	{
		gui.showAttack(src, dest);
		gui.showAttackRoll(a_roll);
		gui.showDefenseRoll(d_roll);
	}
	
	public void update(int index, int owner, int size){
		model.updateTerritory(index, owner, size);
		// model should call update territory on gui
	}
	
	public void start() {
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
					case Constants.GAME_STARTING:
					
						break;
					case Constants.TURN_IND:
						int player_num = input.readInt();
						
						turnIndicator(player_num);
						
						break;
					case Constants.ATTACK_MADE:
						int src = input.readInt();
						int dest = input.readInt();
						int a_roll = input.readInt();
						int d_roll = input.readInt();
						
						showAttack(src,dest, a_roll, d_roll);
						break;
						
					case Constants.TERRITORY_STATUS:
						int index = input.readInt();
						int owner = input.readInt();
						int size = input.readInt();
						
						update(index, owner, size);
						
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