package Model;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JMenuItem;

public class AI extends Player {

	private JMenuItem myDiff;
	private AIStrat myStrat;
	public AI(AIStrat strat, int numOfPlayers)
	{
		super(numOfPlayers);
		myStrat = strat;
	}
	
	//get a random number between from 0 and 49
	//return that country in the array at index randomNumber
	public Country pickRandomCountry(Country[] countries)
	{
		Random rand = new Random();
		int randNum = rand.nextInt(50);
		return countries[randNum];
	}
	
	public Country pickRandomCountryFromOccupied()
	{
		Random rand = new Random();
		int randNum = rand.nextInt(getCountries().size());
		
		return getCountries().get(randNum);
	}
	@Override
	public ArrayList<Card> playCards()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Country attack()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rearrangeTroops()
	{
		// TODO Auto-generated method stub

	}
	
	public void myTurn()
	{
		if(myStrat == AIStrat.EASY)
			easyMove();
		else
			hardMove();
	}

	private void hardMove()
	{
		//place units on outside countries, then try to 
		//take whole continent first, then pick random neighbor, take that continent, etc
		//then try to even out units on border countries, so that they all have close to the same amount at each one
		
	}

	private void easyMove()
	{
		//place new units in random occupied countries then
		//pick random country, if a neighbor to one of my own countries, try to occupy
		//do this till I cannot attack any longer
		
	}
	
	public void setMyStrat(AIStrat strat)
	{
		myStrat = strat;
	}

	//creates the ai's menuItem for changing difficulty
	public void makeMenuItem(int i, ActionListener aiDiffChangeListener)
	{
		myDiff = new JMenuItem("AI " + i );
		myDiff.addActionListener(aiDiffChangeListener);
		myDiff.setActionCommand(String.valueOf(i));
	}
	
	//returns its jMenuItem
	public JMenuItem getMenuItem()
	{
		return myDiff;
	}

	//returns the ai's current strategy as a string, used for checking if the ai difficulty menu in the gui was working
	public String getStrat()
	{
		
		return myStrat.toString();
	}
}
