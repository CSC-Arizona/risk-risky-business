/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Faction.java
 * 	Purpose:	Faction Enum holds the name of the faction and its default name and sets/gets names. 
 */

package Model;

import java.io.Serializable;

public enum Faction implements Serializable{
	STARK("Stark", "Jon"), TARGARYEN("Targaryen", "Danaerys"), LANNISTER("Lannister", "Joffrey"), WHITEWALKERS(
			"White Walker", "Night King"), DOTHRAKI("Dothraki", "Khal Drogo"), WILDLINGS("Wildling", "Mance Rayder");

	private String myName;
	private String defaultPlayerName;

	// private constructor
	private Faction(String name, String playName) {
		myName = name;
		defaultPlayerName = playName;
	}

	// Return my name
	public String getName() {
		if (myName.equals("White Walker") || myName.equals("Wildling"))
			return "the " + myName;
		else if (myName.equals("Dothraki"))
			return "Khal of the Dothraki";
		else
			return "of house " + myName;
	}// end getName

	// So the AIs can have names associated with their factions
	public String getDefaultPlayerName() {
		return defaultPlayerName;
	}// end defaultPlayerName

}// end Faction
