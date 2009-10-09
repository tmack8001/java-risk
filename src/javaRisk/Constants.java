package javaRisk;

public final class Constants {

	public static final int PORT = 1988;
	
	// OPCODES
	
	public static final byte READY = 1;
	
	// ATTACK -- args: src-territory, dest-territory
	public static final byte ATTACK = 2;
	
	public static final byte SURRENDER = 13;
	
	public static final byte END_TURN = 4;
	
	public static final byte GAME_STARTING = 5;
	
	// TURN_IND -- args: player-num
	public static final byte TURN_IND = 6;
	
	// ATTACK_MADE -- args: src-territory, dest-territory
	public static final byte ATTACK_MADE = 7;
	
	public static final byte TERRITORY_STATUS = 8;
	
}
