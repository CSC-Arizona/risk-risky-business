/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Continent.java
 * 	Purpose:	Continent class holds all information for each continent in risk game and tracks/pays bonuses.
 */

package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Continent implements Serializable {

	private Player owner;
	private ArrayList<Country> myCountries;
	private int ownerBonus;
	private String name;

	private int numOfCountries = 0;

	public Continent() {
		this.name = "";
		owner = null;
		myCountries = new ArrayList<Country>();
		ownerBonus = 0;
	}// end continent constructor #1

	public Continent(int ownerBonus, String name) {
		this.name = name;
		owner = null;
		this.myCountries = new ArrayList<Country>();
		this.ownerBonus = ownerBonus;
	}// end constructor #2

	// returns 0 if the player is not the owner of this country,
	// and returns the owner bonus otherwise
	/*
	 * payOwnerBonus() parameters: Player play returns: int (0 or bonus value)
	 * purpose: determines if current player gets a bonus for owning an entire
	 * continent and returns the bonus associated with that continent. dependent
	 * on findOwner();
	 */
	public int payOwnerBonus(Player play) {
		if (play.equals(findOwner()))
			return ownerBonus;
		else
			return 0;
	}// end payOwnerbonus

	public int getBonus() {
		return ownerBonus;
	}

	/*
	 * addCountry() parameters: Country country returns: none purpose: adds the
	 * given country to the current continent's country array
	 */
	public void addCountry(Country country) {
		myCountries.add(country);
		numOfCountries++;
	}// end addCountry

	/*
	 * findOwner() parameters: none returns: Player or null purpose: determines
	 * if all countries in myCountries are owned by the same player, if so, that
	 * player is returned.
	 */
	public Player findOwner() {
		Player owner = myCountries.get(0).getOccupier();
		int i = 1;

		if (owner == null){
			this.owner = null;
			return null;
		}
			

		while (i < myCountries.size()) {
			if (!owner.equals(myCountries.get(i).getOccupier())){
				this.owner = null;
				return null;
			}
				
			i++;
		}//end while
		
		this.owner = owner;
		return owner;
	}// end findOwner

	/*
	 * Override Object's toString() method for continent
	 */
	public String toString() {
		findOwner();
		String str = "";

		str += name + " is held by ";

		if (owner != null)
			str += owner.getName();
		else
			str += "no one.";

		return str;
	}// end toString

	public int getNumOfCountries() {
		return numOfCountries;
	}

	public ArrayList<Country> getMyCountries() {
		return myCountries;
	}

}
