/*
 * 	File:		AI.java
 * 	Purpose:	AI class extends player and contains control of AI type players, both easy and hard.
 */

package Model;

import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JMenuItem;

public class AI extends Player implements Serializable {

	private JMenuItem myDiff;
	private Random rand;
	private AIStrategy strategy;
	// private Game theGame;

	public AI(AIStrategy strat, int numOfPlayers) {
		super(numOfPlayers);
		strategy = strat;
		strategy.setMe(this);
		rand = new Random();
	}// end AI constructor

	// checks an ai's countries neighbors, to see if they are occupied. if they
	// are, go to the next one, otherwise
	// return that country as a selection. Used for placement in the first turn.
	public Country checkAllNeighbors() {
		int i = 0, j = 0;
		// get my first countries neighbors
		ArrayList<Country> neighbors = getCountries().get(i).getNeighbors();
		while (i < neighbors.size()) {
			j = 0;
			while (j < neighbors.size() && neighbors.get(j).getOccupier() != null) {
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

	public int chooseMyDiceToRoll(int max) {
		return max;
	}// end chooseMyDice

	public Country pickRandomOwnedCountry() {
		Random rand = new Random();
		int randNum = rand.nextInt(getCountries().size());
		return getCountries().get(randNum);
	}// end pickRandomOwnedCountry

	// returns true if it is finished attacking, and false otherwise
	// grabs a country to attack, and the country that it is attacking from
	// if there is no country to attack, return. if it loses a battle, check if
	// there are still other countries it can attack
	

	public boolean finishedAttacking() {
		int i = 0;
		for(Country country : findFringeCountries())
		{
			//System.out.println("Fringe at " + country.getName() + " with " + country.getForcesVal() + " forces");
			if(country.getForcesVal() == 1)
				i++;
		}
		
		if(i == findFringeCountries().size())
			return true;
		else if(strategy instanceof MediumAI || strategy instanceof HardAI)
		{
			if(strategy.findCountriesToAttack() == null)
				return true;
		}
		
		return false;

	}// end finishedAttacking

	// creates the ai's menuItem for changing difficulty
	public void makeMenuItem(int i, ActionListener aiDiffChangeListener) {
		myDiff = new JMenuItem("AI " + i);
		myDiff.addActionListener(aiDiffChangeListener);
		myDiff.setActionCommand(String.valueOf(i));
	}// end makeMenuItem

	// returns its jMenuItem
	public JMenuItem getMenuItem() {
		return myDiff;
	}// end getMenuItem

	// starts at first country, checks if it is surrounded by friendlies, if it
	// is
	// moves all of its units except for one to its neighbors

	public Country pickRandomFromFringe() {
		ArrayList<Country> fringeCountries = findFringeCountries();

		int randNum = 0;
		// System.out.println(fringeCountries.size() + " size of list to choose
		// from");
		if (fringeCountries.size() == 0)
			return null;
		randNum = rand.nextInt(fringeCountries.size());
		return fringeCountries.get(randNum);
	}

	public Country pickRandomCountry() {
		Map map = Map.getInstance(0);
		Country[] countries = map.getCountries();
		int randNum = rand.nextInt(50);
		return countries[randNum];
	}

	public ArrayList<Country> findFringeCountries() {
		ArrayList<Country> fringeCountries = new ArrayList<>();
 
		int i = 0, j = 0;
		ArrayList<Country> neighbors = getCountries().get(i).getNeighbors();
		while (i < getCountries().size()) {
			j = 0;
			while (j < neighbors.size()) {

				if (!this.equals(neighbors.get(j).getOccupier())) {
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

	@Override
	public ArrayList<Card> redeemCards() {
		if (getCards().size() == 5) {
			return findmyCardsToRedeem();
		} // end if
		else
			return null;
	}

	private ArrayList<Card> findmyCardsToRedeem() {

		ArrayList<Card> myThreeCards = new ArrayList<>();
		int infantryCount = 0, calvaryCount = 0, artilleryCount = 0, wildCount = 0;
		// step through 5 cards, and count how many of each
		for (Card card : getCards()) {
			switch (card.getUnit()) {
			case "infantry":
				infantryCount++;
				break;
			case "calvary":
				calvaryCount++;
				break;
			case "artillery":
				artilleryCount++;
				break;
			case "WILD":
				wildCount++;
				break;
			}
		}
		if (infantryCount >= 3 || (wildCount == 1 && infantryCount >= 2) || (wildCount == 2 && infantryCount >= 1)) {
			myThreeCards = findThreeInfantry();
		} else if (calvaryCount >= 3 || (wildCount == 1 && calvaryCount >= 2)
				|| (wildCount == 2 && calvaryCount >= 1)) {
			myThreeCards = findThreeCalvary();
		} else if (artilleryCount >= 3 || (wildCount == 1 && calvaryCount >= 2)
				|| (wildCount == 2 && artilleryCount >= 1)) {
			myThreeCards = findThreeArtillery();
		} else {
			myThreeCards = findOneOfEach();
		}

		return myThreeCards;
	}// end findmyCardsToRedeem

	private ArrayList<Card> findOneOfEach() {
		ArrayList<Card> myThreeCards = new ArrayList<>();
		boolean infantry = false, calvary = false, artillery = false;
		for (Card card : getCards()) {
			if (!infantry && (card.getUnit().compareTo("infantry") == 0 || card.getUnit().compareTo("WILD") == 0)) {
				myThreeCards.add(card);
				infantry = true;
			}

			if (!calvary && (card.getUnit().compareTo("calvary") == 0 || card.getUnit().compareTo("WILD") == 0)) {
				myThreeCards.add(card); 
				calvary = true;
			}

			if (!artillery && (card.getUnit().compareTo("artillery") == 0 || card.getUnit().compareTo("WILD") == 0)) {
				myThreeCards.add(card);
				artillery = true;
			}
		}
		return myThreeCards;
	}// end findOneOfEach

	private ArrayList<Card> findThreeArtillery() {
		ArrayList<Card> threeArtillery = new ArrayList<>();
		for (Card card : getCards()) {
			if (card.getUnit().compareTo("artillery") == 0) {
				threeArtillery.add(card);
			}

			if (threeArtillery.size() == 3) {
				break;
			}
		}
		return threeArtillery;
	}// end findThreeArtillery

	private ArrayList<Card> findThreeCalvary() {
		ArrayList<Card> threeCalvary = new ArrayList<>();
		for (Card card : getCards()) {
			if (card.getUnit().compareTo("calvary") == 0) {
				threeCalvary.add(card);
			}

			if (threeCalvary.size() == 3) {
				break;
			}
		}
		return threeCalvary;
	}// end findThreeCalvary

	private ArrayList<Card> findThreeInfantry() {

		ArrayList<Card> threeInfantry = new ArrayList<>();
		for (Card card : getCards()) {
			if (card.getUnit().compareTo("infantry") == 0 || card.getUnit().compareTo("WILD") == 0) {
				threeInfantry.add(card);
			}

			if (threeInfantry.size() == 3) {
				break;
			}
		}
		return threeInfantry;
	}// end findThreeInfantry

	public AIStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(AIStrategy strat) {
		strategy = strat;
		strategy.setMe(this);
	}

}
