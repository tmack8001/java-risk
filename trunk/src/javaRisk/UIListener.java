package javaRisk;

public interface UIListener {

	public void endTurn();
	
	public void startAttack(int territory);
	
	public void launchAttack(int src, int dest);
	
	public void surrender();
	
}
