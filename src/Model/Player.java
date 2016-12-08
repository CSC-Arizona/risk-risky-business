/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Player.java
 * 	Purpose:	Abstract player class to control all player moves- both AI and Human- throughout the Risk game. 
 */

package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JMenuItem;

public abstract class Player implements Serializable {
	private String name;
	private Faction faction;
	private int availTroops;
	private ArrayList<Country> myCountries;
	private ArrayList<Card> myCards;
	private boolean mustRedeemCards = false;


	//constructor for player
	//parameters: number of players
	public Player(int numOfPlayers) {

		this.name = null;
		this.faction = null;
		this.availTroops = 43 - ((numOfPlayers - 3) * 5);

		this.myCountries = new ArrayList<>();
		this.myCards = new ArrayList<>();
	}// end constructor

	/*
	 * Adds troops depending on how many countries a player owns
	 */
	public void getTroops() {
		if (myCountries.size() <= 9)
			availTroops += 3;
		else
			availTroops += (myCountries.size() / 3);

	}// end getTroops

	/*
	 * adds troops equal to the numTroops passed
	 */
	public void addTroops(int numTroops) {
		availTroops += numTroops;
	}

	/*
	 * sets the faction compared to a string
	 */
	public void setFaction(String house) {
		if (house.compareTo("Lannister") == 0) {
			faction = Faction.LANNISTER;
		} else if (house.compareTo("Stark") == 0) {
			faction = Faction.STARK;
		} else if (house.compareTo("Targaryen") == 0) {
			faction = Faction.TARGARYEN;
		} else if (house.compareTo("White Walkers") == 0) {
			faction = Faction.WHITEWALKERS;
		} else if (house.compareTo("Dothraki") == 0) {
			faction = Faction.DOTHRAKI;
		} else if (house.compareTo("Wildlings") == 0) {
			faction = Faction.WILDLINGS;
		}

		// Automatically sets any null names to the defaults
		// for each house that I chose and defined in Faction
		if (name == null || name.equals("")) {
			setName(faction.getDefaultPlayerName());
		}
	}// end setFaction

	/*
	 * sets the name to the passed name, if the pased name is the empty string,
	 * sets the name equal to the players faction enum
	 */
	public void setName(String name) {
		if ((name == null || name.equals("")) && faction != null) {
			this.name = faction.getDefaultPlayerName();
		} else
			this.name = name;
	}// end setName

	/*
	 * occupyCountry adds a country to the players list of country
	 */
	public void occupyCountry(Country occupyMe) {
		myCountries.add(occupyMe);
	}// end occupyCountry

	/*
	 * compares a player to another player
	 */
	public boolean equals(Player player) {
		if (player == null)
			return false;

		if (this == player)
			return true;

		return false;

	}// end equals

	public ArrayList<Country> getCountries() {
		return myCountries;
	}// end getCountries

	public int getAvailableTroops() {
		return availTroops;
	}// end getAvailableTrooops

	public ArrayList<Card> getCards() {
		return myCards;
	}// end getCards

	/*
	 * removes the int troops from the availTroops variable
	 */
	public void subtractFromAvailableTroops(int troops) {
		availTroops -= troops;

	}// end subtractFromAvailableTroops

	/*
	 * adds the int troops to the availTroops variable
	 */
	public void addAvailableTroops(int troops) {
		availTroops += troops;
	}// end addAvailTroops

	public Faction getFaction() {
		return faction;
	}// end getFaction

	public String getName() {
		return name;
	}// end getName

	/*
	 * removes a country from the players list of countries
	 */
	public void loseCountry(Country loser) {
		myCountries.remove(loser);
	}// end loseCountry

	/*
	 * adds a card to the list of the players cards, if the player has 5, sets a
	 * mustReedeemCards flag
	 */
	public void addCard(Card cardToAdd) {
		myCards.add(cardToAdd);

		// If player now has 5 cards
		if (myCards.size() == 5)
			mustRedeemCards = true;
		else
			mustRedeemCards = false;
	}

	/*
	 * for each card the player owns, add it to another list, and then remove
	 * them all.
	 */
	public ArrayList<Card> discardCards() {
		ArrayList<Card> cardsToDiscard = new ArrayList<>();
		for (Card card : myCards) {
			cardsToDiscard.add(card);
		}

		myCards.removeAll(cardsToDiscard);

		// Change whether the cards need to be redeemed
		if (myCards.size() < 5)
			mustRedeemCards = false;

		return cardsToDiscard;
	}

	/*
	 * discards all cards
	 */
	public void discardCards(ArrayList<Card> cards) {
		myCards.removeAll(cards);

		// Change whether the cards need to be redeemed
		if (myCards.size() < 5)
			mustRedeemCards = false;
	}

	// used for tourney mode
	/*
	 * sets faction by integer passed, instead of string
	 */
	public void setFaction(int i) {
		switch (i) {
		case 0:
			faction = Faction.STARK;
			break;
		case 1:
			faction = Faction.TARGARYEN;
			break;
		case 2:
			faction = Faction.DOTHRAKI;
			break;
		case 3:
			faction = Faction.LANNISTER;
			break;
		case 4:
			faction = Faction.WHITEWALKERS;
			break;
		case 5:
			faction = Faction.WILDLINGS;
			break;
		}
	}

	public boolean mustRedeemCards() {
		return mustRedeemCards;
	}

	// abstract function that must be implemented by any classes that extend
	// this one
	public abstract ArrayList<Card> redeemCards();
}
