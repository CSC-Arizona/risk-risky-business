package Model;

import java.util.ArrayList;
import java.util.Random;

public class EasyAI implements AIStrategy{

	private Player me;
	public EasyAI(Player me)
	{
		this.me = me;
	}
	
	@Override
	public ArrayList<Country> countriesToReinforce(ArrayList<Country> myCountries, Player me) {
		ArrayList<Country> countriesToSelect = pickRandomSetOfCountries(myCountries, me);
		
		return countriesToSelect;
	}

	private ArrayList<Country> pickRandomSetOfCountries(ArrayList<Country> myCountries, Player me) {

		ArrayList<Country> countries = new ArrayList<>();
		int randNum = 0;
		int i=0;
		while (me.getAvailableTroops() > i) {
			i++;
			randNum = rand.nextInt(myCountries.size());
			countries.add(countries.get(randNum));
		}
		return countries;
	}

	@Override
	public String reinforce(ArrayList<Country> myCountries) {
		return null;
	}

	@Override
	public Country placeNewTroops(ArrayList<Country> myCountries) {
		return pickRandomCountry(myCountries);
	}


	

}
