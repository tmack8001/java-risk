package javaRisk;

import java.util.ArrayList;
import java.util.List;

/**
 * Server-side implementation of game model.
 * @author Dylan Hall
 */
public class ServerModel {

	private List<Player> players;
	
	private int readyCount;
	
	public ServerModel() {
		players = new ArrayList<Player>();
		readyCount = 0;
	}
	
	public void addPlayer(Player p)
	{
		players.add(p);
	}
	
	public void attack(int src, int dest) {
		// TODO Auto-generated method stub
		
	}

	public void endTurn(Player player) {
		// TODO Auto-generated method stub
		
	}

	public void surrender(Player player) {
		players.remove(player);
		
	}

	public void ready(Player player) {
		readyCount++;
		
	}

}
