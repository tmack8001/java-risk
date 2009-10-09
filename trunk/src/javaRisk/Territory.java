package javaRisk;
/**
 * Territory.java
 */
import Army;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Trevor Mack
 *
 */
public class Territory {

	private int index;
	private ArrayList<Territory> adjacent;
	private Army army;
	private int row,column;
	
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
	
	//TODO: should the territories know what territories are adjacent to them? or the GameBoard?
	public boolean isAdjacent(Territory territory) {
		Iterator<Territory> adjacent = getAdjacent().iterator();
		while(adjacent.hasNext()) {
			if( adjacent.equals(territory))
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 */
	public void addArmy(Army newArmy) {
		if(army == null) {
			army = newArmy;
			army.setTerritory(this);
		}else {
			addArmy(newArmy.getCount());
		}
	}
	
	public void addArmy(int count) {
		if(army == null) {
			addArmy(new Army(getOwner(), count));
		}else {
			army.changeCount(count);
		}
	}
	
	public Army getArmy() {
		return army;
	}
	
	public Player getOwner() {
		if(army == null) {
			return null;
		}else {
			return army.getPlayer();
		}
	}
	
	//TODO: should the territories know what territories are adjacent to them? or the GameBoard?
	public ArrayList<Territory> getAdjacent() {
		return adjacent;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	
	
}
