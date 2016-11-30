package Model;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JMenuItem;

public class AI extends Player {

	private JMenuItem myDiff;
	private AIStrat myStrat;
	private Deck deck;
	private int redemptions;
	// private Game theGame;

	public AI(AIStrat strat, int numOfPlayers) {
		super(numOfPlayers);
		myStrat = strat;
		deck = Deck.getInstance();
		redemptions=0;
	}// end AI constructor

	// get a random number between from 0 and 49
	// return that country in the array at index randomNumber
	public Country pickRandomCountry(Country[] countries) {
		Country countryToSelect = null;

		if (myStrat == AIStrat.EASY || getCountries().size() == 0) {
			countryToSelect = getRandomCountry(countries);
		} else {
			countryToSelect = checkAllNeighbors();
			if (countryToSelect == null)
				countryToSelect = getRandomCountry(countries);
		}
		return countryToSelect;
	}// end pickRandomCountry

	// checks an ai's countries neighbors, to see if they are occupied. if they
	// are, go to the next one, otherwise
	// return that country as a selection. Used for placement in the first turn.
	private Country checkAllNeighbors() {
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

	private Country getRandomCountry(Country[] countries) {
		Random rand = new Random();
		int randNum = rand.nextInt(50);

		return countries[randNum];

	}// end getRandomCountry

	// return an arraylist of all countries that have neighbors that arent owned
	// by me
	private ArrayList<Country> findFringeCountries() {
		ArrayList<Country> fringeCountries = new ArrayList<>();

		int i = 0, j = 0;
		ArrayList<Country> neighbors = getCountries().get(i).getNeighbors();
		while (i < getCountries().size()) {
			j = 0;
			while (j < neighbors.size()) {
				if (neighbors.get(j).getOccupier().getFaction().compareTo(this.getFaction()) != 0) {
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
	}// end findFringeCountries

	private Country pickRandomOwnedCountry() {
		Random rand = new Random();
		int randNum = rand.nextInt(getCountries().size());
		return getCountries().get(randNum);
	}// end pickRandomOwnedCountry

	public Country placeNewTroops() {
		Country selectedCountry = null;
		if (myStrat == AIStrat.EASY) {
			selectedCountry = pickRandomOwnedCountry();
		} else {
			selectedCountry = pickRandomFromFringe();
			if (selectedCountry == null)
				selectedCountry = pickRandomOwnedCountry();

		}
		return selectedCountry;
	}

	private Country pickRandomFromFringe() {
		ArrayList<Country> fringeCountries = findFringeCountries();

		Random rand = new Random();
		int randNum = 0;
		System.out.println(fringeCountries.size() + " size of list to choose from");
		if (fringeCountries.size() == 0)
			return null;
		randNum = rand.nextInt(fringeCountries.size());
		return fringeCountries.get(randNum);
	}

	// returns true if it is finished attacking, and false otherwise
	// grabs a country to attack, and the country that it is attacking from
	// if there is no country to attack, return. if it loses a battle, check if
	// there are still other countries it can attack
	public boolean aiAttack() {
		Country attacking = getCountryToAttack();
		if (attacking == null)
			return true;
		Country attackingFrom = findAttackingCountry(attacking);

		// change this for dice roll later, but for now, just take over
		if (attackingFrom.getForcesVal() - 1 > attacking.getForcesVal()) {
			int oldForces = attacking.getForcesVal();
			attacking.getOccupier().loseCountry(attacking);
			attacking.removeUnits(oldForces);
			attacking.setForcesVal(attackingFrom.getForcesVal() - 1);
			attacking.setOccupier(this);
			System.out.println(this.getName() + " Took " + attacking.getName());
			attackingFrom.removeUnits(attackingFrom.getForcesVal() - 1);
			return false;
		}

		return true;

	}// end aiAttack

	private Country findAttackingCountry(Country attacking) {

		// System.out.println("find attacking");
		for (Country c1 : findFringeCountries()) {
			for (Country c2 : c1.getNeighbors()) {
				if (c2.equals(attacking)) {
					return c1;
				}
			}
		}
		return null;
	}

	// returns a country it can attack
	private Country getCountryToAttack() {
		// System.out.println("get country to attack");
		Country attackMe = pickRandomFromList(findCountriesToAttack());
		return attackMe;
	}// end
		// getCountryToAttack

	// picks a random country from the list of countries to attack
	private Country pickRandomFromList(ArrayList<Country> countriesToAttack) {
		if (countriesToAttack == null)
			return null;

		Random rand = new Random();
		int randInt = 0;

		randInt = rand.nextInt(countriesToAttack.size());

		return countriesToAttack.get(randInt);
	}// end pickRandomFromList

	public void setMyStrat(AIStrat strat) {
		myStrat = strat;
	}// end setMyStrat

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

	// returns the ai's current strategy as a string, used for checking if the
	// ai difficulty menu in the gui was working
	public String getStrat() {

		return myStrat.toString();
	}// end getStrat

	public ArrayList<Country> countriesToReinforce() {
		ArrayList<Country> selectedCountries = new ArrayList<>();
		if (myStrat == AIStrat.EASY) {
			selectedCountries = pickSetOfRandomOwnedCountry();
		} else
			selectedCountries = findFringeCountries();

		return selectedCountries;
	}

	// returns a randomlist of countries to add units to out of the ai's owned
	// countries
	private ArrayList<Country> pickSetOfRandomOwnedCountry() {
		ArrayList<Country> countries = new ArrayList<>();
		Random rand = new Random();
		int randNum = 0;
		while (getAvailableTroops() > 0) {
			randNum = rand.nextInt(getCountries().size());
			countries.add(getCountries().get(randNum));
		}
		return countries;
	}// end pickSetOfRandomOwnedCountry

	// gets all fringe countries, then for each neihbor that fringe country has,
	// if it isn't owned by me
	// check if i have more units on my country than that country, if I do, add
	// that country to my list of countriesWorthAttacking
	private ArrayList<Country> findCountriesToAttack() {
		// System.out.println("find countries to attack");
		ArrayList<Country> fringeCountries = findFringeCountries();
		ArrayList<Country> countriesWorthAttacking = new ArrayList<>();
		for (Country country : fringeCountries) {
			ArrayList<Country> neighbors = country.getNeighbors();
			for (Country neighboringCountry : neighbors) {
				if (neighboringCountry.getOccupier().getFaction().compareTo(this.getFaction()) != 0) {
					if (country.getForcesVal() - 1 > neighboringCountry.getForcesVal())
						countriesWorthAttacking.add(neighboringCountry);
				} // end if
			} // end for
		} // end for

		if (countriesWorthAttacking.size() == 0)
			return null;

		return countriesWorthAttacking;
	}// end findCountriesToAttack

	// starts at first country, checks if it is surrounded by friendlies, if it
	// is
	// moves all of its units except for one to its neighbors
	public void reinforce() {

		int surroundCounter = 0;
		if (myStrat == AIStrat.HARD) {
			for (Country country : getCountries()) {

				surroundCounter = 0;
				ArrayList<Country> neighbors = country.getNeighbors();
				for (Country neighbor : neighbors) {
					if (neighbor.getOccupier().equals(this))
						surroundCounter++;
				}

				if (surroundCounter == neighbors.size() && country.getForcesVal() > 1) {
					while (country.getForcesVal() > 1) {
						for (Country neighbor : neighbors) {
							country.removeUnits(1);
							neighbor.setForcesVal(1);
							if (country.getForcesVal() == 1)
								break;
						}
					}

				}
			}
		}

	}// end reinforce
		// moves units to other countries if it has more than 2 units occupying

	public void reinforce2() {

		if (myStrat == AIStrat.HARD) {
			for (Country country : getCountries()) {
				if (country.getForcesVal() > 2) {
					while (country.getForcesVal() > 2) {
						for (Country neighbor : country.getNeighbors()) {
							if (neighbor.getOccupier().equals(this)) {
								neighbor.setForcesVal(1);
								country.removeUnits(1);
							}
							if (country.getForcesVal() == 2)
								break;
						}
					}
				}
			}
		}
	}// end reinforce2
	
	public void setRedemptions(int num){
		redemptions = num;
	}

	@Override
	public int redeemCards() {
		if (getCards().size() == 5) {
			ArrayList<Card> myCardsToRedeem = findmyCardsToRedeem();
			// TODO turn in cards
			// TODO discardCards to deck
			int numArmies = -1;

			Card one = myCardsToRedeem.get(0);
			Card two = myCardsToRedeem.get(1);
			Card three = myCardsToRedeem.get(2);

			// redeemable: three of the same unit type, one of each type, two + wild
			// if can redeem:
			if ((one.getUnit().compareTo(two.getUnit()) == 0 && one.getUnit().compareTo(three.getUnit()) == 0
					&& three.getUnit().compareTo(two.getUnit()) == 0)
					|| (one.getUnit().compareTo(two.getUnit()) != 0 && one.getUnit().compareTo(three.getUnit()) != 0
							&& three.getUnit().compareTo(two.getUnit()) != 0)
					|| (one.getUnit().compareTo("WILD") == 0 )
					|| (two.getUnit().compareTo("WILD") == 0 )
					|| (three.getUnit().compareTo("WILD") == 0)) {

				numArmies = 0;
				redemptions++;
				switch (redemptions) {
				case 1:
					numArmies = 4;
					break;
				case 2:
					numArmies = 6;
					break;
				case 3:
					numArmies = 8;
					break;
				case 4:
					numArmies = 10;
					break;
				case 5:
					numArmies = 12;
					break;
				case 6:
					numArmies = 15;
					break;
				default:
					numArmies = 15 + 5 * (redemptions - 6);
					break;
				}

				// if any one of the redeemable cards contains a country that the
				// player has, add 2 armies to that country.
				boolean added = false;
				for (Card c : myCardsToRedeem) {
					for (Country t : this.getCountries()) {
						if (c.getCountry().compareTo(t.getName()) == 0) {
							// add 2 armies to that country
							added = true;
							int currentForces = t.getForcesVal();
							System.out.println("current Forces" + currentForces + t.getName());
							t.setForcesVal(2);
							System.out.println("updated Forces" + t.getForcesVal() + t.getName());
							break; //can only redeem a country card for extra armies once per turn
						}
					}
					if(added)
						break;
				}
				if (!added)
					System.out.println("no country cards to redeem");
			} else
				System.out.println("unable to redeem cards");
			
			this.discardCards(myCardsToRedeem);
			deck.addToDiscardPile(myCardsToRedeem);
			return numArmies;
		}
		return 0;
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
		} else if (calvaryCount >= 3 || (wildCount == 1 && calvaryCount >= 2) || (wildCount == 2 && calvaryCount >= 1)) {
			myThreeCards = findThreeCalvary();
		} else if (artilleryCount >= 3 || (wildCount == 1 && calvaryCount >= 2) || (wildCount == 2 && artilleryCount >= 1)) {
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
}
