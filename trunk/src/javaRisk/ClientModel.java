package javaRisk;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
/**
 * ClientModel represents a client-side model of the Risk game board.
 * It maintains a client-side representation of the territories
 * and the players, allowing post-setup network traffic to be minimized.
 * 
 * @author Dylan Hall
 * @author Trevor Mack
 */
public class ClientModel {

	/**
	 * List of territories.
	 */
	private List<Territory> territories;
	
	/**
	 * List of players.
	 */
	private List<Player> players;
	
	/**
	 * The index of the user this ClientModel serves.
	 */
	private int me;
	
	/**
	 * The index of this user's last clicked territory.
	 * A user must first click on one of his own territories 
	 * to launch an arrack. This value becomes the attack source.
	 */
	private int clicked;
	
	/**
	 * Constructor for a ClientModel.
	 * Sets up an empty list of Players, 
	 * and a full list of Territories.
	 */
	public ClientModel()
	{
		players = new ArrayList<Player>();
		territories = new ArrayList<Territory>(Constants.ROW_SIZE*Constants.COL_SIZE);
		
		int index = 0;
		for (int row = 0 ; row < Constants.ROW_SIZE ; row++)
		{
			for (int col = 0 ; col < Constants.COL_SIZE ; col++)
			{
				Territory curr = new Territory(index, row, col);
				territories.add(curr);
				index++;
			}
		}
	}
	
	/**
	 * Set the index of the user this ClientModel represents.
	 * @param me - index of this user
	 */
	public void setMe(int me)
	{
		this.me = me;
	}
	
	/**
	 * Determine if a given player is this ClientModel's user.
	 * @param player - index to check
	 * @return true if the given index == my index.
	 */
	public boolean isMe(int player)
	{
		return player == me;
	}
	
	/**
	 * Set the index of a clicked territory. Called when a user clicks his own territory.
	 * @param clicked - the index of a territory
	 */
	public void setClicked(int clicked){
		this.clicked = clicked;
	}
	
	/**
	 * Get the index of a clicked territory or -1 if no territory is clicked.
	 * @return the clicked territory index or -1 if none clicked
	 */
	public int getClicked()
	{
		return clicked;
	}
	
	/**
	 * Determine if 2 territories are adjacent.
	 * @param t1 - first territory to check
	 * @param t2 - second territory to check
	 * @return true if the distance between the 2 territories == 1
	 */
	public boolean isAdjacent(int t1, int t2)
	{
		return territories.get(t1).isAdjacent(territories.get(t2));
	}
	
	/**
	 * Determine if the user this ClientModel serves owns a given territory.
	 * @param territory - index of the territory to check
	 * @return true if the owner of the given territory is this user.
	 */
	public boolean isMine(int territory)
	{
		return players.get(me) == territories.get(territory).getOwner();
	}
	
	/**
	 * Get a player's color by player index.
	 * @param player the player index (0-based)
	 * @return the user's color as a Color object
	 */
	public Color getPlayerColor(int player)
	{
		return players.get(player).getColor();
	}
	
	/**
	 * Add a player to the list of players. Called during game initialization.
	 * @param index - the player index (0-based)
	 * @param color - the player's color
	 * @param name - the player's username
	 */
	public void fillPlayerData(int index, Color color, String name)
	{
		players.add(new Player(index, name, color));
	}
	
	/**
	 * Update the client representation with new territory information.
	 * Called upon board initialization and after each attack/fortification.
	 * @param index - territory index to update
	 * @param owner - the index of the new owner of the territory
	 * @param size - the size of the new army on the territory
	 */
	public void updateTerritory(int index, int owner, int size) {
		territories.get(index).setArmy(new Army(players.get(owner), size));
	}
	
	/**
	 * Get the list of player usernames.
	 * @return an array of Strings where each String is a player name.
	 */
	public String[] getPlayerNames()
	{
		ArrayList<String> names = new ArrayList<String>();
		for (Player p : players)
		{
			names.add(p.getName());
		}
		return names.toArray(new String[0]);
	}

	/**
	 * Set the list of players.
	 * @param players - the List of Players to set.
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	/**
	 * Get the index of the owner of a territory.
	 * @param src - the territory index
	 * @return the owner index
	 */
	public int getOwnerOfTerr(int src) {
		return territories.get(src).getOwner().getIndex();
	}
}
