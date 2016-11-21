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

	private Game(int numOfHumanPlayers, int totalNumOfPlayers)
	{
		humans = numOfHumanPlayers;
		totalPlayers = totalNumOfPlayers;
		armiesPlaced = 0;
		placePhase = true;
		attackPhase = false;
		reinforcePhase = false;
		playerLocation = 0;
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
		// place initial 50 armies
		if (armiesPlaced < 50)
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

		} else if (armiesPlaced < 107)// place remaining armies on own
										// countires, this number is if we start
										// with 35 units, and 3 players

		{

			if (countryToPlace.getOccupier().equals(players.get(0)))
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
			players.add(new AI(AIStrat.EASY, totalPlayers));// this will change later,
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
			players.add(new HumanPlayer(totalPlayers));
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
			players.get(playerLocation).setAvailableTroops(1);;
			return true;
		}
		return false;
	}

	private boolean checkIfCountryAvailable(Country countryToCheck)
	{

		return countryToCheck.getOccupier() == null;
	}

	public Player getCurrentPlayer()
	{
		return players.get(playerLocation);
	}
}
