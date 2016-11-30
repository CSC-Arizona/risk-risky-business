package Model;

import java.util.ArrayList;

import javax.swing.JMenuItem;

public abstract class Player {
	private String name;
	private Faction faction;
	private int availTroops;
	private ArrayList<Country> myCountries;
	private ArrayList<Card> myCards;

	// private Country currentCountry; //to keep track of where to put the
	// armies in certain Card redeeming situations
	// private Continent[] allContinents; TODO

	public void getTroops() {
		if(myCountries.size() <= 9)
			availTroops = 3;
		else
			availTroops = myCountries.size() / 3;
	
		if(myCards.size() == 5)
		{
			//make user redeem cards
		}
		//TODO continent stuff
	}// end getTroops

	public Player(int numOfPlayers) {

		this.name = null;
		this.faction = null;
		this.availTroops = 43 - ((numOfPlayers - 3) * 5);

		this.myCountries = new ArrayList<>();
		this.myCards = new ArrayList<>();
	}// end constructor

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
		if (name == null) {
			setName(faction.getDefaultPlayerName());
		}
	}// end setFaction

	public void setName(String name) {
		this.name = name;
	}// end setName

	public void occupyCountry(Country occupyMe) {
		myCountries.add(occupyMe);
	}// end occupyCountry

	public boolean equals(Player player) {
		if (this.faction.compareTo(player.faction) == 0)
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

	public void subtractFromAvailableTroops(int troops) {
		availTroops -= troops;

	}// end subtractFromAvailableTroops

	public Faction getFaction() {
		return faction;
	}// end getFaction

	public String getName() {
		return name;
	}// end getName

	public void loseCountry(Country loser) {
		myCountries.remove(loser);
	}// end loseCountry

	public void addCard(Card cardToAdd)
	{
		myCards.add(cardToAdd);
	}
}
