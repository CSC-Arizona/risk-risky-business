package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HardAI implements AIStrategy, Serializable {

	private AI me;

	public HardAI() {
	};

	public HardAI(AI ai) {
		me = ai;
	}

	@Override
	public ArrayList<Country> placeNewTroops() {
		ArrayList<Country> returnMe = null;
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
				returnMe = findFringeOnCont(conts.get(i));
			}
			i++;
		}


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

	private Country findEmptyCountry(Continent cont) {
		for (Country country : cont.getMyCountries()) {
			if (country.getOccupier() == null)
				return country;
		}
		return null;
	}

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

	@Override
	public String reinforce() {
		String str = "";
		System.out.println("gets here");
		int surroundCounter = 0;

		for (Country country : me.getCountries()) {

			surroundCounter = 0;
			ArrayList<Country> neighbors = country.getNeighbors();
			for (Country neighbor : neighbors) {
				if (neighbor.getOccupier().equals(me))
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
	private Country getRandomFromCont(Continent continent) {
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

	private ArrayList<Country> findFringeOnCont(Continent cont)
	{
		ArrayList<Country> continentFringeList = new ArrayList<>();
		int i = 0, j = 0;
		ArrayList<Country> countriesIOwn = findCountriesOnCont(cont);
		ArrayList<Country> neighbors = countriesIOwn.get(i).getNeighbors();
		while (i < countriesIOwn.size()) {
			j = 0;
			while (j < neighbors.size()) {

				if (!me.equals(neighbors.get(j).getOccupier())) {
					continentFringeList.add(countriesIOwn.get(i));
					j = neighbors.size();
				}
				j++;
			}
			i++;
			if (i < countriesIOwn.size())
				neighbors = countriesIOwn.get(i).getNeighbors();
		}
		return continentFringeList;
	}
	private ArrayList<Country> findCountriesOnCont(Continent cont) {
		ArrayList<Country> iOwn = new ArrayList<>();
		for(Country country : cont.getMyCountries())
		{
			if(country.getOccupier().equals(me))
			{
				iOwn.add(country);
			}
		}
		return iOwn;
	}

	@Override
	public Country placeUnit() {
		if (me.getCountries() == null || me.getCountries().size() == 0)
			return me.pickRandomCountry();

		Country countryToPlace = blockOrPickFromFringe();
		return countryToPlace;
	}
}
