package Model;

import java.util.ArrayList;
import java.util.Random;

public class EasyAI implements AIStrategy {

	private AI me;

	public EasyAI(){};
	public EasyAI(AI ai)
	{
		me = ai;
	}
	@Override
	public ArrayList<Country> placeNewTroops() {
		ArrayList<Country> countries = new ArrayList<>();
		Random rand = new Random();
		int randNum = 0;
		int i = 0;
		while (me.getAvailableTroops() > i) {
			i++;
			randNum = rand.nextInt(me.getCountries().size());
			countries.add(me.getCountries().get(randNum));
		}
		return countries;
	}

	@Override
	public void setMe(AI ai) {
		me = ai;
	}

	@Override
	public String reinforce() {
		//easy ai does nothing
		return null;
	}

	@Override
	public Country placeLeftOverUnits() {
		return me.pickRandomOwnedCountry();
	}

	@Override
	public Country placeUnit() {
		int randNum = rand.nextInt(50);
		Map map = Map.getInstance(0);
		Country[] countries = map.getCountries();
		return countries[randNum];

	}

}
