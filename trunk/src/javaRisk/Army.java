package javaRisk;

import java.util.Random;

/**
 * The Army class represents the army that a player uses to attack
 * from one territory to another.
 * 
 * @author Trevor Mack
 * @author Dylan Hall
 */
public class Army {

	/**
	 * The player who owns the Army.
	 */
	private Player player;
	
	/**
	 * The size of the Army.
	 */
	private int count;
	
	/**
	 * Constructor. Set the owner and size.
	 * @param player - the player who owns the Army
	 * @param count - the size of the Army
	 */
	public Army(Player player, int count) {
		this.player = player;
		this.count = count;
	}

	/**
	 * Add to this army.
	 * @param amount - the size to add.
	 * @return true if the army is valid
	 */
	public boolean changeCount(int amount) {
		count += amount;
		if(count < 0) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * Get the roll of the dice for this Army.
	 * Attackers roll one fewer dice than the total size.
	 * Defenders roll the same number as the total size.
	 * @param attack - true if attacking, false if defending.
	 * @return the dice roll for this army.
	 */
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
	
	/**
	 * Get the owner of this Army.
	 * @return the owner.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Set the owner of this Army.
	 * @param player - the owner.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * Get the size of the Army.
	 * @return the size of the Army.
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * Set the size of the army.
	 * @param count the new size of the Army.
	 */
	public void setCount(int count) {
		this.count = count;
	}
}
