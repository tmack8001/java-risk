package javaRisk;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */

/**
 * @author Trevor Mack
 *
 */
public class Player {

	private String name;
	private boolean alive;
	private ArrayList<Territory> territories;
	
	/** Create a new Player with the given name.
	 * 
	 * @param name the player's name
	 */
	public Player( String name ) {
		this.name = name;
		territories = new ArrayList<Territory>();
		alive = true;
	}
	
	/** This allows a player to surrender at any point in the game.
	 */
	public void surrender() {
		alive = false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAlive() {
		if(getTerritories().size() == 0)
			alive = false;
		return alive;
	}
	
	public int getTotalArmies() {
		int totalArmies = 0;
		Iterator<Territory> territories = getTerritories().iterator();
		while(territories.hasNext()) {
			totalArmies += territories.next().getArmy().getCount();
		}
		return totalArmies;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the territories
	 */
	public ArrayList<Territory> getTerritories() {
		return territories;
	}
	
	
	
}
