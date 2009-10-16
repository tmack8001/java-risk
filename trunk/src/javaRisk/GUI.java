package javaRisk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JFrame {


	public GUI()
	{
		super("Risk");
		this.setLayout(new BorderLayout());
		JPanel mainGrid = new JPanel(new GridLayout(7,7));
		for (int i = 0 ; i < 49 ; i++)
		{
			mainGrid.add(new JLabel(""+(i + 1)));
		}
		this.add(mainGrid, BorderLayout.CENTER);
		this.setSize(800, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void showPlayerTurn(int player) {
		// TODO Auto-generated method stub
		
	}

	public void showGamePlayable(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void showAttack(int src, int dest) {
		// TODO Auto-generated method stub
		
	}

	public void showAttackRoll(int a_roll) {
		// TODO Auto-generated method stub
		
	}

	public void showDefenseRoll(int d_roll) {
		// TODO Auto-generated method stub
		
	}

	public void updateTerritory(int index, Color color, int size) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String args[])
	{
		//TESTING THE GUI
		
		new GUI().setVisible(true);
	}
	
	
}
