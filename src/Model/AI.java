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
	}//end AI constructor

	// get a random number between from 0 and 49
	// return that country in the array at index randomNumber
	public Country pickRandomCountry(Country[] countries)
	{
		Country countryToSelect = null;

		if (myStrat == AIStrat.EASY || getCountries().size() == 0)
		{
			countryToSelect = getRandomCountry(countries);
		} else
		{
			countryToSelect = checkAllNeighbors();
			if(countryToSelect == null)
				countryToSelect = getRandomCountry(countries);
		}
		return countryToSelect;
	}// end pickRandomCountry

	//checks an ai's countries neighbors, to see if they are occupied. if they are, go to the next one, otherwise
	// return that country as a selection
	private Country checkAllNeighbors()
	{
		int i = 0, j = 0;
		//get my first countries neighbors
		ArrayList<Country> neighbors = getCountries().get(i).getNeighbors();
		while (i < neighbors.size())
		{
			j = 0;
			while (j < neighbors.size() && neighbors.get(j).getOccupier() != null)
			{
				j++;
			}
			
			if(j < neighbors.size())
				return neighbors.get(j);
			
			i++;
			if(i < neighbors.size())
				neighbors = getCountries().get(i).getNeighbors();
		}
		return null;
	}//end checkAllNeighbors

	private Country getRandomCountry(Country[] countries)
	{
		Random rand = new Random();
		int randNum = rand.nextInt(50);

		return countries[randNum];

	}// end getRandomCountry

	public Country reinforceCountry()
	{
		Country selectedCountry = null;
		if(myStrat == AIStrat.EASY)
		{
			selectedCountry = pickRandomOwnedCountry();
		}
		else
		{
			//look for countries I own who's neihbors are not owned by me, and reinforce that one
			selectedCountry = findCountriesInDanger();
			if(selectedCountry == null)
				selectedCountry = pickRandomOwnedCountry();
		}
		

		return selectedCountry;
	}//end pickRandomCountryFromOccupied

	private Country findCountriesInDanger()
	{
		int i = 0, j = 0;
		//get my first countries neighbors
		ArrayList<Country> neighbors = getCountries().get(i).getNeighbors();
		while (i < neighbors.size())
		{
			j = 0;
			while (j < neighbors.size() && neighbors.get(j).getOccupier() != this)
			{
				j++;
			}
			
			if(j < neighbors.size())
				return neighbors.get(j);
			
			i++;
			if(i < neighbors.size())
				neighbors = getCountries().get(i).getNeighbors();
		}
		return null;
	}//end findCountriesInDanger

	private Country pickRandomOwnedCountry()
	{
		Random rand = new Random();
		int randNum = rand.nextInt(getCountries().size());
		return getCountries().get(randNum);
	}//end pickRandomOwnedCountry

	@Override
	public ArrayList<Card> playCards()
	{
		// TODO Auto-generated method stub
		return null;
	}//end playCards

	@Override
	public Country attack()
	{
		// TODO Auto-generated method stub
		return null;
	}//end attack

	@Override
	public void rearrangeTroops()
	{
		// TODO Auto-generated method stub

	}//end rearrangeTroops

	public void myTurn()
	{
		placeNewTroops();
		attack();
		reinforceCountry();
		
	}//end myTurn

	private void placeNewTroops(){
		if(myStrat == AIStrat.EASY)
		{
			pickRandomOwnedCountry();
		}
	}

	private void hardMove()
	{
		// place units on outside countries, then try to
		// take whole continent first, then pick random neighbor, take that
		// continent, etc
		// then try to even out units on border countries, so that they all have
		// close to the same amount at each one

	}//end hardMove

	private void easyMove()
	{
		// place new units in random occupied countries then
		// pick random country, if a neighbor to one of my own countries, try to
		// occupy
		// do this till I cannot attack any longer

	}//end easyMove

	public void setMyStrat(AIStrat strat)
	{
		myStrat = strat;
	}//end setMyStrat

	// creates the ai's menuItem for changing difficulty
	public void makeMenuItem(int i, ActionListener aiDiffChangeListener)
	{
		myDiff = new JMenuItem("AI " + i);
		myDiff.addActionListener(aiDiffChangeListener);
		myDiff.setActionCommand(String.valueOf(i));
	}//end makeMenuItem

	// returns its jMenuItem
	public JMenuItem getMenuItem()
	{
		return myDiff;
	}//end getMenuItem

	// returns the ai's current strategy as a string, used for checking if the
	// ai difficulty menu in the gui was working
	public String getStrat()
	{

		return myStrat.toString();
	}//end getStrat
}
