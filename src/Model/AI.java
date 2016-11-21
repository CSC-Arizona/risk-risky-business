package Model;

import java.util.ArrayList;
import java.util.Random;

public class AI extends Player {

	private AIStrat myStrat;
	public AI(AIStrat strat, int numOfPlayers)
	{
		super(numOfPlayers);
		myStrat = strat;
	}
	
	//get a random number between from 0 and 49
	//return that country in the array at index randomNumber
	public Country pickRandomCountry(Country[] countries)
	{
		Random rand = new Random();
		int randNum = rand.nextInt(50);
		return countries[randNum];
	}
	@Override
	public ArrayList<Card> playCards()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Country attack()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rearrangeTroops()
	{
		// TODO Auto-generated method stub

	}
	
	public void myTurn()
	{
		if(myStrat == AIStrat.EASY)
			easyMove();
		else
			hardMove();
	}

	private void hardMove()
	{
		//place units on outside countries, then try to 
		//take whole continent first, then pick random neighbor, take that continent, etc
		//then try to even out units on border countries, so that they all have close to the same amount at each one
		
	}

	private void easyMove()
	{
		//place new units in random occupied countries then
		//pick random country, if a neighbor to one of my own countries, try to occupy
		//do this till I cannot attack any longer
		
	}

}
