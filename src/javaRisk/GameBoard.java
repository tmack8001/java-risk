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
	private int currentMove = 0;
	
	private static final int NUM_ARMY = 40;
	
	public GameBoard(ArrayList<Player> players, int rows, int cols) {
		this.players = players;
		initializeBoard(rows, cols);
	}
	
	public void initializeBoard(int rows, int cols) {
		int index = 0;
		for(int row=0; row<rows; row++) {
			for(int col=0; col<cols; col++) {
				territories.add(index, new Territory(index, row, col));
			}
		}
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
}
