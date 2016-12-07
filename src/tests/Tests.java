package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Model.AI;
import Model.AIStrategy;
import Model.Card;
import Model.Continent;
import Model.Country;
import Model.Deck;
import Model.Dice;
import Model.DiscardPile;
import Model.EasyAI;
import Model.Faction;
import Model.Game;
import Model.HardAI;
import Model.HumanPlayer;
import Model.Map;
import Model.MediumAI;
import Model.Player;

public class Tests {

	// No tests for Faction or Continents enums
	
	private ArrayList<Player> players;

	@Test
	public void testCard() {
		// 93% coverage
		Card us = new Card("murrica", "infantry");
		Card us2 = new Card("murrica", "cavalry");
		Card us3 = new Card("murrica", "artillery");
		Card us0 = new Card("murrica", "WILD");
		
		assertEquals(us.getCountry(), "murrica");
		assertEquals(us.getUnit(), "infantry");
		assertEquals(us.toString(), "murrica, infantry");
		
		//unit types
		assertEquals(1, us.getUnitType());
		assertEquals(2, us2.getUnitType());
		assertEquals(3, us3.getUnitType());
		assertEquals(0, us0.getUnitType());
		
		//comparisons
		assertTrue(new Card("flarm", "infantry").equals(us));
		assertTrue(new Card("flarm", "cavalry").equals(us2));
		assertTrue(new Card("flarm", "artillery").equals(us3));
		assertTrue(new Card("flarm", "wild").equals(us0));
		assertTrue(us0.equals(us));
		assertTrue(us.equals(us0));
		assertTrue(us0.equals(us3));
	}
	
	@Test
	public void testCardsInDeck(){
		// 91% coverage
		int incorrectlyNamed = 0;
		Deck test = Deck.getInstance();
		ArrayList<Card> cards = test.getDeck();
		for (int i=0; i < cards.size(); i++){
			
			try {
				Scanner read = new Scanner(new File(cards.get(i).getFilename()));
			} catch (FileNotFoundException e) {
				System.out.println("Bad file name: "+ cards.get(i).getFilename());
				incorrectlyNamed++;
			}
		}//end for
		assertEquals(incorrectlyNamed, 0);
	}

	private Object File(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Test
	public void testDeck() {
		// 91% coverage
		Deck testDeck = Deck.getInstance();
		DiscardPile discard = new DiscardPile();
		assertEquals(52, testDeck.getSize());
		testDeck.shuffle(discard);
		testDeck.deal(discard);
		assertEquals(51, testDeck.getSize());
		testDeck.deal(discard);
		assertEquals(50, testDeck.getSize());
		testDeck.shuffle(discard);
		assertEquals(52, testDeck.getSize());

	}

	@Test
	public void testDice() {
		// 100% Coverage
		Dice die = new Dice(1);
		assertEquals(die.getValue(), 1);
		ArrayList<Dice> num1 = die.roll(1);
		ArrayList<Dice> num2 = die.roll(3);
		
		assertTrue(num1.get(0).getValue() < 7); 
		assertTrue(num1.get(0).getValue() > 0);
		// Not currently testing the roll values here, but used prints to
		// make sure it was working properly.
	}

	@Test
	public void testCountry() {
		// 91% coverage
		
		Continent testCont = new Continent(1, "Tester");
		Country wall = new Country("The Wall", 6.75, 3.5, testCont);
		Country skagos = new Country("Skagos", 10, 3, testCont);
		Country shirt = new Country("long", 10, 3, testCont); 
		wall.addNeighbor(skagos);

		assertEquals(wall.getName(), "The Wall");
		assertEquals(wall.toString(), "The Wall");
		assertEquals(wall.getX(), 6.75, 0);
		assertEquals(wall.getY(), 3.5, 0);
		assertEquals(wall.getForcesVal(), 0);
		assertEquals(wall.getOccupier(), null);
		assertEquals(wall.equals(wall), true);
		
		ActionListener act = null;  
		wall.makeButton(1, 1, act);
		wall.updateButton(1, 1);
		wall.getMyButton();
		
		ArrayList<Country> neighbors = wall.getNeighbors();
		assertEquals((Country) neighbors.get(0), skagos);
		AI player1 = new AI(new EasyAI(), 0);
		player1.setName("test name");
		wall.setOccupier(player1); 
		assertEquals(wall.getOccupier().getName(), "test name");
		AI player2 = new AI(new EasyAI(), 0);
		player2.setName("jim");
		wall.setOccupier(player2);
		assertEquals(wall.getOccupier().getName(), "jim");
		wall.addForcesVal(10);
		assertEquals(wall.getForcesVal(), 10);
		wall.removeUnits(5);
		assertEquals(wall.getForcesVal(), 5);
		wall.setForcesToZero();
		assertEquals(wall.getForcesVal(), 0);
		
	}

	@Test
	public void testContinent(){
		if(players == null)
			players = new ArrayList<>();
		players.add(new HumanPlayer(2));
		players.add(new HumanPlayer(2));
		
		players.get(0).setName("jim");
		Continent cont = new Continent(1, "Test");
		Country country = new Country("Jammy", 0, 0, cont);
		assertEquals(cont.payOwnerBonus(players.get(0)), 0);
		
		assertEquals(cont.getNumOfCountries(), 1);
		assertEquals(cont.getMyCountries().get(0).getName(), "Jammy"); 
		ArrayList<Country> contCountries = cont.getMyCountries();
		for(Country c : contCountries) 
		{
			c.setOccupier(players.get(0));
		}
		Continent cont2 = new Continent();
		assertEquals(cont.payOwnerBonus(players.get(0)), 1);
	 	
		assertEquals(cont.toString(), "Test is held by jim");
	}
	 
	@Test
	public void testMap() {
		// 94.5% Coverage 
		//TODO: Testcase doesn't pass, but it's the full coverage amount. 
		Map map = Map.getInstance(1);
		//Map map1 = Map.newTourneyMap();
		Country[] allCountries = map.getCountries();
		assertEquals(allCountries[0].getName(), "The Wall");
		map = map.newTourneyMap();
		assertEquals(allCountries[0].getName(), "The Wall");
		assertEquals(map.getAllContinents().size(), 7);
	}

	@Test 
	public void testHumanPlayer() {
		// % coverage

		Player human = new HumanPlayer(1);
	}

	@Test
	public void testRedeemCards() { 
		Continent blue = new Continent(0, "Blue");
		ArrayList<Card> redeem = new ArrayList<Card>();
		Card walCard = new Card("The Wall", "cavalry");
		Card someCard = new Card("Skagos", "artillery");
		Card nextCard = new Card("The Wall Else", "infantry");
		Player one = new AI(new EasyAI(), 1);
		Player two = new AI(new MediumAI(),1);
		Country wall = new Country("The Wall", 6.75, 3.5, blue);
		Country skagos = new Country("Skagos", 10, 3, blue);
		skagos.addForcesVal(3);
		one.occupyCountry(wall);
		one.occupyCountry(skagos);
		one.addCard(walCard);
		one.addCard(someCard);
		one.addCard(nextCard);
		
		//redeem.add(nextCard); 
		redeem.add(walCard);
		redeem.add(someCard);
		redeem.add(nextCard);
		assertEquals(one.getCards(), redeem);
		
		one.addCard(walCard);
		one.addCard(someCard);
		
//		for (Card c: one.redeemCards()){
//			System.out.println(c.toString());
//		}
		assertEquals(one.redeemCards(), redeem);
		
		redeem = new ArrayList<Card>();
		two.addCard(walCard);
		two.addCard(walCard);
		two.addCard(walCard);
		two.addCard(someCard);
		two.addCard(nextCard);
		redeem.add(walCard);
		redeem.add(walCard);
		redeem.add(walCard);
		assertEquals(two.redeemCards(), redeem);
	}

	@Test
	public void testRedeemCards2() {
		Continent blue = new Continent(0, "Blue");
		ArrayList<Card> redeem = new ArrayList<Card>();
		Card walCard = new Card("The Wall", "cavalry");
		Card someCard = new Card("Skagos", "cavalry");
		Card nextCard = new Card("The Wall Else", "cavalry");
		Player one = new HumanPlayer(1);
		Player two = new HumanPlayer(1);
		Country wall = new Country("The Wall", 6.75, 3.5, blue);
		Country skagos = new Country("Skagos", 10, 3, blue);
		skagos.addForcesVal(3);
		one.occupyCountry(wall);
		one.occupyCountry(skagos);
		redeem.add(someCard);
		redeem.add(nextCard);
		redeem.add(walCard);
		Game theGame = Game.getInstance(1, 6, false);
		theGame.newGame();
		//int result = theGame.redeemCards(one, redeem);
		//System.out.println(result);
		//assertTrue(result == 4);
	}

	@Test
	public void testRedeemCards3() {
		Continent blue = new Continent(0, "Blue");
		ArrayList<Card> redeem = new ArrayList<Card>();
		Card walCard = new Card("The Wall", "cavalry");
		Card someCard = new Card("Skagos", "artillery");
		Card nextCard = new Card("WILD", "WILD");
		Player one = new HumanPlayer(1);
		Player two = new HumanPlayer(1);
		Country wall = new Country("The Wall", 6.75, 3.5, blue);
		Country skagos = new Country("Skagos", 10, 3, blue);
		skagos.addForcesVal(3);
		one.occupyCountry(wall);
		one.occupyCountry(skagos);
		redeem.add(someCard);
		redeem.add(nextCard);
		redeem.add(walCard);
		Game theGame = Game.getInstance(1, 6, false);
		theGame.newGame();
		//int result = theGame.redeemCards(one, redeem);
		//System.out.println(result);
		//assertTrue(result == 4);
	}

	@Test
	public void testRedeemCardsFail() {
		Continent blue = new Continent(0, "Blue");
		ArrayList<Card> redeem = new ArrayList<Card>();
		Card walCard = new Card("The Wall", "cavalry");
		Card someCard = new Card("Skagos", "artillery");
		Card nextCard = new Card("Something", "cavalry");
		Player one = new HumanPlayer(1);
		Player two = new HumanPlayer(1);
		Country wall = new Country("The Wall", 6.75, 3.5, blue);
		Country skagos = new Country("Skagos", 10, 3, blue);
		skagos.addForcesVal(3);
		one.occupyCountry(wall);
		one.occupyCountry(skagos);
		redeem.add(someCard);
		redeem.add(nextCard);
		redeem.add(walCard);
		Game theGame = Game.getInstance(1, 6, false);
		theGame.newGame();
		//int result = theGame.redeemCards(one, redeem);
		//System.out.println(result);
		//assertTrue(result == -1);

	}
	
	@Test 
	public void testFaction(){
		// 85% ENUM - MAY NOT BE OVER 90%
		assertEquals(Faction.DOTHRAKI.getName(), "Khal of the Dothraki");
		assertEquals(Faction.WILDLINGS.getName(), "the Wildling");
		assertEquals(Faction.STARK.getName(), "of house Stark");
		
		assertEquals(Faction.STARK.getDefaultPlayerName(), "Jon");
	}
	
	@Test 
	public void testGame(){
		//Oh god...
	}
	
	@Test
	public void testTournamentMode(){
		
	}
	
	@Test
	public void testAI(){
		 AI aiE = new AI(new EasyAI(), 0);
		 AI aiM = new AI(new MediumAI(), 0);
		 AI aiH = new AI(new HardAI(), 0);
		 aiE.setStrategy(new EasyAI(aiE));
		 AIStrategy testStrat = aiE.getStrategy();
		 assertEquals(aiE.getStrategy(), testStrat);
	}
	
	@Test
	public void testAIStrat(){
		
	}
}
