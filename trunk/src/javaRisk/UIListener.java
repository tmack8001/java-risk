package javaRisk;

import java.io.IOException;

public interface UIListener {

	public void endTurn() throws IOException ;
	
	public void startAttack(int territory) throws IOException;
	
	public void launchAttack(int src, int dest) throws IOException;
	
	public void surrender() throws IOException;
	
}
