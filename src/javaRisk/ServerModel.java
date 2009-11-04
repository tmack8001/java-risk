/**
 * ServerModel.java
 */
package javaRisk;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Representation of a general Risk Game for server side execution.
 * 
 * @author Trevor Mack
 * @author Dylan Hall
 * 
 */
public class ServerModel {

	private List<Territory> territories;
	private List<Player> players;
	
	private HashMap<ClientProxy, Player> listeners;
	private Random rand = new Random();
	
	private int currentMove;
	private int rows, cols;
	private int readyCount;
	private boolean gameStarted = false;
	
	/*TODO: figure out an algorithm for number of armies per player*/
	private static final int NUM_ARMY = 40;
	
	/**
	 * Default constructor
	 */
	public ServerModel() {
		this(new ArrayList<Player>(), Constants.ROW_SIZE, Constants.COL_SIZE);
	}
	
	/**
	 * The constructor for the Server Model.
	 * 
	 * @param players	list of players for this game
	 * @param rows		number of rows in the board
	 * @param cols		number of columns in the board
	 */
	public ServerModel(List<Player> players, int rows, int cols) {
		territories = new ArrayList<Territory>();
		listeners = new HashMap<ClientProxy, Player>();
		this.players = players;
		this.rows = rows;
		this.cols = cols;
		readyCount = 0;
	}
	
	/**
	 * Add a ClientProxy to the list of clients for this instance with a default name.
	 * 
	 * @param client	ClientProxy to added to list of players
	 */
	public synchronized void addListener(ClientProxy client) {
		Player player = new Player(players.size(), "Player " + players.size(), 
				Color.getHSBColor(rand.nextFloat(), 1.0f, 1.0f));
		client.setPlayer(player);
		players.add(player);
		listeners.put(client, player);
	}
	
	/**
	 * Add a ClientProxy to the list of clients for this instance with a default name.
	 * 
	 * @param client
	 * @param name
	 */
	public synchronized void addListener(ClientProxy client, String name) {
		Player player = new Player(players.size(), name, 
				Color.getHSBColor(rand.nextFloat(), 1.0f, 1.0f));
		players.add(player);
		listeners.put(client, player);
	}
	
	/**
	 * Removes a client from the list of listeners.
	 * 
	 * @param client	the ClientProxy to remove
	 */
	public synchronized void removeListener(ClientProxy client) {
		Player player = listeners.remove(client);
		players.remove(player);
		player.surrender();
	}
	
	/**
	 * Initially used to setup a game board. Loops over all the rows and columns
	 * to create initialized territories.
	 * 
	 * Global variable territories is changed to consist of the newly generated
	 * territories.
	 */
	public void initializeBoard() {
		System.out.println("setup board");
		int index = 0;
		for(int row=0; row<rows; row++) {
			for(int col=0; col<cols; col++) {
				territories.add(new Territory(index++, row, col));
			}
		}
		currentMove = 0;
	}
	
	/**
	 * Once the initial board is setup the armies that belong to the list current list
	 * of players can be placed on random territories. This method guarantees that each player
	 * received approximately the same number of territories, with the extras going to
	 * players starting with the first player (index == 0).
	 */
	public void placeArmies() {
		System.out.println("placearmies");
		int terrPerPlayer = (int) (territories.size() / players.size()) + 1;
		for(int i=0; i<territories.size(); i++) {
			boolean assigned = false;
			while(!assigned) {
				Player player = players.get( rand.nextInt(players.size()) );
				if( player.getTerritories().size() < terrPerPlayer ) {
					int armySize = (rand.nextInt(7) + 1);
					Territory t = territories.get(i);
					t.setArmy(new Army(player, armySize));
					updateTerritory(t.getIndex(), t);
					assigned = true;
				}
			}
		}
	}
	
	/**
	 * This is the method that contains the attacking logic for the Risk Model.
	 * The given attacking territory will roll for each of it's attacking members
	 * a dice and that final roll value (sum of all rolls) will be compared to
	 * the defenders value to see who wins. If the attacker's value is great than
	 * the defender's value then the win goes to the attacker.
	 * 
	 * If the attacker wins then N-1 army members will advance on to the defending
	 * territory.
	 * 
	 * If the defender wins then the attacking territory loses its attacking army (N-1)
	 * and the attacking territory is left with 1 army member on it.
	 * 
	 * @param attacker	the index assigned to the attacking territory
	 * @param defender	the index assigned to the defending territory
	 */
	public synchronized void attack(int attacker, int defender) {
		System.out.println("src: " + attacker + " dest: " + defender);
		Territory attacking = territories.get(attacker);
		Territory defending = territories.get(defender);
		
		/* [1] - attacking Index
		 * [2] - defending Index
		 * [3] - attack Roll
		 * [4] - defend Roll
		 */
		int[] attackResults = attacking.attack(defending);
		
		if( attackResults != null ) {
			Iterator<ClientProxy> iterator = listeners.keySet().iterator();
			while(iterator.hasNext()) {
				try {
					iterator.next().attackMade(attackResults);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			updateTerritory(attackResults[0], attacking);
			updateTerritory(attackResults[1], defending);
		}
	}
	
	/**
	 * This method, updateTerritory, will go into the model and update a territory
	 * with a new territory reference. This will also send a network message using
	 * the clientProxy class out communicate with every listener on this model
	 * instance about this change.
	 * 
	 * @param index			the index number associated with the territory
	 * @param newTerritory	the new territory reference
	 */
	public synchronized void updateTerritory(int index, Territory newTerritory) {
		territories.set(index, newTerritory);
		
		int[] territoryStatus = new int[3];
		territoryStatus[0] = newTerritory.getIndex();
		territoryStatus[1] = newTerritory.getOwner().getIndex();
		territoryStatus[2] = newTerritory.getArmy().getCount();
		
		Iterator<ClientProxy> iterator = listeners.keySet().iterator();
		while(iterator.hasNext()) {
			try {
				iterator.next().updateTerritory(territoryStatus);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Iterates across the model sending player information (index, name, color) to
	 * all of the connected players using the ClientProxy class.
	 */
	public void playerInfo() {
		Iterator<ClientProxy> iterator = listeners.keySet().iterator();
		while(iterator.hasNext()) {
			try {
				ClientProxy proxy = iterator.next();
				for (Player player : players) {
					proxy.playerInfo(player);
				}
				proxy.whoAmI();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Iterates across all the linked clients to send network message that the game
	 * is ready to begin.
	 * 
	 * @return	true if the game started, else false since the game could not start
	 */
	public boolean gameStarting() {
		if(readyCount == players.size()) {
			gameStarted = true;
			initializeBoard();
			placeArmies();
			
			Iterator<ClientProxy> iterator = listeners.keySet().iterator();
			while(iterator.hasNext()) {
				try {
					iterator.next().gameStarting();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			currentMove = -1;
			incrementMove();
			return true;
		}
		return false;
	}
	
	/**
	 * Calculates to see if there is a winner if there is a winner of the game then
	 * return that player's information.
	 * 
	 * @return	the player object of the winner, else null
	 */
	public Player getWinner() {
		Iterator<Player> iterator = players.iterator();
		int numAlive = 0;
		Player temp = null;
		Player winner = null;
		while( iterator.hasNext() ) {
			temp = iterator.next();
			if(temp.getAlive()) {
				winner = temp;
				if(++numAlive > 1)
					return null;
			}
		}
		return winner;
	}
	
	/**
	 * This method tells the Model that a given user is "ready".
	 * 
	 * @param player	the player to set to ready.
	 */
	public void ready(Player player) {
		player.setReady(true);
		readyCount++;
		
		if(readyCount == players.size() && players.size() > 1) {
			playerInfo();
			gameStarting();
		}
	}
	
	/**
	 * Getter for currentMove.
	 * @return	the currentMove number, the index of the player
	 */
	public int getCurrentMove() {
		return currentMove;
	}
	
	/**
	 * Setter for the currentMove number. This is also modded with the number of
	 * players in the game as to remove IndexOutOfBounds Exceptions.
	 * 
	 * @param currentMove	the index to change the currentMove counter to.
	 */
	public void setCurrentMove( int currentMove ) {
		this.currentMove = currentMove % players.size();
	}
	
	/**
	 * Increments the currentMove counter by one. This is also modded with the number
	 * of players in the game as to remove IndexOutOfBounds Exceptions.
	 */
	public void incrementMove() {
		currentMove = (currentMove + 1) % players.size();
		
		while(players.get(currentMove).getNumTerritories() == 0) {
			currentMove = (currentMove + 1) % players.size();
		}
		
		Iterator<ClientProxy> iterator = listeners.keySet().iterator();
		while(iterator.hasNext()) {
			try {
				iterator.next().sendTurn(currentMove);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fortify();
	}
	
	/**
	 * This method will set a specified player in the list of current players
	 * to the given player object.
	 * 
	 * @param index		the index of the player object to set
	 * @param player	the player object associated with this index number
	 */
	public void setPlayer(int index, Player player) {
		players.set(index, player);
	}
	
	/**
	 * This method executes after each turn to allow the previous player to fortify
	 * or build up his army counts on his territories. This is implemented server side
	 * to just randomly add one to a territory that belongs to him. or her.
	 */
	private void fortify() {
		int playerIndex = currentMove - 1;
		if(playerIndex < 0) {
			playerIndex = players.size()-1;
		}
		System.out.println("fortify " + playerIndex);
		Player player = players.get(playerIndex);
		int numArmies = player.getNumTerritories() / 2;
		while(numArmies > 0) {
			Territory territory = player.getRandomTerritory();
			Army army = territory.getArmy();
			army.changeCount( 1 );
			updateTerritory(territory.getIndex(), territory);
			numArmies--;
		}
		
	}
	
	/**
	 * If for some reason a player no longer wants to play the game, his or her connection
	 * fails or some other network problem exists so that a player is not able to continue
	 * playing he or she will be considered as "surrendering".
	 * 
	 * Surrendering will turn all your territories over to the server to distribute
	 * evenly to the players who are left "alive" in this current game session.
	 * 
	 * @param player	the player object associated with the player surrendering.
	 */
	public synchronized void surrender(Player player) {
		System.out.println("surrender " + player.getIndex());
		player.setAlive(false);
		/*
		if(alivePlayers() == 1) {
			Iterator<ClientProxy> iterator = listeners.keySet().iterator();
			while(iterator.hasNext()) {
				try {
					iterator.next().winnerFound(getWinner());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}*/
		
		HashMap<Integer, Territory> tempTerritories = player.getTerritories();
		Iterator<Territory> iterator = tempTerritories.values().iterator();
		while(iterator.hasNext()) {
			Territory territory = iterator.next();
			Player randPlayer = getRandomPlayer();
			
			Army newArmy = new Army(randPlayer, territory.getArmy().getCount());
			Territory newTerritory = new Territory(territory.getIndex(), territory.getRow(), territory.getColumn());
			newTerritory.setArmy(newArmy);
			
			updateTerritory(newTerritory.getIndex(), newTerritory);
		}
		
		if(currentMove == player.getIndex())
			incrementMove();
		if(getWinner() != null) {
			winnerFound(getWinner());
		}
		
	}
	
	public Player getRandomPlayer() {
		Player player = players.get(rand.nextInt(players.size()));
		while(!player.getAlive() && alivePlayers() > 0) {
			player = getRandomPlayer();
		}
		return player;	
	}
	
	public int alivePlayers() {
		int numPlayersAlive = 0;
		for(Player player : players) {
			if(player.getAlive())
				numPlayersAlive++;
		}
		return numPlayersAlive;
	}
	
	/**
	 * Once a winner has been found this method will send out that information to all
	 * the client programs to that they can handle this according.
	 * 
	 * @param winner	the player object associated with the winner.
	 */
	public void winnerFound(Player winner) {
		Iterator<ClientProxy> iterator = listeners.keySet().iterator();
		while(iterator.hasNext()) {
			try {
				iterator.next().winnerFound(winner);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		SessionMap.removeSession(this);
	}
	
	/**
	 * Getter for the sameStarted boolean flag.
	 * 
	 * @return gameStarted	true, if the game is in progress else returns false.
	 */
	public boolean isGameStarted() {
		return gameStarted;
	}

}
