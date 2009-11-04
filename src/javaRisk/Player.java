package javaRisk;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


/**
 * The Player class represents a single player of the Risk game.
 * @author Trevor Mack
 * @author Dylan Hall
 */
public class Player {

	/**
	 * This Player's index.
	 */
	private int index;
	
	/**
	 * This player's username.
	 */
	private String name;
	
	/**
	 * Flag for if this player is still connected/has any territories.
	 */
	private boolean alive;
	
	/**
	 * The color used to represent this player's territories.
	 */
	private Color color;
	
	/**
	 * The set of this player's territories.
	 */
	private HashMap<Integer, Territory> territories;
	
	/**
	 * Flag for if this user has signaled ready to play.
	 */
	private boolean ready;
	
	/**
	 * Dummy constructor - used in ClientProxy before any info is known.
	 */
	public Player() {
	}
	
	/** Create a new Player with the given name.
	 * 
	 * @param index - the user index
	 * @param name the player's name
	 * @param color - the color representing this user
	 */
	public Player( int index, String name, Color color ) {
		this.index = index;
		this.name = name;
		this.color = color;
		territories = new HashMap<Integer, Territory>();
		alive = true;
		ready = false;
	}
	
	/** 
	 * This allows a player to surrender at any point in the game.
	 */
	public void surrender() {
		alive = false;
	}
	
	/**
	 * Get the total number of armies this player controls.
	 * @return the total number of armies on all territories.
	 */
	public int getTotalArmies() {
		int totalArmies = 0;
		Iterator<Integer> iterator = getTerritories().keySet().iterator();
		while(iterator.hasNext()) {
			totalArmies += territories.get(iterator.next()).getArmy().getCount();
		}
		return totalArmies;
	}

	/**
	 * Get the map of territories this player controls.
	 * @return the territories
	 */
	public HashMap<Integer, Territory> getTerritories() {
		return territories;
	}
	
	/**
	 * Add a territory to this player's control.
	 * @param territory - the territory to add
	 */
	public void addTerritory(Territory territory) {
		territories.put(new Integer(territory.getIndex()), territory);
	}
	
	/**
	 * Remove a territory from this player's control.
	 * @param territory - territory to remove
	 * @return true if the player owned that territory.
	 */
	public boolean removeTerritory(Territory territory) {
		if(territories.containsKey(new Integer(territory.getIndex()))) {
			territories.remove(new Integer(territory.getIndex()));
			return true;
		}
		return false;
	}
	
	/**
	 * Get the color used to represent this user on the map.
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Set the color used to represent this user on the map.
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Get this player's name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set this player's name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Determine if this player is still playing.
	 * @return true if the player is connected and has territories.
	 */
	public boolean getAlive() {
		return alive && getTerritories().size() > 0;
	}
	
	/**
	 * Set whether or not the player is connected.
	 * @param alive the alive to set
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	/**
	 * Get the user's index.
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Determine if this player is ready to play.
	 * @return true if the player has signaled ready to play
	 */
	public boolean getReady() {
		return ready;
	}
	
	/**
	 * Set whether this player is ready to play.
	 * @param ready - true if the user has signaled ready
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	/**
	 * Get the number of Territories this player controls.
	 * @return - number of Territories
	 */
	public int getNumTerritories() {
		return territories.size();
	}

	/**
	 * Get a territory based on index.
	 * @param index - index to get
	 * @return Territory
	 */
	public Territory getTerritory(int index) {
		return territories.get(new Integer(index));
	}
	
	/**
	 * Get a random territory from this user's control.
	 * @return - random Territory
	 */
	public Territory getRandomTerritory() {
		Random generator = new Random();
		Object[] values = territories.values().toArray();
		return (Territory) (values[generator.nextInt(values.length)]);
	}
	
	/**
	 * Get a String representation of this Player.
	 * @return format "Name:   #"
	 */
	public String toString() {
		return getName() + ":     " + getTotalArmies();
	}
}
