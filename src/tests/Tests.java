package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Model.Card;
import Model.Continent;
import Model.Country;
import Model.Deck;
import Model.Dice;
import Model.Faction;
import Model.Game;
import Model.HumanPlayer;
import Model.Map;
import Model.Player;

public class Tests {

	// No tests for Faction or Continents enums
	
	private ArrayList<Player> players; 

	@Test
	public void testCard() {
		// 93% coverage
		Card us = new Card("murrica", "infantry");
		assertEquals(us.getCountry(), "murrica");
		assertEquals(us.getUnit(), "infantry");
		assertEquals(us.toString(), "murrica, infantry");
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
		assertEquals(52, testDeck.getSize());
		testDeck.shuffle();
		testDeck.deal();
		assertEquals(51, testDeck.getSize());
		testDeck.deal();
		assertEquals(50, testDeck.getSize());
		testDeck.shuffle();
		assertEquals(52, testDeck.getSize());

	}

	@Test
	public void testDice() {
		// 100% Coverage
		Dice die = new Dice();
		assertEquals(die.getValue(), 0);
		die.roll(1);
		die.roll(3);
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
		//Player player = players.get(0);
		//wall.setOccupier(player);
	}

	@Test
	public void testMap() {
		// 94.5% Coverage 
		//TODO: Testcase doesn't pass, but it's the full coverage amount. 
		Map map = Map.getInstance(1);
		map = map.newMap();
		//Map map1 = Map.newTourneyMap();

		map.getCountries();
		assertEquals(map.getInstance(0).getCountries()[0].getName(), "The Wall");
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
		Player one = new HumanPlayer(1);
		Player two = new HumanPlayer(1);
		Country wall = new Country("The Wall", 6.75, 3.5, blue);
		Country skagos = new Country("Skagos", 10, 3, blue);
		skagos.setForcesVal(3);
		one.occupyCountry(wall);
		one.occupyCountry(skagos);
		redeem.add(someCard);
		redeem.add(nextCard);
		redeem.add(walCard);
		Game theGame = Game.getInstance(1, 6, false);
		/*
		int result = theGame.redeemCards(one, redeem);
		System.out.println(result);
		int result2 = theGame.redeemCards(one, redeem);
		System.out.println(result2);
		int result3 = theGame.redeemCards(one, redeem);
		System.out.println(result3);
		int result4 = theGame.redeemCards(two, redeem);
		System.out.println(result4);
		assertTrue(result == 4);
		*/
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
		skagos.setForcesVal(3);
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
		skagos.setForcesVal(3);
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
		skagos.setForcesVal(3);
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
		
	}
	
	@Test
	public void testAIStrat(){
		
	}
}
