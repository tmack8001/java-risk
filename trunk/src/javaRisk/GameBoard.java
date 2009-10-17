/**
 * GameBoard.java
 */
package javaRisk;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Representation of a general Risk Game.
 * 
 * @author Trevor Mack
 * 
 */
public class GameBoard {
	
	private ArrayList<Territory> territories;
	private ArrayList<Player> players;
	private int currentMove;
	private int rows, cols;
	
	/*TODO: figure out an algorithm for number of armies per player*/
	private static final int NUM_ARMY = 40;
	
	public GameBoard(ArrayList<Player> players, int rows, int cols) {
		territories = new ArrayList<Territory>();
		this.players = players;
		this.rows = rows;
		this.cols = cols;
	}
	
	public void initializeBoard() {
		int index = 0;
		for(int row=0; row<rows; row++) {
			for(int col=0; col<cols; col++) {
				territories.add(new Territory(index++, row, col));
			}
		}
		placeArmies();
		
		setCurrentMove(0);
	}
	
	public void placeArmies() {
		int terrPerPlayer = (int) (territories.size() / players.size()) + 1;
		for(int i=0; i<territories.size(); i++) {
			boolean assigned = false;
			while(!assigned) {
				Player player = players.get( (int)(Math.random() * players.size()) );
				if( player.getTerritories().size() < terrPerPlayer) {
					int armySize = randomArmySize(player);
					territories.get(i).setArmy(new Army(player, armySize));
					assigned = true;
				}
			}
		}
	}
	
	private int randomArmySize( Player player ) {
		return (int)(Math.random() * 7 + 1);
	}
	
	public void updateTerritory(int index, int owner, int size) {
		Territory territory = territories.get( index );
		territory.setArmy( new Army(players.get(owner), size) );
	}
	
	public boolean winnerExists() {
		Iterator<Player> iterator = players.iterator();
		int numAlive = 0;
		while( iterator.hasNext() ) {
			if(iterator.next().isAlive()) {
				numAlive++;
			}
			if(numAlive > 1) {
				return false;
			}
		}
		return true;
	}
	
	public int getCurrentMove() {
		return currentMove;
	}
	
	public void setCurrentMove( int currentMove ) {
		this.currentMove = currentMove;
	}
	
	public void incrementMove() {
		if(++currentMove >= players.size())
			currentMove = 0;
	}
	
	public static void main(String[] args) {
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(new Player(0, "player 1"));
		players.add(new Player(1, "player 2"));
		GameBoard gb = new GameBoard(players,4,4);
		
		gb.initializeBoard();
		gb.setCurrentMove(0);
		while(!gb.winnerExists()) {
			Player attacker = players.get(gb.getCurrentMove());
			gb.incrementMove();
			Player defender = players.get(gb.getCurrentMove());
			boolean attacked = false;
			while(!attacked) {
				int source = (int)(Math.random()*attacker.getTerritories().size());
				int target = (int)(Math.random()*defender.getTerritories().size());
				attacked = gb.territories.get(source).attack(gb.territories.get(target));
			}
			gb.incrementMove();
		}
	}
}
