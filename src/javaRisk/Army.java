package javaRisk;
/**
 * Army.java
 */

/**
 * @author Trevor Mack
 *
 */
public class Army {

	private Territory territory;
	private Player player;
	private int count;
	
	public Army(Player player, int count) {
		this.player = player;
		this.count = count;
	}

	public boolean changeCount(int amount) {
		count += amount;
		if(count < 0) {
			return false;
		}else {
			return true;
		}
	}
	
	public Territory getTerritory() {
		return territory;
	}

	public void setTerritory(Territory newTerritory) {
		territory = newTerritory;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getCount() {
		return count;
	}
}
