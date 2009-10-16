package javaRisk;

public final class Constants {

	/**
	 * PORT
	 */
	public static final int PORT = 1988;
	
	// OPCODES
	
	/**
	 * READY
	 */
	public static final byte READY = 1;
	
	/**
	 * ATTACK -- args: src-territory, dest-territory
	 */
	public static final byte ATTACK = 2;
	
	/**
	 * SURRENDER
	 */
	public static final byte SURRENDER = 13;
	
	/**
	 * END_TURN
	 */
	public static final byte END_TURN = 4;
	
	/**
	 * GAME_STARTING
	 */
	public static final byte GAME_STARTING = 5;
	
	/**
	 * TURN_IND -- args: player-num
	 */
	public static final byte TURN_IND = 6;
	
	/**
	 * ATTACK_MADE -- args: src-territory, dest-territory, attacker_roll, defender_roll
	 */
	public static final byte ATTACK_MADE = 7;
	
	/**
	 * TERRITORY_STATUS -- args: index, new_owner, army_size
	 */
	public static final byte TERRITORY_STATUS = 8;
	
}
