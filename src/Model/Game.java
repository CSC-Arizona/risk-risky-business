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

	private Game(int numOfHumanPlayers, int totalNumOfPlayers)
	{
		humans = numOfHumanPlayers;
		totalPlayers = totalNumOfPlayers;
		armiesPlaced = 0;
		placePhase = true;
		attackPhase = false;
		reinforcePhase = false;
		playerLocation = 0;
		numRedemptions = 0;
		newGame();

	}

	public static Game getInstance(int numOfHumanPlayers, int totalNumOfPlayers)
	{
		if (theGame == null)
			theGame = new Game(numOfHumanPlayers, totalNumOfPlayers);

		return theGame;
	}

	public void newGame()
	{
		selectedCountry = null;
		aiSelectedCountry = null;
		gameMap = new Map();
		deck = Deck.getInstance();
		// deck.shuffle();
		players = new ArrayList<>();
		addHumanPlayers(humans);
		addAI(totalPlayers - humans);
		int startingPlayer = 0; // this should change to a method that returns
								// the number of the position in the players
								// list of who is going first
		// or write a function that randomizes everyones position in the array,
		// and start at 0

	}

	public void startGame(int startingPlayer)
	{

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
	public Player placeArmies(Country countryToPlace)
	{

		if (armiesPlaced < 10)
		{
			if (countryToPlace.getOccupier() == null)
			{
				players.get(playerLocation).occupyCountry(countryToPlace);
				countryToPlace.setOccupier(players.get(playerLocation));
				countryToPlace.setForcesVal(1);
				armiesPlaced++;
				System.out.println(armiesPlaced);
				System.out.println("Next players turn");
				System.out.println("Army placed at : " + countryToPlace.toString());
				nextPlayer();

			} else
			{
				System.out.println("That country is already Occupied");
				System.out.println(armiesPlaced);

			}

		} else if (armiesPlaced < 20)

		{
			if (countryToPlace.getOccupier() == null)// TODO this will need to
														// be deleted, this is
														// just for testing
				System.out.println("You don't occupy this country");
			else if (countryToPlace.getOccupier().equals(players.get(0)))
			{
				countryToPlace.setForcesVal(1);
				armiesPlaced++;
				System.out.println("Reinforced " + selectedCountry.getName());
				nextPlayer();

			} else
				System.out.println("You don't occupy this country");

		} else
		{
			placePhase = false;
			attackPhase = true;

		}

		return players.get(playerLocation);
	}

	private void addAI(int numOfAI)
	{
		for (int i = 0; i < numOfAI; i++)
			players.add(new AI(AIStrat.EASY));// this will change later,
												// depending on what difficulty
												// is chosen;

	}

	public Country getSelectedCountry()
	{
		return selectedCountry;
	}

	public void setSelectedCountry(Country selectedCountry)
	{
		this.selectedCountry = selectedCountry;
	}

	private void addHumanPlayers(int numOfHumanPlayers)
	{
		for (int i = 0; i < numOfHumanPlayers; i++)
		{
			players.add(new HumanPlayer());
		}

	}

	public Map getGameMap()
	{
		return gameMap;
	}

	public Player nextPlayer()
	{
		playerLocation++;
		if (playerLocation >= totalPlayers)
			playerLocation = 0;

		return players.get(playerLocation);
	}

	public boolean isPlacePhase()
	{
		return placePhase;
	}

	public boolean isAttackPhase()
	{
		return attackPhase;
	}

	public boolean isReinforcePhase()
	{
		return reinforcePhase;
	}

	public boolean aiChoice()
	{
		aiSelectedCountry = ((AI) players.get(playerLocation)).pickRandomCountry(gameMap.getCountries());
		if (checkIfCountryAvailable(aiSelectedCountry))
		{
			placeArmies(aiSelectedCountry);
			return true;
		}
		return false;
	}

	private boolean checkIfCountryAvailable(Country countryToCheck)
	{

		return countryToCheck.getOccupier() == null;
	}
	
	public Player getCurrentPlayer(){
		return players.get(playerLocation);
	}
	
	//Main idea: player chooses which cards to redeem (max of 3)
	//if player has 5 cards, he MUST have a match, so call this function until the player chooses the matching 3 cards
	public int redeemCards(Player player, ArrayList<Card> cardsToRedeem){
		int numArmies = -1;
		if(cardsToRedeem.size()<3) //if the user didn't select 3 cards
			return -1;
		Card one = cardsToRedeem.get(0);
		Card two = cardsToRedeem.get(1);
		Card three = cardsToRedeem.get(2);
		
		//redeemable: three of the same unit type, one of each type, two different cards + wild
		//if can redeem:
		if ((one.getUnit().compareTo(two.getUnit())==0 && one.getUnit().compareTo(three.getUnit())==0 && three.getUnit().compareTo(two.getUnit())==0)
				|| (one.getUnit().compareTo(two.getUnit())!=0 && one.getUnit().compareTo(three.getUnit())!=0 && three.getUnit().compareTo(two.getUnit())!=0)
				|| (one.getUnit().compareTo("WILD")==0 && (two.getUnit().compareTo(three.getUnit())!=0))
				|| (two.getUnit().compareTo("WILD")==0 && (one.getUnit().compareTo(three.getUnit())!=0))
				|| (three.getUnit().compareTo("WILD")==0 && (one.getUnit().compareTo(two.getUnit())!=0))){
			numArmies = 0;
			numRedemptions++; 
			switch (numRedemptions){
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
					numArmies = 15 + 5*(numRedemptions-6);
					break;
			}
			
			//if any one of the redeemable cards contains a country that the player has, add 2 armies to that country. 
			for(int i=0; i<3; i++){
				for(int j=0; j<player.getCountries().size(); j++){
					
				}
			}
		}
		//TODO: check if cardsToRedeem are valid cards to redeem (as far as unit type)
		
		//if redeemable and a country that player owns, place 2 extra armies on that country.
		return numArmies; 
		//if num armies is -1 when returned, cards cannot be redeemed
	}
}
