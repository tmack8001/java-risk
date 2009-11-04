package javaRisk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

/**
 * The GUI for the Risk game.
 * The layout is a window with a large grid of colored squares, 
 * each representing a territory.
 * At the bottom is a row of usernames and a button to end the user's turn.
 * @author Administrator
 *
 */
public class RiskGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * The UIListener.
	 */
	private UIListener listener;
	
	/**
	 * Button to end the user's turn.
	 */
	private JButton endTurn = new JButton("End Turn");
	
	/**
	 * Label used to display the attack roll of the current attack.
	 */
	private JLabel attackRoll = new JLabel();
	
	/**
	 * Label used to display the defense roll of the current attack.
	 */
	private JLabel defendRoll = new JLabel();
	
	/**
	 * Toggle for whether or not it's this user's turn.
	 */
	private boolean myTurn = false;
	
	/**
	 * Represents the grid of territories.
	 */
	private JLabel[] gridTiles;
	
	/**
	 * The user name tiles.
	 */
	private JLabel[] playerTitles;
	
	/**
	 * The panel where the user name tiles and End Turn button go.
	 */
	private JPanel buttonPanel;
	
	/**
	 * Array of the users' names.
	 */
	private String[] names;

	/**
	 * Constructor. Sets up the GUI, the grid of territories, and the event listeners.
	 * The user name panels are not yet set.
	 */
	public RiskGUI()
	{
		super("Risk");
		this.setLayout(new BorderLayout());
	
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					listener.surrender();
				} catch (IOException ex) {}
			}
		});
		
		JPanel mainGrid = new JPanel(new GridLayout(Constants.ROW_SIZE,Constants.COL_SIZE));
		gridTiles = new JLabel[Constants.ROW_SIZE*Constants.COL_SIZE];
		for (int i = 0 ; i < Constants.ROW_SIZE*Constants.COL_SIZE ; i++)
		{
			
			gridTiles[i] = new JLabel(Integer.toString(i+1), JLabel.CENTER);
			gridTiles[i].setOpaque(true);
			//gridTiles[i].setBorder(new BevelBorder(BevelBorder.LOWERED));
			
			final int internal_i = i;
			gridTiles[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (isPlayable())
					{
						try {
							listener.clicked(internal_i);	
						} catch (IOException ex) {
							// do nothing
						}
					}
				}
			});
			
			mainGrid.add(gridTiles[i]);
		}
		this.add(mainGrid, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		JPanel infoLine = new JPanel(new GridLayout(1,0));
		JLabel ar = new JLabel("Attacker Roll: ");
		JLabel dr = new JLabel("Defender Roll: ");
		
		Font f = new Font("Arial", Font.PLAIN, 24);
		
		ar.setFont(f);
		dr.setFont(f);
		attackRoll.setFont(f);
		defendRoll.setFont(f);
		
		infoLine.add(ar);
		infoLine.add(attackRoll);
		infoLine.add(dr);
		infoLine.add(defendRoll);
		
		buttonPanel = new JPanel(new GridLayout(1,0));
		
		
		playerTitles = new JLabel[0];
		
		endTurn.setEnabled(false);
		endTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					listener.endTurn();
				} catch (IOException e1) {
					// do nothing
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

	/**
	 * Return whether or not it's this player's turn.
	 * @return true if it's this user's turn
	 */
	public boolean isPlayable()
	{
		return myTurn;
	}
	
	/**
	 * Update the name tiles, signaling whose turn it is with >> <<.
	 * Also refreshes the number of territories.
	 * 
	 * @param player - the player whose turn it is
	 */
	public void showPlayerTurn(int player) {
		for (int i = 0 ; i < playerTitles.length ; i++)
		{
			playerTitles[i].setText(names[i] + "(" + listener.requestPlayerCount(i) + ")");
		}
		playerTitles[player].setText(">> " + playerTitles[player].getText()+ " <<");		
	}

	/**
	 * Toggle for whether or not the user can play.
	 * @param b - true if this user's turn
	 */
	public void showGamePlayable(boolean b) {
		endTurn.setEnabled(b);
		myTurn = b;
	}

	/**
	 * Method to indicate an attack from one territory to another.
	 * The implementation is that the territories are outlined in their
	 * original colors, but their colors are changed to black for
	 * 500 milliseconds, then their original colors are restored.
	 * At this point the server will have sent back updates.
	 * @param src - attacking territory
	 * @param dest - defending territory
	 */
	public void showAttack(int src, int dest) {
		JLabel srcT = gridTiles[src];
		JLabel destT = gridTiles[dest];
		
		Color oldSrc = srcT.getBackground();
		Color oldDest = destT.getBackground();
		
		srcT.setBackground(Color.black);
		destT.setBackground(Color.BLACK);
		
		srcT.setBorder(new LineBorder(oldSrc, 3));
		destT.setBorder(new LineBorder(oldDest, 3));
		
		try {Thread.sleep(500);} catch (InterruptedException e){}
	
		reset();
		srcT.setBackground(oldSrc);
		destT.setBackground(oldDest);
	}

	/**
	 * Set the value of the Attack Roll.
	 * @param a_roll - the attacking roll
	 */
	public void showAttackRoll(int a_roll) {
		attackRoll.setText(Integer.toString(a_roll));
	}

	/**
	 * Set the value of the Defense Roll.
	 * @param d_roll - the defense roll
	 */
	public void showDefenseRoll(int d_roll) {
		defendRoll.setText(Integer.toString(d_roll));	
	}
	
	/**
	 * Reset all highlighting and rolls.
	 */
	public void reset()
	{
		clearRolls();
		for (JLabel gridItem : gridTiles)
		{
			gridItem.setBorder(null);
		}
	}
	
	/**
	 * Clear the displayed Attack and Defense rolls.
	 */
	public void clearRolls() {
		attackRoll.setText("");
		defendRoll.setText("");
	}

	/**
	 * Set the information for a territory.
	 * @param index - territory index
	 * @param color - Color to set
	 * @param size - army size on the territory
	 */
	public void updateTerritory(int index, Color color, int size) {
		gridTiles[index].setBackground(color);
		gridTiles[index].setText(Integer.toString(size));
	}
	
	/**
	 * Visibly highlight a territory.
	 * The implementation is that the territory is raised.
	 * @param territory - territory index
	 */
	public void highlight(int territory){
		gridTiles[territory].setBorder(new BevelBorder(BevelBorder.RAISED));
	}

	/**
	 * Set the associated UIListener.
	 * @param listener UIListener
	 */
	public void setListener(UIListener listener)
	{
		this.listener = listener;
	}
	
	/**
	 * Set the names of the users playing the game.
	 * This adds their names to the panel at the bottom of the UI.
	 * @param names - array of Strings
	 */
	public void setNames(String[] names)
	{
		this.names = names;
		playerTitles = new JLabel[names.length];
		
		buttonPanel.removeAll();
		for (int i = 0 ; i < playerTitles.length ; i++)
		{
			playerTitles[i] = new JLabel(names[i]);
			playerTitles[i].setForeground(listener.requestPlayerColor(i));
			buttonPanel.add(playerTitles[i]);
			
		}
		buttonPanel.add(endTurn);
		
	}

	/**
	 * Show message dialog when the user wins.
	 */
	public void youWin() {
		JOptionPane.showMessageDialog(null, "You win !!!");
	}
	
	/**
	 * Show message dialog when the user does not win.
	 */
	public void youLose() {
		JOptionPane.showMessageDialog(null, "You lose :(");
	}
		
}
