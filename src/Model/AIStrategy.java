package Model;

import java.util.ArrayList;
import java.util.Random;

public interface AIStrategy {

	Random rand = new Random();
	
	public ArrayList<Country> countriesToReinforce(ArrayList<Country> myCountries, Player me);
	public String reinforce(ArrayList<Country> countries);
	public Country placeNewTroops(ArrayList<Country> countries);
	public default Country pickRandomCountry(ArrayList<Country> countries)
	{
		int randNum = rand.nextInt(50);
		return countries.get(randNum);
	}
	public default Country pickRandomFromFringe(ArrayList<Country> countries) {
		ArrayList<Country> fringeCountries = findFringeCountries(countries);

		int randNum = 0;
		// System.out.println(fringeCountries.size() + " size of list to choose
		// from");
		if (fringeCountries.size() == 0)
			return null;
		randNum = rand.nextInt(fringeCountries.size());
		return fringeCountries.get(randNum);
	}
	public default ArrayList<Country> findFringeCountries(ArrayList<Country> countries) {
		ArrayList<Country> fringeCountries = new ArrayList<>();

		int i = 0, j = 0;
		ArrayList<Country> neighbors = countries.get(i).getNeighbors();
		while (i < countries.size()) {
			j = 0;
			while (j < neighbors.size()) {

				if (!this.equals(neighbors.get(j).getOccupier())) {
					fringeCountries.add(countries.get(i));
					j = neighbors.size();
				}
				j++;
			}
			i++;
			if (i < countries.size())
				neighbors = countries.get(i).getNeighbors();
		}

		return fringeCountries;
	}

	

}
