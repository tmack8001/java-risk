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
 * 
 * @author Dylan Hall
 * @author Trevor Mack
 */
public class ServerProxy implements UIListener {

	private RiskGUI gui;
	private ClientModel model;
	private List<Player> players = new ArrayList<Player>();
	
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	
	private boolean gameStarted = false;
	
	/**
	 * Create a new ServerProxy based on the given socket.
	 * @param socket - the socket connected to the Risk server
	 */
	public ServerProxy(Socket socket) {
		this.socket = socket;
		try {
			this.input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called when the user clicks on any territory.
	 * @param territory - the index of the clicked territory
	 */
	public void clicked(int territory) throws IOException {
		
		if (model.isMine(territory)) {
			gui.reset();
			model.setClicked(territory);
			gui.highlight(territory);
			
		} else {
			if (model.getClicked() != -1) 
			{
				// user has already clicked one of his own territories
				
				if (model.isAdjacent(model.getClicked(), territory)) {
					launchAttack(model.getClicked(), territory);
					model.setClicked(-1);
					gui.reset();
				}
			}
		}
		
	}
	
	/**
	 * User clicked "End Turn" button.
	 */
	public void endTurn() throws IOException {
		System.out.println("I'm Done");
		output.writeByte(Constants.END_TURN);
		gui.reset();
	}
	
	/**
	 * User launched an attack from one territory to another.
	 * @param src - the index of the attacking territory
	 * @param dest - the index of the defending territory
	 */
	public void launchAttack(int src, int dest) throws IOException{
		System.out.println(src + " attacks " + dest);
		output.writeByte(Constants.ATTACK);
		output.writeInt(src);
		output.writeInt(dest);
	}
	
	/**
	 * User exited the program, causing a surrender.
	 */
	public void surrender() throws IOException{
		System.out.println("Surrender Me");
		output.writeByte(Constants.SURRENDER);
		socket.close();
	}
	
	/**
	 * Received an indicator of a player's turn.
	 * @param player - the index of the player whose turn it is.
	 */
	public void turnIndicator(int player)
	{
		gui.showPlayerTurn(player);
		gui.showGamePlayable(model.isMe(player));
	}
	
	/**
	 * Display an attack on the GUI.
	 * @param src - attacking territory index
	 * @param dest - defending territory index
	 * @param a_roll - attack roll
	 * @param d_roll - defense roll
	 */
	public void showAttack(int src, int dest, int a_roll, int d_roll)
	{
		gui.showAttack(src, dest);
		gui.showAttackRoll(a_roll);
		gui.showDefenseRoll(d_roll);
	}
	
	/**
	 * Update a territory. (Either game initialization or after attack)
	 * @param index - territory to update
	 * @param owner - new owner index of the territory
	 * @param size - size of the army on this territory
	 */
	public void update(int index, int owner, int size){
		model.updateTerritory(index, owner, size);
		gui.updateTerritory(index, model.getPlayerColor(owner), size);
	}


	/**
	 * Used by the RiskClient to determine when to display the GUI.
	 * @return true if the server has sent the GAME_STARTED message
	 */
	public boolean gameIsStarted() {
		return gameStarted;
	}

	/**
	 * Request the Color representing a player by index.
	 * @return the given player's Color
	 */
	public Color requestPlayerColor(int player) {
		return model.getPlayerColor(player);
	}
	
	/**
	 * Set the associated GUI object.
	 * @param gui - the GUI to set
	 */
	public void setGUI(RiskGUI gui)
	{
		this.gui = gui;
	}
	
	/**
	 * Send the name of the game to join to the server.
	 * @param gameName - the name of the game to join
	 */
	public void joinGame(String gameName) throws IOException
	{
		System.out.println("Join game " + gameName);
		output.writeByte(Constants.GAME_TO_JOIN);
		output.writeUTF(gameName);
		output.flush();
	}
	
	/**
	 * Send the name of the player joining a game. 
	 * @param playerName - the player's username
	 */
	public void playerInfo(String playerName) throws IOException
	{
		System.out.println("Send Player Name");
		output.writeByte(Constants.PLAYER_INFO);
		output.writeUTF(playerName);
		output.flush();
	}
	
	/**
	 * Received information about a player from the server.
	 * @param player - the player's index
	 * @param color - the player's Color
	 * @param name - the player's Username
	 */
	public void receivedPlayerInfo(int player, Color color, String name) {
		players.add(new Player(player, name, color));
	}
	
	/**
	 * Signal to the server that this user is ready to play.
	 */
	public void ready() throws IOException
	{
		System.out.println("Ready Me");
		output.writeByte(Constants.READY);
		output.flush();
	}
	
	/**
	 * Set the associated model.
	 * @param model - the ClientModel to set.
	 */
	public void setModel(ClientModel model)
	{
		this.model = model;
	}
	
	/**
	 * Get a list of the names of players in the current game.
	 * @return array of player names
	 */
	public String[] getPlayerNames()
	{
		return model.getPlayerNames();
	}
	
	/**
	 * Start this ServerProxy reading data from the server.
	 */
	public void start() {
		new Reader().start();
	}
	
	/**
	 * Private Reader class used to read data from the Socket
	 * connected to the server.
	 */
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
						receivedPlayerInfo(player, new Color(rgb), name);
						break;
						
					case Constants.WHO_AM_I:
						System.out.println("Who am I?");
						int me = input.readInt();
						model.setPlayers(players);
						model.setMe(me);
						gui.setNames(model.getPlayerNames());
						break;
						
					case Constants.GAME_FINISHED:
						System.out.println("Game over");
						int winner = input.readInt();
						if (model.isMe(winner))
						{
							gui.youWin();
						} else
						{
							gui.youLose();
						}
						break;
						
					case Constants.GAME_ALREADY_STARTED:
						System.out.println("game already started");
						gui.gameAlreadyStarted();
						socket.close();
						System.exit(0);
						break;
						
					default:
						break;
					}

				}
			} catch (EOFException e) 
			{	
			} catch (IOException e)
			{
				e.printStackTrace();
			}

		}


	}

}