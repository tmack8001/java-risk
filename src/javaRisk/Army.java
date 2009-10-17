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
	
	private final int MAX_ATTACK = 6;
	private final int MAX_DEFEND = 5;
	
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
			if( armyCount > MAX_ATTACK )
				armyCount = MAX_ATTACK;
			else 
				armyCount = armyCount - 1;
		}else {
			if( armyCount > MAX_DEFEND )
				armyCount = MAX_DEFEND;
		}
		
		int totalRoll = 0;
		for(int i=0; i<armyCount; i++) {
			Random generator = new Random();
			int diceRoll = generator.nextInt(5)+1;
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
