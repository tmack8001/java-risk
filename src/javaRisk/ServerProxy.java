package javaRisk;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * The ServerProxy is the interface between the Risk game UI
 * and the network.
 */
public class ServerProxy implements UIListener {

	private RiskGUI gui;
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
	
	public void clicked(int territory) throws IOException {
		
		if (model.isMine(territory)) {
			gui.reset();
			model.setClicked(territory);
			gui.highlight(territory);
			
		} else {
			if (model.getClicked() != -1)
			{
				if (model.isAdjacent(model.getClicked(), territory)) {
					launchAttack(model.getClicked(), territory);
					model.setClicked(-1);
					gui.reset();
				}
			}
		}
		
	}
	
	public void endTurn() throws IOException {
		output.writeByte(Constants.END_TURN);
	}
	
	public void launchAttack(int src, int dest) throws IOException{
		output.writeByte(Constants.ATTACK);
		output.writeInt(src);
		output.writeInt(dest);
	}
	
	public void surrender() throws IOException{
		output.writeByte(Constants.SURRENDER);
		socket.close();
	}
	
	public void turnIndicator(int player)
	{
		gui.showPlayerTurn(player);
		gui.showGamePlayable(model.isMe(player));
	}
	
	public void showAttack(int src, int dest, int a_roll, int d_roll)
	{
		gui.showAttack(src, dest);
		gui.showAttackRoll(a_roll);
		gui.showDefenseRoll(d_roll);
	}
	
	public void update(int index, int owner, int size){
		model.updateTerritory(index, owner, size);
		gui.updateTerritory(index, model.getPlayerColor(owner), size);
	}
	
	public void setGUI(RiskGUI gui)
	{
		this.gui = gui;
	}
	
	public void setModel(ClientModel model)
	{
		this.model = model;
	}
	
	public ClientModel getModel() {
		return model;
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
					
				//	System.out.println("Received: " + curr);
					
					switch(curr)
					{
					case Constants.GAME_STARTING:
						int numPlayers = input.readInt();
						
						model = new ClientModel(numPlayers);
						
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
						break;
						
					case Constants.PLAYER_INFO:
						int player = input.readInt();
						int rgb = input.readInt();
						String name = input.readUTF();
						
						model.fillPlayerData(player, new Color(rgb), name);
						break;
						
					case Constants.WHO_AM_I:
						int me = input.readInt();
						model.setMe(me);
						
					default:
						break;
					}

				}
			} catch (EOFException e) 
			{
				
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				try { socket.close(); } catch(Exception e){}
			}

		}


	}
	
}