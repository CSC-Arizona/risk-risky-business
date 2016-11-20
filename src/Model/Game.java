package Model;

import java.util.ArrayList;

public class Game {
	private ArrayList<Player> players;
	private Map gameMap;
	private Deck deck;
	private Country selectedCountry;
	private boolean placePhase;
	private int humans;
	private int totalPlayers, armiesPlaced;
	private static Game theGame;
	private Game(int numOfHumanPlayers, int totalNumOfPlayers)
	{
		humans = numOfHumanPlayers;
		totalPlayers = totalNumOfPlayers;
		armiesPlaced = 0;
		placePhase = true;
		newGame();
		
		
	}

	
	public static Game getInstance(int numOfHumanPlayers, int totalNumOfPlayers)
	{
		if(theGame == null)
			theGame = new Game(numOfHumanPlayers, totalNumOfPlayers);
		
		return theGame;
	}
	private void newGame()
	{
		selectedCountry = null;
		gameMap = new Map();
		deck = Deck.getInstance();
		players = new ArrayList<>();
		addHumanPlayers(humans);
		addAI(totalPlayers - humans);
		int startingPlayer = 0; //this should change to a method that returns the number of the position in the players list of who is going first
		//or write a function that randomizes everyones position in the array, and start at 0
		
	}
	
	public void startGame(int startingPlayer)
	{
		
		//pick starting countries
		while(placePhase)
		{
			//this loop just does nothing until all armies are placed!
		}
			//doNextThing();
		
		
		
	}


	public void placeArmies()
	{
			if(armiesPlaced < 10)
			{
				if(selectedCountry.getOccupier() == null)
				{	
					players.get(0).occupyCountry(selectedCountry);
					selectedCountry.setOccupier(players.get(0));
					armiesPlaced++;
					System.out.println(armiesPlaced);
				}
			else{
				System.out.println("That country is already Occupied");
				System.out.println(armiesPlaced);
			}}
			else
				placePhase = false;
	}


	private void addAI(int numOfAI)
	{
		for(int i = 0; i < numOfAI; i++)
			players.add(new AI());
		
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
		for(int i = 0; i < numOfHumanPlayers; i++)
		{
			players.add(new HumanPlayer());
		}
		
	}
	public Map getGameMap()
	{
		return gameMap;
	}
	
}
