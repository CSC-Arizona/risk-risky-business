package Model;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * We had a better idea, but we'll change it later! Continents will eventually be objects
 */

/*
 public enum Continent {
 BLUE, GREEN, ORANGE, PINK, RED, YELLOW, BLACK;
 }*/

public class Continent implements Serializable{

	private Player owner;
	private ArrayList<Country> myCountries;
	private int ownerBonus;
	private String name;

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
		
		if (owner == null)
			return null;

		while (i < myCountries.size()) {
			if (!owner.equals(myCountries.get(i).getOccupier()))
				return null;
			i++;
		}
		this.owner = owner;
		return owner;
	}// end findOwner

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

}
