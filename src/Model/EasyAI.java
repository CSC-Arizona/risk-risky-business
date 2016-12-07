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
//		while (me.getAvailableTroops() > i) { 
//			i+=2;
			randNum = rand.nextInt(me.getCountries().size());
			countries.add(me.getCountries().get(randNum));
//		}
		return countries;
	}

	@Override
	public void setMe(AI ai) {
		me = ai;
	}

	@Override
	public String reinforce() { 
		String log = "";
		ArrayList<Country> countries = me.getCountries();
		
		
		for (int i=0; i < 3; i++){
			int ran = (int)(Math.random() * countries.size());
			Country c = countries.get(ran);
			
			if (c.getForcesVal() > 3){
				ran = (int)(Math.random() * c.getNeighbors().size());
				Country n = c.getNeighbors().get(ran);
				n.addForcesVal(2);
				c.removeUnits(2);
				log+=me.getName() + " removed 2 units from " + c.getName() + " and placed them on " + n.getName() + ".\n";
			}//end if
		}//end for
				
		return log;
	}

	@Override
	public Country placeLeftOverUnits() { 
		return me.pickRandomOwnedCountry();
	}

	@Override
	public Country placeUnit() {
		int randNum = rand.nextInt(50);
		Map map = Map.getInstance(1);
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

		return attackMe; 
	}
	@Override 
	public ArrayList<Country> findCountriesToAttack() {
		ArrayList<Country> fringeCountries = me.findFringeCountries();
		ArrayList<Country> countriesWorthAttacking = new ArrayList<>();
		for (Country country : fringeCountries) {
			ArrayList<Country> neighbors = country.getNeighbors();
			for (Country neighboringCountry : neighbors) {
				if (!neighboringCountry.getOccupier().equals(me) && country.getForcesVal() > 1) {
						countriesWorthAttacking.add(neighboringCountry);
				} // end if
			} // end for
		} // end for

		
		if (countriesWorthAttacking.size() == 0)
			return null;

		return countriesWorthAttacking;
	}
	
	public String toString(){
		return "EASY";
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
