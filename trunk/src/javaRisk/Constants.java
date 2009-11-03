package javaRisk;

/**
 * Set of constants used throughout the Risk game system.
 * 
 * @author Trevor Mack
 * @author Dylan Hall
 */
public final class Constants {

	/**
	 * HOST
	 */
	public static final String HOST = "localhost";
	
	/**
	 * PORT
	 */
	public static final int PORT = 1988;

	
	/**
	 * ROW_SIZE -- constant number of rows in a risk game
	 */
	public static final int ROW_SIZE = 7;
	
	/**
	 * COL_SIZE -- constant number of columns in a risk game
	 */
	public static final int COL_SIZE = 7;
	
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
	public static final byte SURRENDER = 3;
	
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
	 * ATTACK_MADE -- args: src-territory, dest-territory, attacker-roll, defender-roll
	 */
	public static final byte ATTACK_MADE = 7;
	
	/**
	 * TERRITORY_STATUS -- args: index, owner, army_size
	 */
	public static final byte TERRITORY_STATUS = 8;
	
	/**
	 * PLAYER_INFO (to client)-- args: player-num, color(int), player-name
	 * 			   (to server)-- args: player-name
	 */
	public static final byte PLAYER_INFO = 9;
	
	/**
	 * WHO_AM_I -- args: player-num
	 */
	public static final byte WHO_AM_I = 10;
	
	/**
	 * GAME_TO_JOIN -- args: game-name
	 */
	public static final byte GAME_TO_JOIN = 11;
	
	/**
	 * GAME_STARTING -- args: player-num
	 */
	public static final byte GAME_FINISHED = 12;
}
