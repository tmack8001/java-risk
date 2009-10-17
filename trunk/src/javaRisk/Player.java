/**
 * Player.java
 */
package javaRisk;
import java.util.HashMap;
import java.util.Iterator;


/**
 * @author Trevor Mack
 *
 */
public class Player {

	private int index;
	private String name;
	private boolean alive;
	private HashMap<Integer, Territory> territories;
	
	/** Create a new Player with the given name.
	 * 
	 * @param name the player's name
	 */
	public Player( int index, String name ) {
		this.index = index;
		this.name = name;
		territories = new HashMap<Integer, Territory>();
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
		return alive && getTerritories().size() > 0;
	}
	
	public int getTotalArmies() {
		int totalArmies = 0;
		Iterator<Integer> iterator = getTerritories().keySet().iterator();
		while(iterator.hasNext()) {
			totalArmies += territories.get(iterator.next()).getArmy().getCount();
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
	public HashMap<Integer, Territory> getTerritories() {
		return territories;
	}
	
	public void addTerritory(Territory territory) {
		territories.put(new Integer(territory.getIndex()), territory);
	}
	
	public boolean removeTerritory(Territory territory) {
		if(territories.containsKey(new Integer(territory.getIndex()))) {
			territories.remove(new Integer(territory.getIndex()));
			return true;
		}
		return false;
	}
	
	
	
}
