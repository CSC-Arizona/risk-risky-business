package Model;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JMenuItem;

public class AI extends Player {

	private JMenuItem myDiff;
	private AIStrat myStrat;
	// private Game theGame;

	public AI(AIStrat strat, int numOfPlayers) {
		super(numOfPlayers);
		myStrat = strat;

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
		} else
			selectedCountry = pickRandomFromFringe();

		return selectedCountry;
	}

	private Country pickRandomFromFringe() {
		ArrayList<Country> fringeCountries = findFringeCountries();
		Random rand = new Random();
		int randNum = 0;
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
			System.out.println(this.getFaction() + " Took " + attacking.getName());
			attackingFrom.removeUnits(attackingFrom.getForcesVal() - 1);
			return false;
		}

		return true;

	}// end aiAttack

	private Country findAttackingCountry(Country attacking) {

		//System.out.println("find attacking");
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
		//System.out.println("get country to attack");
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
		//System.out.println("find countries to attack");
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
}
