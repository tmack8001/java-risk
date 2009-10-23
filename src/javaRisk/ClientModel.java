package javaRisk;

import java.awt.Color;
import java.util.ArrayList;

import java.util.HashMap;

public class ClientModel {

	private ArrayList<Territory> territories;
	private ArrayList<Player> players;
	
	private int me;
	
	private int clicked;
	
	public ClientModel(int numPlayers)
	{
		players = new ArrayList<Player>(numPlayers);
		territories = new ArrayList<Territory>(Constants.BOARD_SIZE*Constants.BOARD_SIZE);
		
		for (int n = 0 ; n < numPlayers ; n++)
		{
			players.add(new Player(n, "", null));
		}
		
		int i = 0;
		for (int r = 0 ; r < Constants.BOARD_SIZE ; r++)
		{
			for (int c = 0 ; c < Constants.BOARD_SIZE ; c++)
			{
				Territory curr = new Territory(i, r, c);
				territories.add(curr);
				i++;
			}
			
		}
	}
	
	public void setMe(int me)
	{
		this.me = me;
	}
	
	public boolean isMe(int player)
	{
		return player == me;
	}
	
	public void setClicked(int clicked){
		this.clicked = clicked;
	}
	
	public int getClicked()
	{
		return clicked;
	}
	
	public boolean isAdjacent(int t1, int t2)
	{
		return territories.get(t1).isAdjacent(territories.get(t2));
	}
	
	public boolean isMine(int territory)
	{
		return players.get(me) == territories.get(territory).getOwner();
	}
	
	public Color getPlayerColor(int player)
	{
		return players.get(player).getColor();
	}
	
	public void fillPlayerData(int index, Color color, String name)
	{
		players.set(index, new Player(index, name, color));
	}
	
	public void updateTerritory(int index, int owner, int size) {
		territories.get(index).setArmy(new Army(players.get(owner), size));
	}

}
