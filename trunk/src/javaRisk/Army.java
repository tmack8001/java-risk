/**
 * Army.java
 */
package javaRisk;

import java.util.Random;

/**
 * @author Trevor Mack
 *
 */
public class Army {

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
	
	public int getRoll( boolean attack ) {
		int armyCount = getCount();
		if(attack) {
			armyCount--;
		}
		
		int totalRoll = 0;
		for(int i=0; i<armyCount; i++) {
			Random generator = new Random();
			int diceRoll = generator.nextInt(6)+1;
			totalRoll += diceRoll;
		}
		return totalRoll;
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
