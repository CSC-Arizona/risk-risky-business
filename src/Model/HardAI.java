package Model;

/*
 * HardAI
 * 
 * Author: Dylan Tobia, Abigail Dodd, Jewell Finder, Sydney Komro
 * 
 * Creates a HardAI Strategy to be used during gameplay by a hard level ai
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")
public class HardAI implements AIStrategy, Serializable {

	private AI me;

	/*
	 * constructors
	 */
	public HardAI() {
	};

	public HardAI(AI ai) {
		me = ai;
	}

	/*
	 * Places new troops on fringe countries
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

	/*
	 * checks if someone is about to take a whole continent, if so places a unit
	 * on the last country available there, if not, picks a country next to a
	 * fringe country and places there. If that is already taken, picks a random
	 * country.
	 */
	private Country blockOrPickFromFringe() {
		// step through each continent, check if one user almost has it all, if
		// so block
		// otherwise chose from fringe
		Country grabMe = null;
		ArrayList<Continent> allConts = Map.getAllContinents();
		for (Continent cont : allConts) {
			int totalNumOfCountries = cont.getNumOfCountries();
			Player p = findAnOwner(cont);
			int countOfOwners = 0;
			for (Country country : cont.getMyCountries()) {

				if (country.getOccupier() != null && country.getOccupier().equals(p)) {
					countOfOwners++;
				}

			}

			if (countOfOwners == totalNumOfCountries - 1) {
				grabMe = findEmptyCountry(cont);
				break;
			}

		}

		if (grabMe == null) {
			grabMe = me.checkAllNeighbors();

			if (grabMe == null)
				grabMe = me.pickRandomCountry();
		}
		return grabMe;
	}

	/*
	 * finds the country that is not occupied on cont, and returns it
	 */
	public Country findEmptyCountry(Continent cont) {
		for (Country country : cont.getMyCountries()) {
			if (country.getOccupier() == null)
				return country;
		}
		return null;
	}

	/*
	 * finds an owner of a country on cont
	 */
	private Player findAnOwner(Continent cont) {
		Player p = null;
		for (Country country : cont.getMyCountries()) {
			if (country.getOccupier() != null) {
				p = country.getOccupier();
				break;
			}
		}
		return p;
	}

	@Override
	public void setMe(AI ai) {
		me = ai;
	}

	/*
	 * checks if a country is completely surrounded by friendly country. If so,
	 * move the units on the country to its neighbors. continue until i have
	 * checked all of my countries
	 */
	@Override
	public String reinforce() {
		String str = "";
		int numRes = 0;
		int surroundCounter = 0;

		for (Country country : me.getCountries()) {

			surroundCounter = 0;
			ArrayList<Country> neighbors = country.getNeighbors();
			for (Country neighbor : neighbors) {
				if (neighbor.getOccupier().equals(me))
					surroundCounter++;
			}

			if (surroundCounter == neighbors.size() && country.getForcesVal() > 1) {
				while (country.getForcesVal() > 4) {
					for (Country neighbor : neighbors) {
						if (country.getForcesVal() > 6) {
							country.removeUnits(3);
							neighbor.addForcesVal(3);
							str += me.getName() + " removed 3 units from " + country.getName() + " and moved them to "
									+ neighbor.getName() + ".\n";
						} else {
							country.removeUnits(2);
							neighbor.addForcesVal(2);
							str += me.getName() + " removed 2 units from " + country.getName() + " and moved them to "
									+ neighbor.getName() + ".\n";
						}

						numRes++;

						if (numRes == 10)
							return str;

						if (country.getForcesVal() <= 1)
							break;
					}
				}

			}
		}

		return str;
	}// end reinforce

	// check which continents I own, or almost own, and reinforce them
	@Override
	public Country placeLeftOverUnits() {
		Country returnMe = null;
		ArrayList<Continent> conts = Map.getAllContinents();
		Collections.shuffle(conts);// shuffle continents up so that you don't
									// always select the same continent over and
									// over again
		int[] contCountryCounter = new int[7];
		int i = 0;
		for (Continent cont : conts) {
			contCountryCounter[i] = countOnCont(cont);
			i++;
		}
		i = 0;
		while (returnMe == null && i < contCountryCounter.length) {

			if (contCountryCounter[i] >= (conts.get(i).getNumOfCountries() / 2)) {
				returnMe = getRandomFromCont(conts.get(i));
			}
			i++;
		}

		if (returnMe == null)
			returnMe = me.pickRandomFromFringe();

		return returnMe;
	}

	/*
	 * count how many countries I own on a cont, and return that count
	 */
	public int countOnCont(Continent cont) {
		int counter = 0;
		for (Country country : cont.getMyCountries()) {
			if (country.getOccupier().equals(me))
				counter++;
		}
		return counter;
	}

	// takes a continent as its argument, steps through all countries on
	// continent, and checks which ones I own,
	// adds the ones I own to a list, and picks a random one from the list
	public Country getRandomFromCont(Continent continent) {
		ArrayList<Country> myContinentCountries = new ArrayList<>();
		for (Country country : continent.getMyCountries()) {
			if (country.getOccupier().equals(me)) {
				myContinentCountries.add(country);
			}
		}

		if (myContinentCountries.size() > 0) {
			int randInt = rand.nextInt(myContinentCountries.size());
			return myContinentCountries.get(randInt);
		}

		return null;
	}

	/*
	 * returns a country to place a new unit on
	 */
	@Override
	public Country placeUnit() {
		if (me.getCountries() == null || me.getCountries().size() == 0)
			return me.pickRandomCountry();

		Country countryToPlace = blockOrPickFromFringe();
		return countryToPlace;
	}

	/*
	 * finds a list of all countries I can attack, and picks a random one of
	 * them
	 */
	@Override
	public Country getCountryToAttack() {
		ArrayList<Country> allNeighboringCountries = findCountriesToAttack();
		if (allNeighboringCountries == null)
			return null;

		int randNum = rand.nextInt(allNeighboringCountries.size());
		return allNeighboringCountries.get(randNum);
	}

	/*
	 * Finds all fringe countries, and then checks all of its neighbors. If the
	 * neigbor is owned by an enemy, and I have more units on my country, add it
	 * to a list of countries worth attcking. If by chance countries worth
	 * attacking is empty, try finding countries the way a medium ai does.
	 * 
	 * return this list
	 */
	@Override
	public ArrayList<Country> findCountriesToAttack() {
		ArrayList<Country> fringeCountries = me.findFringeCountries();
		ArrayList<Country> countriesWorthAttacking = new ArrayList<>();
		for (Country country : fringeCountries) {
			ArrayList<Country> neighbors = country.getNeighbors();
			for (Country neighboringCountry : neighbors) {
				if (!neighboringCountry.getOccupier().equals(me)
						&& (neighboringCountry.getForcesVal() < country.getForcesVal() && country.getForcesVal() > 1)) {
					countriesWorthAttacking.add(neighboringCountry);
				} // end if
			} // end for
		} // end for

		if (countriesWorthAttacking.size() == 0)
			countriesWorthAttacking = theMediumWay();

		return countriesWorthAttacking;
	}

	/*
	 * given a country moveTo, check all of its neighbors until I find one that
	 * is owned by me, and has >= the amount moveTo -2. Return this country.
	 * Return null otherwise.
	 */
	@Override
	public Country findAttackingCountry(Country moveTo) {
		Country attackFrom = null;

		if (moveTo == null)
			return null;
		for (Country country : me.getCountries()) {
			for (Country neighbor : country.getNeighbors()) {
				if (moveTo.equals(neighbor) && moveTo.getForcesVal() - 2 <= country.getForcesVal()) {
					attackFrom = country;
					break;
				}
			}

			if (attackFrom != null && attackFrom.getForcesVal() > 1)
				break;
		}
		return attackFrom;
	}

	/*
	 * Finds all fringe countries, and checks each neighbor. If I do not own the
	 * neighbor, and have >= the same amount of units, and have moer than one
	 * unit on my country, add that country to a list. If the list is not empty,
	 * otherwise return null
	 */
	public ArrayList<Country> theMediumWay() {
		ArrayList<Country> fringeCountries = me.findFringeCountries();
		ArrayList<Country> countriesWorthAttacking = new ArrayList<>();
		for (Country country : fringeCountries) {
			ArrayList<Country> neighbors = country.getNeighbors();
			for (Country neighboringCountry : neighbors) {
				if (!neighboringCountry.getOccupier().equals(me)
						&& (neighboringCountry.getForcesVal() <= country.getForcesVal()
								&& country.getForcesVal() > 1)) {
					countriesWorthAttacking.add(neighboringCountry);
				} // end if
			} // end for
		} // end for

		if (countriesWorthAttacking.size() == 0)
			return null;

		return countriesWorthAttacking;
	}
	
	public String toString(){
		return "(hard)";
	}
}
