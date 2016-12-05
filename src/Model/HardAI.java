package Model;

import java.util.ArrayList;
import java.util.Random;

public class HardAI implements AIStrategy {

	private AI me;

	public HardAI(){};
	public HardAI(AI ai)
	{
		me = ai;
	}
	@Override
	public ArrayList<Country> placeNewTroops() {
		ArrayList<Country> returnMe = null;
		//place them on fringe countries near continent almost, or owned.
		return returnMe;

	}

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

			if(countOfOwners == totalNumOfCountries - 1)
			{
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

	private Country findEmptyCountry(Continent cont) {
		for(Country country : cont.getMyCountries())
		{
			if (country.getOccupier() == null)
				return country;
		}
		return null;
	}
	private Player findAnOwner(Continent cont) {
		Player p = null;
		for(Country country : cont.getMyCountries())
		{
			if(country.getOccupier() != null)
			{
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

	@Override
	public String reinforce() {
		String str = "";

		int surroundCounter = 0;

		for (Country country : me.getCountries()) {

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
						neighbor.addForcesVal(1);
						str += me.getName() + " removed 1 unit from " + country.getName() + " and moved it to "
								+ neighbor.getName() + ".\n";
						if (country.getForcesVal() == 1)
							break;
					}
				}

			}
		}

		return str;
	}// end reinforce

	@Override
	public Country placeLeftOverUnits() {
		//place units on fringe countries on continent I want to take
		return null;
	}

	@Override
	public Country placeUnit() {
		if(me.getCountries() == null || me.getCountries().size() == 0)
			return me.pickRandomCountry();
		
		Country countryToPlace = blockOrPickFromFringe();
		return countryToPlace;
	}
}
