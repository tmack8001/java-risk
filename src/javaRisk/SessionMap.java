package javaRisk;

import java.util.HashMap;

public class SessionMap {

	private static HashMap<String, ServerModel> map = new HashMap<String, ServerModel>();
	
	public static ServerModel getSession(String sessionName)
	{
		ServerModel model = map.get(sessionName);
		
		if (model == null)
		{
			model = new ServerModel();
			map.put(sessionName, model);
		}
		
		return model;
	}
	
}
