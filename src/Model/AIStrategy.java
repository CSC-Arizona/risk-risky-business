/*
 * AIStrategy.java
 * 		An interface for use with AI
 * 
 */

package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public interface AIStrategy extends Serializable{

	public static Random rand = new Random();
	
	public String reinforce();
	public ArrayList<Country> placeNewTroops();
	public Country placeLeftOverUnits();
	public Country placeUnit();
	public void setMe(AI ai);
	public Country getCountryToAttack();
	public ArrayList<Country> findCountriesToAttack();
	public Country findAttackingCountry(Country moveTo);
	

}
