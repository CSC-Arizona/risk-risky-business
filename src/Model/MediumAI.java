package Model;

/*
 * MediumAI
 * 
 * by Abigail Dodd, Sydney Komro, Dylan Tobia, Jewell Finder
 * 
 * Creates a medium strategy for AI players that controls the way that they
 * place units, fight, and reinforce 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class MediumAI implements AIStrategy, Serializable {

	private AI me;

	// Constructors
	public MediumAI() {
	};

	public MediumAI(AI ai) {
		me = ai;
	}

	/*
	 * reinforce for each country I own, if I have more than 5 units on it, move
	 * them to friendly neighbors until I only have 5 left.
	 */
	@Override
	public String reinforce() {
		String log = "";
		int numRes = 0;
		boolean stopFlag = false;
		for (Country country : me.getCountries()) {
			if (country.getForcesVal() > 5) {
				stopFlag = false;
				while (country.getForcesVal() > 2 && stopFlag == false) {
					int i = 0;
					for (Country neighbor : country.getNeighbors()) {
						if (neighbor.getOccupier().equals(me)) {
							numRes++;
							neighbor.addForcesVal(2);
							country.removeUnits(2);
							log += me.getName() + " removed 2 units from "
									+ country.getName()
									+ " and placed them on "
									+ neighbor.getName() + ".\n";
						}

						if (numRes == 5)
							return log;

						if (country.getForcesVal() == 2)
							break;

						i++;
						if (i >= country.getNeighbors().size()) {
							stopFlag = true;
						}
					}
				}
			}
		}

		return null;
	}

	/*
	 * Places units on random fringe countries
	 */
	@Override
	public ArrayList<Country> placeNewTroops() {
		ArrayList<Country> countries = new ArrayList<>();
		ArrayList<Country> fringes = me.findFringeCountries();
		int randNum = 0;
		int i = 0;
		while (me.getAvailableTroops() > i) {
			i += 2;
			randNum = rand.nextInt(fringes.size());
			countries.add(fringes.get(randNum));
		}
		return countries;
	}

	@Override
	public void setMe(AI ai) {
		me = ai;
	}

	/*
	 * Places units on random fringe countries
	 */
	@Override
	public Country placeLeftOverUnits() {

		return me.pickRandomFromFringe();
	}

	/*
	 * if i own no contries, pick a random country to start otherwise, look for
	 * neighboring countrie that I do not own, and place units there. If all my
	 * neighbors are occupied, pick a random country.
	 */
	@Override
	public Country placeUnit() {
		Country countryToReturn = null;
		if (me.getCountries() == null || me.getCountries().size() == 0)
			countryToReturn = me.pickRandomCountry();
		else {
			countryToReturn = me.checkAllNeighbors();
			if (countryToReturn == null)
				countryToReturn = me.pickRandomCountry();
		}
		return countryToReturn;
	}

	public String toString() {
		return "(med)";
	}

	/*
	 * returns a list of all neighboring enemy countries and picks a random one
	 */
	@Override
	public Country getCountryToAttack() {

		ArrayList<Country> allNeighboringEnemies = findCountriesToAttack();
		if (allNeighboringEnemies == null)
			return null;

		int randNum = rand.nextInt(allNeighboringEnemies.size());
		return allNeighboringEnemies.get(randNum);
	}

	/*
	 * Finds all fringe countries, then checks their neighbors. If the neighbor
	 * is an enemy, and I have >= the same amount of units on it, add the
	 * neighbor to a lit of countries to attack. If the list ends up being
	 * empty, return null, otherwise return that list.
	 */
	@Override
	public ArrayList<Country> findCountriesToAttack() {
		ArrayList<Country> fringeCountries = me.findFringeCountries();
		ArrayList<Country> countriesWorthAttacking = new ArrayList<>();
		for (Country country : fringeCountries) {
			ArrayList<Country> neighbors = country.getNeighbors();
			for (Country neighboringCountry : neighbors) {
				if (!neighboringCountry.getOccupier().equals(me)
						&& (neighboringCountry.getForcesVal() <= country
								.getForcesVal() && country.getForcesVal() > 1)) {
					countriesWorthAttacking.add(neighboringCountry);
				} // end if
			} // end for
		} // end for

		if (countriesWorthAttacking.size() == 0)
			return null;

		return countriesWorthAttacking;
	}

	/*
	 * Given a country moveTo, step through its neighbors. If I own the
	 * neighbor, and have >= the number of units on the given country, and have
	 * more than 1 unit on my country, return my country. Otherwise, return
	 * null.
	 */
	@Override
	public Country findAttackingCountry(Country moveTo) {
		Country attackFrom = null;
		if (moveTo == null)
			return null;

		for (Country country : me.getCountries()) {
			for (Country neighbor : country.getNeighbors()) {

				if (moveTo.equals(neighbor)
						&& (moveTo.getForcesVal() <= country.getForcesVal() && country
								.getForcesVal() > 1)) {
					attackFrom = country;
					break;
				}
			}
			if (attackFrom != null)
				break;
		}
		return attackFrom;

	}

}
