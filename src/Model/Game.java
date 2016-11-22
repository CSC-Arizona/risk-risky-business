package Model;

import java.util.ArrayList;

public class Game {
	private ArrayList<Player> players;
	private Map gameMap;
	private Deck deck;
	private Country selectedCountry, aiSelectedCountry;
	private boolean placePhase, attackPhase, reinforcePhase;
	private int humans;
	private int totalPlayers, armiesPlaced, playerLocation;
	private static Game theGame;
	private int numRedemptions;

	private Game(int numOfHumanPlayers, int numOfAIPlayers)
	{
		humans = numOfHumanPlayers;
		totalPlayers = numOfHumanPlayers+numOfAIPlayers;
		armiesPlaced = 0;
		placePhase = true;
		attackPhase = false;
		reinforcePhase = false;
		playerLocation = 0;
		numRedemptions = 0;
		newGame();

	}

	public static Game getInstance(int numOfHumanPlayers, int totalNumOfPlayers) {
		if (theGame == null)
			theGame = new Game(numOfHumanPlayers, totalNumOfPlayers);

		return theGame;
	}

	public void newGame() {
		selectedCountry = null;
		aiSelectedCountry = null;
		gameMap = Map.getInstance();
		deck = Deck.getInstance();
		// deck.shuffle();
		players = new ArrayList<>();
		addHumanPlayers(humans);
		addAI(totalPlayers - humans);
		numRedemptions = 0;
		int startingPlayer = 0; // this should change to a method that returns
								// the number of the position in the players
								// list of who is going first
		// or write a function that randomizes everyones position in the array,
		// and start at 0

	}

	public void startGame(int startingPlayer) {

		// pick starting countries
		// while (placePhase)
		// {
		// if (players.get(playerLocation) instanceof AI)
		// {
		// selectedCountry = ((AI)
		// players.get(playerLocation)).pickRandomCountry(gameMap.getCountries());
		// placeArmies();
		// } else
		// {
		// // do nothing because the player needs to select a country
		//
		// }
		// // this loop just does nothing until all armies are placed!
		// }
		// doNextThing();

	}
	

	// this is called by the countryClickListener, and "places" an army in a
	// country, and sets the occupier to whichever player is up
	public Player placeArmies(Country countryToPlace) {
		// place initial 50 armies
		if (armiesPlaced < 50) {
			if (countryToPlace.getOccupier() == null) {
				players.get(playerLocation).occupyCountry(countryToPlace);
				countryToPlace.setOccupier(players.get(playerLocation));
				countryToPlace.setForcesVal(1);
				armiesPlaced++;
				System.out.println(armiesPlaced);
				System.out.println("Next players turn");
				System.out.println("Army placed at : " + countryToPlace.toString());
				players.get(playerLocation).subtractFromAvailableTroops(1);
				nextPlayer();

			} else {
				System.out.println("That country is already Occupied");
				System.out.println(armiesPlaced);

			}

		} else if (armiesPlaced < 107)// place remaining armies on own
										// countires, this number is if we start
										// with 35 units, and 3 players

		{
			placePhase = false;
			reinforcePhase = true;

			if (countryToPlace.getOccupier().equals(players.get(0))) {
				countryToPlace.setForcesVal(1);
				armiesPlaced++;
				System.out.println("Reinforced " + selectedCountry.getName());
				players.get(playerLocation).subtractFromAvailableTroops(1);
				nextPlayer();

			} else
				System.out.println("You don't occupy this country");

		} else {
			placePhase = false;
			attackPhase = true;

		}

		return players.get(playerLocation);
	}

	private void addAI(int numOfAI) {
		for (int i = 0; i < numOfAI; i++)
			players.add(new AI(AIStrat.EASY, totalPlayers));// this will change
															// later,
		// depending on what difficulty
		// is chosen;

	}

	public Country getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(Country selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	private void addHumanPlayers(int numOfHumanPlayers) {
		for (int i = 0; i < numOfHumanPlayers; i++) {
			players.add(new HumanPlayer(totalPlayers));
		}

	}

	public Map getGameMap() {
		return gameMap;
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}

	public Player nextPlayer() {
		playerLocation++;
		if (playerLocation >= totalPlayers)
			playerLocation = 0;

		return players.get(playerLocation);
	}

	public boolean isPlacePhase() {
		return placePhase;
	}

	public boolean isAttackPhase() {
		return attackPhase;
	}

	public boolean isReinforcePhase() {
		return reinforcePhase;
	}


	public boolean aiChoicePlacement()
	{
		aiSelectedCountry = ((AI) players.get(playerLocation)).pickRandomCountry(gameMap.getCountries());
		if (checkIfCountryAvailable(aiSelectedCountry)) {

			placeArmies(aiSelectedCountry);


			return true;
		}
		return false;
	}
	
	public void aiReinforcePlacement(){
		aiSelectedCountry = ((AI) players.get(playerLocation)).pickRandomCountryFromOccupied();
		placeArmies(aiSelectedCountry);
		
	}

	private boolean checkIfCountryAvailable(Country countryToCheck) {

		return countryToCheck.getOccupier() == null;
	}

	public Player getCurrentPlayer() {
		return players.get(playerLocation);
	}

	// Main idea: player chooses which cards to redeem (max of 3)
	// if player has 5 cards, he MUST have a match, so call this function until
	// the player chooses the matching 3 cards
	public int redeemCards(Player player, ArrayList<Card> cardsToRedeem) {
		int numArmies = -1;
		if (cardsToRedeem.size() < 3) // if the user didn't select 3 cards
			return -1;
		Card one = cardsToRedeem.get(0);
		Card two = cardsToRedeem.get(1);
		Card three = cardsToRedeem.get(2);

		// redeemable: three of the same unit type, one of each type, two
		// different cards + wild
		// if can redeem:
		if ((one.getUnit().compareTo(two.getUnit()) == 0 && one.getUnit().compareTo(three.getUnit()) == 0
				&& three.getUnit().compareTo(two.getUnit()) == 0)
				|| (one.getUnit().compareTo(two.getUnit()) != 0 && one.getUnit().compareTo(three.getUnit()) != 0
						&& three.getUnit().compareTo(two.getUnit()) != 0)
				|| (one.getUnit().compareTo("WILD") == 0 && (two.getUnit().compareTo(three.getUnit()) != 0))
				|| (two.getUnit().compareTo("WILD") == 0 && (one.getUnit().compareTo(three.getUnit()) != 0))
				|| (three.getUnit().compareTo("WILD") == 0 && (one.getUnit().compareTo(two.getUnit()) != 0))) {
			numArmies = 0;
			numRedemptions++;
			switch (numRedemptions) {
			case 1:
				numArmies = 4;
				break;
			case 2:
				numArmies = 6;
				break;
			case 3:
				numArmies = 8;
				break;
			case 4:
				numArmies = 10;
				break;
			case 5:
				numArmies = 12;
				break;
			case 6:
				numArmies = 15;
				break;
			default:
				numArmies = 15 + 5 * (numRedemptions - 6);
				break;
			}

			// if any one of the redeemable cards contains a country that the
			// player has, add 2 armies to that country.
			boolean added = false;
			for (Card c : cardsToRedeem) {
				for (Country t : player.getCountries()) {
					if (c.getCountry().compareTo(t.getName()) == 0) {
						// add 2 armies to that country
						added = true;
						int currentForces = t.getForcesVal();
						System.out.println("current Forces" + currentForces + t.getName());
						t.setForcesVal(2);
						System.out.println("updated Forces" + t.getForcesVal() + t.getName());
					}
				}
			}
			if (!added)
				System.out.println("no country cards to redeem");
		} else
			System.out.println("unable to redeem cards");
		return numArmies;
		// if numArmies is -1 when returned, cards cannot be redeemed
	}
}
