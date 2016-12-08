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

public abstract class Player implements Serializable{
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
	
	
	public void getTroops() {
		if (myCountries.size() <= 9)
			availTroops += 3;
		else
			availTroops += (myCountries.size() / 3);

		//redeemCards(); //this will mess up how human players need to redeem cards, 
		//calling it in the GUI for AI and in the button listener for 
		//the human player instead

	}// end getTroops
	
	public void addTroops(int numTroops){
		availTroops+=numTroops;
	}

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

	public void setName(String name) {
		if ((name == null || name.equals("")) && faction != null) {
			this.name = faction.getDefaultPlayerName();
		} 
		else
			this.name = name;
	}// end setName

	public void occupyCountry(Country occupyMe) {
		myCountries.add(occupyMe);
	}// end occupyCountry

	public boolean equals(Player player) {
		if (player == null)
			return false;
		
		if (this == player)
			return true;

		return false;

	}// end equals

	public ArrayList<Country> getCountries() {
	//	Collections.shuffle(myCountries);
		return myCountries;
	}// end getCountries

	public int getAvailableTroops() {
		return availTroops;
	}// end getAvailableTrooops

	public ArrayList<Card> getCards() {
		return myCards;
	}// end getCards

	public void subtractFromAvailableTroops(int troops) {
		availTroops -= troops;

	}// end subtractFromAvailableTroops
	
	
	public void addAvailableTroops(int troops){
		availTroops += troops;
	}//end addAvailTroops

	public Faction getFaction() {
		return faction;
	}// end getFaction

	public String getName() {
		return name;
	}// end getName

	public void loseCountry(Country loser) {
		myCountries.remove(loser);
	}// end loseCountry

	public void addCard(Card cardToAdd) {
		myCards.add(cardToAdd);
		
		//If player now has 5 cards
		if (myCards.size() == 5)
			mustRedeemCards = true;
		else
			mustRedeemCards = false;
	}

	public ArrayList<Card> discardCards() {
		ArrayList<Card> cardsToDiscard = new ArrayList<>();
		for (Card card : myCards) {
			cardsToDiscard.add(card);
		}

		myCards.removeAll(cardsToDiscard);
		
		//Change whether the cards need to be redeemed
		if (myCards.size()<5)
			mustRedeemCards = false;
		
		return cardsToDiscard;
	}
	
	public void discardCards(ArrayList<Card> cards) { 
		myCards.removeAll(cards);
		
		//Change whether the cards need to be redeemed
		if (myCards.size()<5)
			mustRedeemCards = false;
	}

	//used for tourney mode
	public void setFaction(int i)
	{
		switch(i)
		{
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
	
	public boolean mustRedeemCards(){
		return mustRedeemCards;
	}
	
	public abstract ArrayList<Card> redeemCards();
}
