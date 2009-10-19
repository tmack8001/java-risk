package javaRisk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class GUI extends JFrame {

	private UIListener listener;
	
	private JButton endTurn = new JButton("End Turn");
	private JLabel attackRoll = new JLabel();
	private JLabel defendRoll = new JLabel();

	public GUI()
	{
		super("Risk");
		this.setLayout(new BorderLayout());
		  
		JPanel mainGrid = new JPanel(new GridLayout(7,7));
		for (int i = 0 ; i < 49 ; i++)
		{
			mainGrid.add(new JLabel(""+(i + 1), JLabel.CENTER));
		}
		this.add(mainGrid, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		JPanel infoLine = new JPanel(new GridLayout(1,0));
		
		infoLine.add(new JLabel("Attacker Roll: "));
		infoLine.add(attackRoll);
		infoLine.add(new JLabel("Defender Roll: "));
		infoLine.add(defendRoll);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1,0));
		
		buttonPanel.add(new JLabel("  Player 1  "));
		buttonPanel.add(new JLabel("  Player 2  "));
		buttonPanel.add(new JLabel("  Player 3  "));
		buttonPanel.add(new JLabel("  Player 4  "));
		buttonPanel.add(new JLabel("  Player 5  "));
		buttonPanel.add(new JLabel("  Player 6  "));
		
		
		
		endTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.endTurn();
				
			}
			
		});
		buttonPanel.add(endTurn);
		
		bottomPanel.add(infoLine, BorderLayout.NORTH);
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		this.add(bottomPanel, BorderLayout.SOUTH);
	
		this.setSize(800, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void showPlayerTurn(int player) {
		// TODO Auto-generated method stub
		
	}

	public void showGamePlayable(boolean b) {
		endTurn.setEnabled(b);
		
	}

	public void showAttack(int src, int dest) {
		// TODO Auto-generated method stub
		
	}

	public void showAttackRoll(int a_roll) {
		attackRoll.setText(Integer.toString(a_roll));
		
	}

	public void showDefenseRoll(int d_roll) {
		defendRoll.setText(Integer.toString(d_roll));	
	}
	
	public void clearRolls() {
		attackRoll.setText("");
		defendRoll.setText("");
	}

	public void updateTerritory(int index, Color color, int size) {
		// TODO Auto-generated method stub
		
	}

	public void setListener(UIListener listener)
	{
		this.listener = listener;
	}
	
	public static void main(String args[])
	{
		//TESTING THE GUI
		
		new GUI().setVisible(true);
	}
	
	
}
