package Model;

import java.util.ArrayList;
import java.util.Random;

public class HardAI implements AIStrategy {

	private Player me;
	public HardAI(Player me)
	{
		this.me = me;
	}
	@Override
	public ArrayList<Country> countriesToReinforce(ArrayList<Country> countries, Player me) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String reinforce(ArrayList<Country> countries) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Country placeNewTroops(ArrayList<Country> countries) {
		
		Country countryToPlace = blockOrPickFromFringe(countries);
		return null;
	}
	private Country blockOrPickFromFringe(ArrayList<Country> countries) {
		// step through each continent, check if one user almost has it all, if so block
		//otherwise chose from fringe
		Country grabMe = null;
		ArrayList<Continent> allConts = Map.getAllContinents();
		for(Continent cont : allConts)
		{
			int totalNumOfCountries = cont.getNumOfCountries();
			Player p = cont.getMyCountries().get(0).getOccupier();
			int countOfOwners = 0;
			for(Country country : cont.getMyCountries())
			{
				if(countOfOwners == totalNumOfCountries - 1 && country.getOccupier() == null)
				{
					grabMe = country;
					break;
				}
				
				if(country.getOccupier().equals(p))
				{
					countOfOwners++;
				}
				
				
				
			}
			
			if(grabMe != null)
				break;
			
			
		}
		
		if(grabMe == null){
			grabMe = pickRandomFromFringe(countries);
		
			if(grabMe == null)
				grabMe = pickRandomCountry(countries);
		}
		return grabMe;
	}
	
	
}
