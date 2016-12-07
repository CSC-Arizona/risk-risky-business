package Model;

/*
 * EasyAI
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

	public MediumAI() {
	};

	public MediumAI(AI ai) { 
		me = ai;
	}

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
							log += me.getName() + " removed 2 units from " + country.getName()+ " and placed them on " + neighbor.getName() + ".\n";
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

	@Override
	public ArrayList<Country> placeNewTroops() {
		ArrayList<Country> countries = new ArrayList<>();
		ArrayList<Country> fringes = me.findFringeCountries();
		int randNum = 0;
		int i = 0;
 		while (me.getAvailableTroops() > i) {
			i+=2;
			randNum = rand.nextInt(fringes.size());
			countries.add(fringes.get(randNum));
		}
		return countries;
	}

	@Override
	public void setMe(AI ai) {
		me = ai;

	}

	@Override
	public Country placeLeftOverUnits() {
 
		return me.pickRandomFromFringe();
	}

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
	
	public String toString(){
		return "MED";
	}
	
	@Override
	public Country getCountryToAttack() {

		ArrayList<Country> allNeighboringEnemies = findCountriesToAttack();
		if (allNeighboringEnemies == null)
			return null;

		int randNum = rand.nextInt(allNeighboringEnemies.size());
		return allNeighboringEnemies.get(randNum);
	}

	@Override
	public ArrayList<Country> findCountriesToAttack() {
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

	@Override
	public Country findAttackingCountry(Country moveTo) {
		Country attackFrom = null;
		if (moveTo == null)
			return null;

		for (Country country : me.getCountries()) {
			for (Country neighbor : country.getNeighbors()) {

				if (moveTo.equals(neighbor)
						&& (moveTo.getForcesVal() <= country.getForcesVal() && country.getForcesVal() > 1)) {
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
