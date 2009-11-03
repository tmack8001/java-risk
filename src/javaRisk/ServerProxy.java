package javaRisk;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The ServerProxy is the interface between the Risk game UI
 * and the network.
 */
public class ServerProxy implements UIListener {

	private RiskGUI gui;
	private ClientModel model;
	private List<Player> players = new ArrayList<Player>();
	
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	
	private boolean gameStarted = false;
	
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
		System.out.println("I'm Done");
		output.writeByte(Constants.END_TURN);
		gui.reset();
	}
	
	public void launchAttack(int src, int dest) throws IOException{
		System.out.println(src + " attackes " + dest);
		output.writeByte(Constants.ATTACK);
		output.writeInt(src);
		output.writeInt(dest);
	}
	
	public void surrender() throws IOException{
		System.out.println("Surrender Me");
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
	
	public void joinGame(String gameName) throws IOException
	{
		System.out.println("Join game " + gameName);
		output.writeByte(Constants.GAME_TO_JOIN);
		output.writeUTF(gameName);
		output.flush();
	}
	
	public void playerInfo(String playerName) throws IOException
	{
		System.out.println("Send Player Name");
		output.writeByte(Constants.PLAYER_INFO);
		output.writeUTF(playerName);
		output.flush();
	}
	
	public void playerInfo(int player, Color color, String name) {
		players.add(new Player(player, name, color));
	}
	
	public void ready() throws IOException
	{
		System.out.println("Ready Me");
		output.writeByte(Constants.READY);
		output.flush();
	}
	
	public void setModel(ClientModel model)
	{
		this.model = model;
	}
	
	public String[] getPlayerNames()
	{
		return model.getPlayerNames();
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
				while(socket.isConnected())
				{
					byte curr = input.readByte();
					
					switch(curr)
					{
					case Constants.GAME_STARTING:
						System.out.println("Game Starting");
						model.setPlayers(players);
						gameStarted = true;
						
						break;
					case Constants.TURN_IND:
						System.out.println("Turn Ind");
						int player_num = input.readInt();
						turnIndicator(player_num);
						break;
						
					case Constants.ATTACK_MADE:
						System.out.println("Attack Made");
						int src = input.readInt();
						int dest = input.readInt();
						int a_roll = input.readInt();
						int d_roll = input.readInt();
						
						showAttack(src,dest, a_roll, d_roll);
						break;
						
					case Constants.TERRITORY_STATUS:
						System.out.println("Territory Status");
						int index = input.readInt();
						int owner = input.readInt();
						int size = input.readInt();
						
						update(index, owner, size);
						break;
						
					case Constants.PLAYER_INFO:
						System.out.println("Player Info");
						int player = input.readInt();
						int rgb = input.readInt();
						String name = input.readUTF();
						playerInfo(player, new Color(rgb), name);
						break;
						
					case Constants.WHO_AM_I:
						System.out.println("Who am I?");
						int me = input.readInt();
						model.setPlayers(players);
						model.setMe(me);
						gui.setNames(model.getPlayerNames());
						
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

	public boolean gameIsStarted() {
		return gameStarted;
	}

	public Color requestPlayerColor(int player) {
		return model.getPlayerColor(player);
	}
}