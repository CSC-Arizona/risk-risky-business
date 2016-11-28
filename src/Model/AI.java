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
	}// end AI constructor

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
			if (countryToSelect == null)
				countryToSelect = getRandomCountry(countries);
		}
		return countryToSelect;
	}// end pickRandomCountry

	// checks an ai's countries neighbors, to see if they are occupied. if they
	// are, go to the next one, otherwise
	// return that country as a selection. Used for placement in the first turn.
	private Country checkAllNeighbors()
	{
		int i = 0, j = 0;
		// get my first countries neighbors
		ArrayList<Country> neighbors = getCountries().get(i).getNeighbors();
		while (i < neighbors.size())
		{
			j = 0;
			while (j < neighbors.size() && neighbors.get(j).getOccupier() != null)
			{
				j++;
			}

			if (j < neighbors.size())
				return neighbors.get(j);

			i++;
			if (i < getCountries().size())
				neighbors = getCountries().get(i).getNeighbors();
		}
		return null;
	}// end checkAllNeighbors

	private Country getRandomCountry(Country[] countries)
	{
		Random rand = new Random();
		int randNum = rand.nextInt(50);

		return countries[randNum];

	}// end getRandomCountry

	// find my countries that are surrounded by friendly countries, and move
	// their units out
	// public Country reinforceCountry()
	// {
	// Country selectedCountry = null;
	// if(myStrat == AIStrat.EASY)
	// {
	// selectedCountry = pickRandomOwnedCountry();
	// }
	// else
	// {
	// //look for countries I own who's neihbors are not owned by me, and
	// reinforce that one
	// selectedCountry = findCountriesInDanger();
	// if(selectedCountry == null)
	// selectedCountry = pickRandomOwnedCountry();
	// }
	//
	//
	// return selectedCountry;
	// }//end pickRandomCountryFromOccupied

	// return an arraylist of all countries that have neighbors that arent owned
	// by me
	private ArrayList<Country> findFringeCountries()
	{
		ArrayList<Country> fringeCountries = new ArrayList<>();

		int i = 0, j = 0;
		ArrayList<Country> neighbors = getCountries().get(i).getNeighbors();
		while (i < getCountries().size())
		{
			j = 0;
			while (j < neighbors.size())
			{
				if (neighbors.get(j).getOccupier().getFaction().compareTo(this.getFaction()) != 0)
				{
					fringeCountries.add(getCountries().get(i));
					j = neighbors.size();
				}
				j++;
			}
			i++;
			if (i < getCountries().size())
				neighbors = getCountries().get(i).getNeighbors();
		}

		return fringeCountries;
	}

	private Country findCountriesInDanger()
	{
		int i = 0, j = 0;
		// get my first countries neighbors
		// create a function to find a list of all owned countries who's
		// neighbors are not owned by me, and use it instead
		ArrayList<Country> outsideCountries = findFringeCountries();
	
		return null;
	}// end findCountriesInDanger

	private Country pickRandomOwnedCountry()
	{
		Random rand = new Random();
		int randNum = rand.nextInt(getCountries().size());
		return getCountries().get(randNum);
	}// end pickRandomOwnedCountry

	@Override
	public ArrayList<Card> playCards()
	{
		// TODO Auto-generated method stub
		return null;
	}// end playCards

	@Override
	public Country attack()
	{
		// TODO Auto-generated method stub
		return null;
	}// end attack

	@Override
	public void rearrangeTroops()
	{
		// TODO Auto-generated method stub

	}// end rearrangeTroops

	public Country placeNewTroops()
	{
		Country selectedCountry = null;
		if (myStrat == AIStrat.EASY)
		{
			selectedCountry = pickRandomOwnedCountry();
		} else
			selectedCountry = pickRandomFromFringe();

		return selectedCountry;
	}

	private Country pickRandomFromFringe()
	{
		ArrayList<Country> fringeCountries = findFringeCountries();
		Random rand = new Random();
		int randNum = 0;
		randNum = rand.nextInt(fringeCountries.size());
		return fringeCountries.get(randNum);
	}

	private void hardMove()
	{
		// place units on outside countries, then try to
		// take whole continent first, then pick random neighbor, take that
		// continent, etc
		// then try to even out units on border countries, so that they all have
		// close to the same amount at each one

	}// end hardMove

	private void easyMove()
	{
		// place new units in random occupied countries then
		// pick random country, if a neighbor to one of my own countries, try to
		// occupy
		// do this till I cannot attack any longer

	}// end easyMove

	public void setMyStrat(AIStrat strat)
	{
		myStrat = strat;
	}// end setMyStrat

	// creates the ai's menuItem for changing difficulty
	public void makeMenuItem(int i, ActionListener aiDiffChangeListener)
	{
		myDiff = new JMenuItem("AI " + i);
		myDiff.addActionListener(aiDiffChangeListener);
		myDiff.setActionCommand(String.valueOf(i));
	}// end makeMenuItem

	// returns its jMenuItem
	public JMenuItem getMenuItem()
	{
		return myDiff;
	}// end getMenuItem

	// returns the ai's current strategy as a string, used for checking if the
	// ai difficulty menu in the gui was working
	public String getStrat()
	{

		return myStrat.toString();
	}// end getStrat

	public ArrayList<Country> countriesToReinforce()
	{
		ArrayList<Country> selectedCountries = new ArrayList<>();
		if (myStrat == AIStrat.EASY)
		{
			selectedCountries = pickSetOfRandomOwnedCountry();
		} else
			selectedCountries = findFringeCountries();

		return selectedCountries;
	}

	//returns a randomlist of countries to add units to out of the ai's owned countries
	private ArrayList<Country> pickSetOfRandomOwnedCountry()
	{
		ArrayList<Country> countries = new ArrayList<>();
		Random rand = new Random();
		int randNum = 0;
		while(getAvailableTroops() > 0)
		{
			randNum = rand.nextInt(getCountries().size());
			countries.add(getCountries().get(randNum));
		}
		return countries;
	}//end pickSetOfRandomOwnedCountry
}
