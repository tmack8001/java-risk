package javaRisk;

import java.util.HashMap;
import java.util.Iterator;

/**
 * The SessionMap class associates a name to each current instance of a running game.
 * @author Dylan Hall
 * @author Trevor Mack
 */
public class SessionMap {

	/**
	 * The underlying map.
	 */
	private static HashMap<String, ServerModel> map = new HashMap<String, ServerModel>();
	
	/**
	 * Get a running game based on the session name.
	 * If not found in the map, a new game is created, added to the map, and returned.
	 * @param sessionName - the game name
	 * @return a session associated with that name
	 */
	public synchronized static ServerModel getSession(String sessionName)
	{
		ServerModel model = map.get(sessionName);
		
		if (model == null)
		{
			model = new ServerModel();
			map.put(sessionName, model);
		}
		
		return model;
	}
	
	/**
	 * Remove a session from the 
	 * @param sessionName
	 */
	public synchronized static void removeSession(ServerModel game)
	{
		Iterator<ServerModel> itr = map.values().iterator();
		
		while(itr.hasNext())
		{
			ServerModel curr = itr.next();
			if (curr == game)
			{
				// identity equality should be enough
				
				itr.remove();
				break;
			}
		}
		
	}
	
}
