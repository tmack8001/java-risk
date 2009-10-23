/**
 * Player.java
 */
package javaRisk;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


/**
 * @author Trevor Mack
 *
 */
public class Player {

	private int index;
	private String name;
	private boolean alive;
	private Color color;
	private HashMap<Integer, Territory> territories;
	
	/** Create a new Player with the given name.
	 * 
	 * @param name the player's name
	 */
	public Player( int index, String name, Color color ) {
		this.index = index;
		this.name = name;
		this.color = color;
		territories = new HashMap<Integer, Territory>();
		alive = true;
	}
	
	/** This allows a player to surrender at any point in the game.
	 */
	public void surrender() {
		alive = false;
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
	
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getAlive() {
		return alive && getTerritories().size() > 0;
	}
	
	/**
	 * @param alive the alive to set
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Territory getTerritory(int index) {
		return territories.get(new Integer(index));
	}
	
	public Territory getRandomTerritory() {
		Random generator = new Random();
		Object[] values = territories.values().toArray();
		return (Territory) (values[generator.nextInt(values.length)]);
	}
	
	public String toString() {
		return getName() + ":     " + getTotalArmies();
	}
}
