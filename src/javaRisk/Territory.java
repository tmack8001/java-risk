package javaRisk;

/**
 * The Territory class represents a single territory on the world map.
 * @author Trevor Mack
 * @author Dylan Hall
 */
public class Territory {

	/**
	 * The index of this Territory.
	 */
	private int index;
	
	/**
	 * The army on this Territory.
	 */
	private Army army;
	
	/**
	 * The row and column on the grid where this Territory is located.
	 */
	private final int row,column;

	// flags used for launching an attack
	private static final boolean ATTACK = true;
	private static final boolean DEFEND = false;
	
	/**
	 * Smallest size an army can be.
	 */
	private static final int MINIMUM_ARMY = 1;
	
	/**
	 * Creates a new territory with the given index, row, and column numbers.
	 * 
	 * @param index the index number of the territory
	 * @param row the row that the territory exists in
	 * @param column the column that the territory exists in
	 */
	public Territory(int index, int row, int column) {
		this.index = index;
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Launch an attack from this territory to another.
	 * @param territory - the territory to attack from this one
	 * @return array [0] - this index
	 * 				 [1] - the other territory index
	 *  			 [2] - the roll from this territory's army
	 *  			 [3] - the roll from the other territory's army
	 */
	public int[] attack(Territory territory) {
		if(!getOwner().equals(territory.getOwner()) && getArmy().getCount() > 1 && isAdjacent(territory)) {
			Army defendingArmy = territory.getArmy();
			Army attackingArmy = getArmy();
			int attackingRoll = attackingArmy.getRoll( ATTACK );
			int defendingRoll = defendingArmy.getRoll( DEFEND );
			
			int[] attackResults = new int[4];
			attackResults[0] = this.getIndex();
			attackResults[1] = territory.getIndex();
			attackResults[2] = attackingRoll;
			attackResults[3] = defendingRoll;
			
			if(attackingRoll > defendingRoll) {
				Army deployedArmy = new Army(getOwner(), attackingArmy.getCount()-MINIMUM_ARMY);
				attackingArmy.setCount( MINIMUM_ARMY );
				
				territory.setArmy(deployedArmy);
			}else {
				attackingArmy.setCount( MINIMUM_ARMY );
			}
			return attackResults;
		}
		return null;
	}
	
	/**
	 * Calculates if a territory is adjacent to the current territory.
	 * 
	 * @param territory - territory to check for adjacency
	 * @return true, if the territories are adjacent
	 */
	public boolean isAdjacent(Territory territory) {
		int rowDiff = Math.abs( getRow() - territory.getRow() );
		int colDiff = Math.abs( getColumn() - territory.getColumn() );
		
		//false, if either same territory or further than 1 square away including corner adjacent
		return (rowDiff + colDiff == 1);
	}
	
	/**
	 * Getter for the army residing on this territory.
	 * @return army on this territory
	 */
	public Army getArmy() {
		return army;
	}
	
	/**
	 * Sets the terroritory's army to the "invading" army.
	 * 
	 * @param newArmy - army moving to this territory
	 */
	public void setArmy(Army newArmy) {
		if(army != null && newArmy.getPlayer() != army.getPlayer()) {
			army.getPlayer().removeTerritory(this);	
		}
		newArmy.getPlayer().addTerritory(this);
		army = newArmy;
	}
	
	/**
	 * Finds the player that owns the resident army.
	 * 
	 * @return player associated with the army
	 */
	public Player getOwner() {
		if(army == null) {
			return null;
		}else {
			return army.getPlayer();
		}
	}
	
	/**
	 * Getter for the unique index number. 
	 * 
	 * @return index of territory
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Getter for the row value.
	 * 
	 * @return row position number
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Getter for the column value.
	 * 
	 * @return column position number
	 */
	public int getColumn() {
		return column;
	}	
}