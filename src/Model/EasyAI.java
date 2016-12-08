package Model;

/*
 * EasyAI
 * 
 * by Abigail Dodd, Sydney Komro, Dylan Tobia, Jewell Finder
 * 
 * Creates an easy strategy for AI players that controls the way that they
 * place units, fight, and reinforce 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class EasyAI implements AIStrategy, Serializable {

	private AI me;

	public EasyAI() {
	};

	public EasyAI(AI ai) {
		me = ai;
	}

	/*
	 * placeNewTroops Places troops on fringe countries during deployment
	 */
	@Override
	public ArrayList<Country> placeNewTroops() {
		ArrayList<Country> countries = new ArrayList<>();
		Random rand = new Random();
		int randNum = 0;
		int i = 0;
		ArrayList<Country> fringes = me.findFringeCountries();
		randNum = rand.nextInt(fringes.size());
		countries.add(fringes.get(randNum));
		return countries;
	}

	/*
	 * setter for the ai
	 */
	@Override
	public void setMe(AI ai) {
		me = ai;
	}

	/*
	 * reinforce Does nothing. Easy AI doesn't reinforce.
	 */
	@Override
	public String reinforce() {
		String log = "";
		return log;
	}

	/*
	 * placeLeftOverUnits Reinforces countries after initial placement
	 */
	@Override
	public Country placeLeftOverUnits() {
		return me.pickRandomOwnedCountry();
	}

	/*
	 * placeUnit Chooses a country to occupy during game setup
	 */
	@Override
	public Country placeUnit() {
		int randNum = rand.nextInt(50);
		Map map = Map.getInstance(1);
		Country[] countries = map.getCountries();
		return countries[randNum]; 
	}

	/*
	 * getCountryToAttack chooses a country to go to war with during attack. The
	 * country is chosen randomly from its fringe countries
	 */
	@Override
	public Country getCountryToAttack() {
		ArrayList<Country> neighboringEnemies = findCountriesToAttack();
		if (neighboringEnemies == null)
			return null;

		int randInt = rand.nextInt(neighboringEnemies.size());
		Country attackMe = neighboringEnemies.get(randInt);

		return attackMe;
	}

	/*
	 * findCountriesToAttack Gets a collection of countries worth attacking from
	 * the fringe countries
	 */
	@Override
	public ArrayList<Country> findCountriesToAttack() {
		ArrayList<Country> fringeCountries = me.findFringeCountries();
		ArrayList<Country> countriesWorthAttacking = new ArrayList<>();
		for (Country country : fringeCountries) {
			ArrayList<Country> neighbors = country.getNeighbors();
			for (Country neighboringCountry : neighbors) {
				if (!neighboringCountry.getOccupier().equals(me)
						&& country.getForcesVal() > 1) {
					countriesWorthAttacking.add(neighboringCountry);
				} // end if
			} // end for
		} // end for

		if (countriesWorthAttacking.size() == 0) 
			return null;

		return countriesWorthAttacking;
	}

	/*
	 * toString No explanation needed
	 */
	public String toString() {
		return "(easy)";
	}

	/*
	 * findAttackingCountry Given the country we wat to attack (moveTo), find a
	 * good country to attack it from
	 */
	@Override
	public Country findAttackingCountry(Country moveTo) {
		Country attackFrom = null;
		if (moveTo == null)
			return null;
		for (Country country : me.getCountries()) {
			for (Country neighbor : country.getNeighbors()) {
				if (moveTo.equals(neighbor) && country.getForcesVal() > 1) {
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
