package javaRisk;

import java.io.IOException;

public interface UIListener {

	public void endTurn() throws IOException ;
	
	public void launchAttack(int src, int dest) throws IOException;
	
	public void surrender() throws IOException;

	public void clicked(int territory) throws IOException;
	
}
