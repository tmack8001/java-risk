/**
 * Army.java
 */
package javaRisk;

/**
 * @author Trevor Mack
 *
 */
public class Army {

	private Territory territory;
	private Player player;
	private int count;
	
	private final int MAX_ATTACK = 3;
	private final int MAX_DEFEND = 3;
	
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
	
	public int getRoll( boolean attack ) {
		int armyCount = getCount();
		if( attack ) {
			if( armyCount > MAX_ATTACK ) {
				armyCount = MAX_ATTACK;
			}
			armyCount = armyCount - 1;
		}else {
			if( armyCount > MAX_DEFEND ) {
				armyCount = MAX_DEFEND;
			}
		}
		
		int totalRoll = 0;
		for(int i=0; i<armyCount; i++) {
			int diceRoll = (int)(Math.random()*6);
			totalRoll += diceRoll;
		}
		return totalRoll;
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
	
	public void setPlayer(Player player2) {
		this.player = player2;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
}
