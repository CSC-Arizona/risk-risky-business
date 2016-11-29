package Model;

import java.util.ArrayList;

/*
 * We had a better idea, but we'll change it later! Continents will eventually be objects
 */

/*
 public enum Continent {
 BLUE, GREEN, ORANGE, PINK, RED, YELLOW, BLACK;
 }*/

public class Continent {

	private Player owner;
	private ArrayList<Country> myCountries;
	private int ownerBonus;
	private String name;

	public Continent() {
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
	public int payOwnerBonus(Player play) {
		if (play.equals(findOwner()))
			return ownerBonus;
		else
			return 0;
	}// end payOwnerbonus

	public void addCountry(Country country) {
		myCountries.add(country);
	}// end addCountry

	public Player findOwner() {
		Player owner = myCountries.get(0).getOccupier();
		int i = 1;

		while (i < myCountries.size()) {
			if (!owner.equals(myCountries.get(i).getOccupier()))
				return null;
			i++;
		}
		return owner;
	}// end findOwner

	public String toString() {
		String str = "";

		str += name;

		for (Country c : myCountries) {
			str += "\n\t" + c.getName();
		}

		return str;
	}// end toString

}
