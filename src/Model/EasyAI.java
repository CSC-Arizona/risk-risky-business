package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class EasyAI implements AIStrategy, Serializable {

	private AI me;
	static int a,b,c,d,e,f,g,h,wa,fa;
	
	
	
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
			//System.out.println("A: "+ ++a);
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
		boolean stopFlag = false;
		for (Country country : me.getCountries()) {
			System.out.println("b: "+ ++b);
			if (country.getForcesVal() > 2) {
				stopFlag = false;
				while (country.getForcesVal() > 2 && stopFlag == false) {
					System.out.println("C: "+ ++c);
					int i = 0;
					for (Country neighbor : country.getNeighbors()) {
						if (neighbor.getOccupier().equals(me)) {
							neighbor.addForcesVal(1);
							country.removeUnits(1);
						}
						if (country.getForcesVal() == 2)
							break;
						
						i++;
						if(i >= country.getNeighbors().size())
						{
							stopFlag = true;
						}
					}
				}
			}
		}
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
	@Override
	public Country getCountryToAttack() {
		ArrayList<Country> neighboringEnemies = findCountriesToAttack();
		if(neighboringEnemies == null)
			return null;
		
		int randInt = rand.nextInt(neighboringEnemies.size());
		Country attackMe = neighboringEnemies.get(randInt);
//		
//		boolean found = false;
//		ArrayList<Country> countries = me.getCountries();
//		Country attackMe = null;
//		
//		while (!found){
//			//System.out.println("Whilea: " + ++wa);
//			int ran = (int)(Math.random() * countries.size());
//			attackMe = countries.get(ran);
//			
//			for (Country c : attackMe.getNeighbors()){
//				//System.out.println("Fa: " + ++fa);
//				if (!this.equals(c.getOccupier())){
//					found = true;
//					break;
//				}//end if
//			}//end for
//			
//		}//end while
//		
		return attackMe;
	}
	@Override
	public ArrayList<Country> findCountriesToAttack() {
		ArrayList<Country> fringeCountries = me.findFringeCountries();
		ArrayList<Country> countriesWorthAttacking = new ArrayList<>();
		for (Country country : fringeCountries) {
			//System.out.println("d: "+ ++d);
			ArrayList<Country> neighbors = country.getNeighbors();
			for (Country neighboringCountry : neighbors) {
				//System.out.println("e: "+ ++e);
				if (!neighboringCountry.getOccupier().equals(me) && country.getForcesVal() > 1) {
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
		if(moveTo == null)
			return null;
		for(Country country : me.getCountries())
		{
			for(Country neighbor : country.getNeighbors())
			{
				if(moveTo.equals(neighbor) && country.getForcesVal() > 1 )
				{
					attackFrom = country;
					break;
				}
			}
			if(attackFrom != null)
				break;
		}
		return attackFrom;
	}

}
