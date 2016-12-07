package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Model.*;

public class StatPanel extends JPanel{

	private Game theGame;
	private Player currPlayer;
	private CurrentPlayerStatsPanel currPanel;
	private AllPlayerStatsPanel allPanel;
	private ArrayList<Player> allPlayers;
	private Font headerFont;
	private Font bodyFont;
	
	
	public StatPanel(Game theGame, Font gotFont, Font otherFont){
		super();
		this.theGame = theGame;
		headerFont = gotFont;
		bodyFont = otherFont;
		//It's a border layout
		this.setLayout(new BorderLayout());
	}
	
	
	/*
	 * Left half of the gui
	 */
	
	
	/*
	 * Anytime you make this view, a new currentplayerstats
	 * panel needs to be made to reflect the current player
	 */
	private class CurrentPlayerStatsPanel extends JPanel{
		
		private CurrentPlayerStatsPanel(){
			super();
			currPlayer = theGame.getCurrentPlayer();
			this.setLayout(new GridLayout(2,0));
			this.add(addNameAndCardsPanel());
			this.add(addPlayerCountriesPanel());
		}//end constructor
	}//end currentplayerstats
	
	
	private JPanel addNameAndCardsPanel(){
		JPanel myStuff = new JPanel();
		myStuff.setLayout(new GridLayout(0,2));
		
		//Drawing the name and factionr
		JPanel name = new JPanel();
		name.setLayout(new GridLayout(0,2));
		JLabel nameLabel = new JLabel(currPlayer.getName());
		nameLabel.setFont(headerFont);
		JLabel factLabel = new JLabel(currPlayer.getFaction().getName());
		factLabel.setFont(headerFont);
		name.add(nameLabel);
		name.add(factLabel);
		myStuff.add(name);
		
		//Drawing the cards
		
		return myStuff;
	}
	
	
	private JPanel addPlayerCountriesPanel(){
		//build one panel for all countries
		ArrayList<Country> currCountries = currPlayer.getCountries();
		int numRows = 10;
		int numCols = currCountries.size()/numRows;
		//If there are leftovers, add one more column
		if (currCountries.size() % numRows != 0)
			numCols++;
		
		JPanel countPanel = new JPanel();
		countPanel.setLayout(new GridLayout(numCols, numRows));
		
		//Make labels for all countries and add them
		for (int i=0; i < currCountries.size(); i++){
			JLabel countryLabel = new JLabel(currCountries.get(i).getName());
			countryLabel.setFont(bodyFont);
			countPanel.add(countryLabel);
		}
		return countPanel;
	}//end addPlayerCountriesPanel
	
	
	
	
	/*
	 * Begin Right Half of GUI
	 */
	
	
	private class AllPlayerStatsPanel extends JPanel{
		
		
		private AllPlayerStatsPanel(){
			super();
			allPlayers = theGame.getPlayers();
		}//end constructor
		
		
	}//end allplayerstats
	
	
	
	
	
	

	
	
	

}
