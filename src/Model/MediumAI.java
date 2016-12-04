package Model;

import java.util.ArrayList;
import java.util.Random;

public class MediumAI implements AIStrategy {

	private Player me;

	MediumAI(Player me) {
		this.me = me;
	}

	@Override
	public ArrayList<Country> countriesToReinforce(ArrayList<Country> countries, Player me) {
		ArrayList<Country> returnCountry = null;
		returnCountry = findFringeCountries(countries);
		return returnCountry;
	}

	@Override
	public String reinforce(ArrayList<Country> countries) {
		for (Country country : countries) {
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

		return null;
	}

	@Override
	public Country placeNewTroops(ArrayList<Country> countries) {
		Country returnMe = null;

		returnMe = pickRandomFromFringe(countries);
		if (returnMe == null)
			returnMe = pickRandomCountry(countries);

		return returnMe;
	}

	

}
