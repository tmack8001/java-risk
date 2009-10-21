package javaRisk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class GUI extends JFrame {

	private UIListener listener;
	
	private JButton endTurn = new JButton("End Turn");
	private JLabel attackRoll = new JLabel();
	private JLabel defendRoll = new JLabel();
	
	private JLabel[] gridTiles;
	private JLabel[] playerTitles;

	public GUI()
	{
		super("Risk");
		this.setLayout(new BorderLayout());
		  
		JPanel mainGrid = new JPanel(new GridLayout(7,7));
		gridTiles = new JLabel[49];
		for (int i = 0 ; i < 49 ; i++)
		{
			gridTiles[i] = new JLabel(Integer.toString(i+1), JLabel.CENTER);
			gridTiles[i].setOpaque(true);
			gridTiles[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
				}
			});
			
			mainGrid.add(gridTiles[i]);
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
		
		playerTitles = new JLabel[6];
		
		for (int i = 0 ; i < playerTitles.length ; i++)
		{
		
			playerTitles[i] = new JLabel("Player " + (i+1));
			buttonPanel.add(playerTitles[i]);
			
		}
		
		
		endTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					listener.endTurn();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
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
		for (int i = 0; i < playerTitles.length ; i++)
		{
			JLabel j = playerTitles[i];
			j.setText("Player " + (i+1));
		}
		playerTitles[player].setText(">> " + playerTitles[player].getText() + " <<");
		
	}

	public void showGamePlayable(boolean b) {
		endTurn.setEnabled(b);
		
	}

	public void showAttack(int src, int dest) {
		gridTiles[src].setBorder(new BevelBorder(BevelBorder.RAISED));
		gridTiles[dest].setBorder(new LineBorder(gridTiles[src].getBackground(), 10));
		
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
		gridTiles[index].setBackground(color);
		gridTiles[index].setText(Integer.toString(size));
		
	}

	public void setListener(UIListener listener)
	{
		this.listener = listener;
	}
	
	public static void main(String args[]) throws InterruptedException
	{
		//TESTING THE GUI
		
		GUI g = new GUI();
		g.setVisible(true);
		java.util.Random r = new java.util.Random();
		for (int i = 0 ; i < 19000  ; i++)
		{
			//Thread.sleep(50);
			Color col = Color.decode(Integer.toString(r.nextInt(16777215)));
			g.updateTerritory(r.nextInt(49), col, r.nextInt(8)+1);
		}
		
		g.showAttack(19,20);
		
		int me = r.nextInt(6);
		
		while (true)
		{
			for (int i = 0 ; i < 6 ; i++)
			{
				g.showPlayerTurn(i);
				g.showGamePlayable(i == me);
				Thread.sleep(1000);
			}
		}
	
	}
	
	
}
