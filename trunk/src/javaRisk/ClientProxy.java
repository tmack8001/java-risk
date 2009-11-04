package javaRisk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * The ClientProxy is the interface between the Risk game back end
 * and the network.
 * 
 * @author Trevor Mack
 * @author Dylan Hall
 */
public class ClientProxy {

	/**
	 * Associated ServerModel.
	 */
	private ServerModel model;
	
	/**
	 * Player that this ClientProxy represents.
	 */
	private Player me;

	/**
	 * Socket connection to the client.
	 */
	private Socket socket;
	
	/**
	 * InputStream to read data from client.
	 */
	private DataInputStream input;
	
	/**
	 * OutputStream to send data to client.
	 */
	private DataOutputStream output;
	
<<<<<<< .mine
	/**
	 * Create a ClientProxy from the given socket.
	 * @param socket - connection to the client
	 */
=======
	/**
	 * The constructor for a ClientProxy. Initially sets up the connection.
	 * 
	 * @param socket	the socket connection that is associated with the user.
	 */
>>>>>>> .r85
	public ClientProxy(Socket socket) {
		this.socket = socket;
		try {
			this.input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		me = new Player(); // temporary
	}
	
	/**
	 * Set the player who is represented by this ClientProxy.
	 * @param player - the player
	 */
	public void setPlayer(Player player) {
		me = player;
	}
	
	/**
	 * Set the associated ServerModel.
	 * @param model - ServerModel
	 */
	public void setModel(ServerModel model) {
		this.model = model;
		this.model.setPlayer(me.getIndex(), me);
	}
	
	/**
	 * Launch an attack from territory src to territory dest.
	 * @param src - attacking territory
	 * @param dest - defending territory
	 */
	public void attackLaunched(int src, int dest) throws IOException {
		model.attack(src, dest);
		if(model.getWinner() != null) {
			model.winnerFound(model.getWinner());
		}
	}
	
	/**
	 * Tell the client a winner was found.
	 * @param player - the winner.
	 */
	public void winnerFound(Player player) throws IOException {
		output.writeByte(Constants.GAME_FINISHED);
		output.writeInt(player.getIndex());
		output.flush();
	}
	
	/**
	 * Send the info about a player. (index, color, name)
	 * @param player - the player to send info about.
	 */
	public void playerInfo(Player player) throws IOException {
		output.writeByte(Constants.PLAYER_INFO);
		output.writeInt(player.getIndex());
		output.writeInt(player.getColor().getRGB());
		output.writeUTF(player.getName());
		output.flush();
	}
	
	/**
	 * Attaches the attack results onto the socket to send over to the client.
	 * 
	 * @param attackResult	[0] - attacking territory index
	 * 						[1] - defending territory index
	 * 						[2] - attack roll
	 * 						[3] - defend roll
	 */
	public void attackMade(int[] attackResult) throws IOException {
		output.writeByte(Constants.ATTACK_MADE);
		output.writeInt(attackResult[0]);
		output.writeInt(attackResult[1]);
		output.writeInt(attackResult[2]);
		output.writeInt(attackResult[3]);
		output.flush();
	}
	
	/**
	 * Send the user the state of a territory.
	 * @param territoryStatus [0] - territory index
	 * 						  [1] - owner index
	 * 		  				  [2] - army size
	 */
	public void updateTerritory(int[] territoryStatus) throws IOException {
		output.writeByte(Constants.TERRITORY_STATUS);
		output.writeInt(territoryStatus[0]);
		output.writeInt(territoryStatus[1]);
		output.writeInt(territoryStatus[2]);
		output.flush();
	}
	
	/**
	 * Send the user the index of the current player's turn.
	 * @param currentTurn - the index of the player whose turn it is.
	 */
	public void sendTurn(int currentTurn) throws IOException {
		output.writeByte(Constants.TURN_IND);
		output.writeInt(currentTurn);
		output.flush();
	}
	
	/**
	 * Tell the user the game is starting.
	 */
	public void gameStarting() throws IOException {
		output.writeByte(Constants.GAME_STARTING);
		output.flush();
	}
	
	/**
	 * Send the user his player index.
	 */
	public void whoAmI() throws IOException {
		output.writeByte(Constants.WHO_AM_I);
		output.writeInt(this.me.getIndex());
		output.flush();
	}
	
	/**
	 * The user ended his turn.
	 */
	public void signalEndTurn() {
		model.incrementMove();
	}
	
	/**
	 * The user surrendered.
	 */
	public void surrender() {
		if(model != null)
			model.removeListener(this);
	}
	
	/**
	 * The client signaled he is ready to play.
	 */
	public void signalReady() {
		model.ready(me);
	}
	
	/**
	 * The client tried to join a game with the given name.
	 * @param name - the game name
	 */
	public void joinGame(String name) throws IOException
	{
		ServerModel model = SessionMap.getSession(name);
		//check if game already started
		if(model.isGameStarted()) {
			this.gameAlreadyStarted(true);
		}else {
			this.gameAlreadyStarted(false);
			model.addListener(this);
			this.setModel(model);
		}
	}
	
	/**
	 * Tell the client whether or not the game he tried to connect to
	 * has already started.
	 * @param b - true if the game was already started.
	 */
	private void gameAlreadyStarted(boolean b) throws IOException {
		output.writeByte(Constants.GAME_ALREADY_STARTED);
		output.writeBoolean(b);
		output.flush();
	}

	/**
	 * Start the client proxy reading data from the client.
	 */
	public void start()
	{
		new Reader().start();
	}
	
	/**
	 * Private Reader thread used to read data from the client.
	 */
	private class Reader extends Thread {

		public void run() {
			try {
				while(true)	{
					byte curr = input.readByte();
					
					switch(curr){
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
					case Constants.GAME_TO_JOIN:
						String gameName = input.readUTF();
						joinGame(gameName);
						break;
					case Constants.PLAYER_INFO:
						Player player = ClientProxy.this.me;
						String playerName = input.readUTF();
						player.setName(playerName);
						break;	
					default:
						break;
					}
				}
			} catch(EOFException exc) {
			} catch(IOException exc) {
				System.err.println("ClientProxy.Reader.run(): I/O error");
				exc.printStackTrace();
			} finally { 
				try {
					surrender();
					socket.close();
				}catch(IOException exc) {
				}
			}
		}
	}
}