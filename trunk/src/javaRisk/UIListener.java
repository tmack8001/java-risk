package javaRisk;

import java.awt.Color;
import java.io.IOException;

/**
 * Listener interface to listen to UI events.
 * @author Dylan Hall
 * @author Trevor Mack
 */
public interface UIListener {

	/**
	 * Called when the user ends his turn.
	 */
	public void endTurn() throws IOException;
	
	/**
	 * The user launched an attack from one territory to another.
	 * @param src - index of attacking territory
	 * @param dest - index of defending territory
	 */
	public void launchAttack(int src, int dest) throws IOException;
	
	/**
	 * Called when the user closes the GUI before the game is over.
	 */
	public void surrender() throws IOException;

	/**
	 * Called when the user clicks on any territory in the grid.
	 * @param territory - the index of the clicked territory
	 */
	public void clicked(int territory) throws IOException;

	/**
	 * Method for the GUI to determine a player's color.
	 * @param player - player index
	 * @return the Color for that user
	 */
	public Color requestPlayerColor(int player);

	/**
	 * Called when the client attempts to join a game.
	 * @param gameName - the name of the game to join
	 */
	public void joinGame(String gameName) throws IOException;

	/**
	 * Method for the GUI to know how many territories a player has.
	 * @param player - player index
	 * @return the player's number of territories
	 */
	public int requestPlayerCount(int player);
}
