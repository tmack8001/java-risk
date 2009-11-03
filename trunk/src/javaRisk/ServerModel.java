/**
 * GameBoard.java
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
 * Representation of a general Risk Game.
 * 
 * @author Trevor Mack
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
	
	public ServerModel() {
		this(new ArrayList<Player>(), Constants.ROW_SIZE, Constants.COL_SIZE);
	}
	
	public ServerModel(List<Player> players, int rows, int cols) {
		territories = new ArrayList<Territory>();
		listeners = new HashMap<ClientProxy, Player>();
		this.players = players;
		this.rows = rows;
		this.cols = cols;
		readyCount = 0;
	}
	
	public synchronized void addListener(ClientProxy client) {
		Player player = new Player(players.size(), "Player " + players.size(), 
				Color.getHSBColor(rand.nextFloat(), 1.0f, 1.0f));
		client.setPlayer(player);
		players.add(player);
		listeners.put(client, player);
	}
	
	public synchronized void addListener(ClientProxy client, String name) {
		Player player = new Player(players.size(), name, 
				Color.getHSBColor(rand.nextFloat(), 1.0f, 1.0f));
		players.add(player);
		listeners.put(client, player);
	}
	
	public synchronized void removeListener(ClientProxy client) {
		Player player = listeners.remove(client);
		player.surrender();
		players.remove(player);
	}
	
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
	
	public void placeArmies() {
		System.out.println("placearmies");
		int terrPerPlayer = (int) (territories.size() / players.size()) + 1;
		for(int i=0; i<territories.size(); i++) {
			boolean assigned = false;
			while(!assigned) {
				Player player = players.get( rand.nextInt(players.size()) );
				if( player.getTerritories().size() < terrPerPlayer ) {
					int armySize = randomArmySize(player);
					Territory t = territories.get(i);
					t.setArmy(new Army(player, armySize));
					updateTerritory(t.getIndex(), t);
					assigned = true;
				}
			}
		}
	}
	
	private int randomArmySize( Player player ) {
		return (rand.nextInt(7) + 1);
	}
	
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
	
	public boolean gameStarting() {
		if(readyCount == players.size()) {
			gameStarted = true;
			initializeBoard();
			placeArmies();
			
			Iterator<ClientProxy> iterator = listeners.keySet().iterator();
			while(iterator.hasNext()) {
				try {
					iterator.next().gameStarting(players.size());
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
	
	public Player getWinner() {
		Iterator<Player> iterator = players.iterator();
		int numAlive = 0;
		Player winner = null;
		while( iterator.hasNext() ) {
			winner = iterator.next();
			if(winner.getAlive()) {
				if(++numAlive > 1)
					return null;
			}
		}
		return winner;
	}
	
	public void ready(Player player) {
		player.setReady(true);
		readyCount++;
		
		if(readyCount == players.size() && players.size() > 1) {
			playerInfo();
			gameStarting();
		}
	}
	
	public int getCurrentMove() {
		return currentMove;
	}
	
	public void setCurrentMove( int currentMove ) {
		this.currentMove = currentMove;
	}
	
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
	
	public void setPlayer(int index, Player player) {
		players.set(index, player);
	}
	
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
	
	public void winnerFound(Player winner) {
		Iterator<ClientProxy> iterator = listeners.keySet().iterator();
		while(iterator.hasNext()) {
			try {
				iterator.next().winnerFound(winner);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the gameStarted
	 */
	public boolean isGameStarted() {
		return gameStarted;
	}

	public static void main(String[] args) {
		/* Used for Testing */
		List<Player> players = new ArrayList<Player>();
		players.add(new Player(0, "player 1", Color.RED));
		players.add(new Player(1, "player 2", Color.BLUE));
		ServerModel gb = new ServerModel(players,4,4);
		
		gb.initializeBoard();
		gb.placeArmies();
		gb.setCurrentMove(0);
		
		while(gb.getWinner() == null) {
			Player attacker = players.get(gb.getCurrentMove());
			gb.incrementMove();
			Player defender = players.get(gb.getCurrentMove());
			int[] attacked = null;
			System.out.println("ATTACKER: " +attacker.getName());
			System.out.println("DEFENDER: " +defender.getName());
			while(attacked != null) {
				Territory attacking = attacker.getRandomTerritory();
				Territory defending = attacking.getAdjacent(defender.getTerritories());
				//Territory defending = defender.getRandomTerritory();
				if(defending != null)
					attacked = attacking.attack(defending);
				else {
					System.out.println("INFINITE");
				}
			}
		}
		System.out.println("WINNER FOUND!");
		System.out.println(gb.getWinner().getName());
	}
}
