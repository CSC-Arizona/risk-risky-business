package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import Model.Card;
import Model.Continents;
import Model.Country;
import Model.Deck;
import Model.Dice;
import Model.Game;
import Model.HumanPlayer;
import Model.Map;
import Model.Player;

public class Tests {
	
	//No tests for Faction or Continents enums

	
	@Test 
	public void testCard(){ 
		// 100% coverage
		Card us = new Card("murrica", "infantry");
		assertEquals(us.getCountry(), "murrica");
		assertEquals(us.getUnit(), "infantry");
	}
	
	@Test 
	public void testDeck(){ 
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
	public void testDice(){
		//100% Coverage
		Dice die = new Dice();
		assertEquals(die.getValue(), 0);
		die.roll(1);
		die.roll(3);
		//Not currently testing the roll values here, but used prints to 
		//make sure it was working properly. 
	}
	
	
	@Test
	public void testCountry(){
		// 90.6% coverage
		Country wall = new Country("The Wall", 6.75, 3.5, Continents.BLUE);
		Country skagos = new Country("Skagos", 10, 3, Continents.BLUE);
		wall.addNeighbor(skagos);
		wall.drawMyButton();
		
		assertEquals(wall.getName(), "The Wall");
		assertEquals(wall.getX(), 6.75, 0);
		assertEquals(wall.getY(), 3.5, 0);
		assertEquals(wall.getForcesVal(), 0);
		assertEquals(wall.getOccupier(), null);
		assertEquals(wall.equals(wall), true);
	}
	
	@Test
	public void testMap(){
		// 100% Coverage
		Map map = Map.getInstance();
		
		assertEquals(map.getCountries()[0].getName(), "The Wall" );
	}
	
	@Test
	public void testHumanPlayer(){
		//TODO: 37.5% coverage (Player: 30% Coverage)
		Player human = new HumanPlayer(1);
	}
	
	@Test
	public void testRedeemCards(){
		ArrayList<Card> redeem = new ArrayList<Card>();
		Card walCard = new Card("The Wall","cavalry");
		Card someCard = new Card("Skagos","artillery");
		Card nextCard = new Card("The Wall Else","infantry");
		Player one = new HumanPlayer(1);
		Player two = new HumanPlayer(1);
		Country wall = new Country("The Wall", 6.75, 3.5, Continents.BLUE);
		Country skagos = new Country("Skagos", 10, 3, Continents.BLUE);
		skagos.setForcesVal(3);
		one.occupyCountry(wall);
		one.occupyCountry(skagos);
		redeem.add(someCard);
		redeem.add(nextCard);
		redeem.add(walCard);
		Game theGame = Game.getInstance(1, 6);
		int result = theGame.redeemCards(one, redeem);
		System.out.println(result);
		int result2 = theGame.redeemCards(one, redeem);
		System.out.println(result2);
		int result3 = theGame.redeemCards(one, redeem);
		System.out.println(result3);
		int result4 = theGame.redeemCards(two, redeem);
		System.out.println(result4);
		assertTrue(result == 4);
	}
	
	@Test
	public void testRedeemCards2(){
		ArrayList<Card> redeem = new ArrayList<Card>();
		Card walCard = new Card("The Wall","cavalry");
		Card someCard = new Card("Skagos","cavalry");
		Card nextCard = new Card("The Wall Else","cavalry");
		Player one = new HumanPlayer(1);
		Player two = new HumanPlayer(1);
		Country wall = new Country("The Wall", 6.75, 3.5, Continents.BLUE);
		Country skagos = new Country("Skagos", 10, 3, Continents.BLUE);
		skagos.setForcesVal(3);
		one.occupyCountry(wall);
		one.occupyCountry(skagos);
		redeem.add(someCard);
		redeem.add(nextCard);
		redeem.add(walCard);
		Game theGame = Game.getInstance(1, 6);
		theGame.newGame();
		int result = theGame.redeemCards(one, redeem);
		System.out.println(result);
		assertTrue(result == 4);
	}
	@Test
	public void testRedeemCards3(){
		ArrayList<Card> redeem = new ArrayList<Card>();
		Card walCard = new Card("The Wall","cavalry");
		Card someCard = new Card("Skagos","artillery");
		Card nextCard = new Card("WILD","WILD");
		Player one = new HumanPlayer(1);
		Player two = new HumanPlayer(1);
		Country wall = new Country("The Wall", 6.75, 3.5, Continents.BLUE);
		Country skagos = new Country("Skagos", 10, 3, Continents.BLUE);
		skagos.setForcesVal(3);
		one.occupyCountry(wall);
		one.occupyCountry(skagos);
		redeem.add(someCard);
		redeem.add(nextCard);
		redeem.add(walCard);
		Game theGame = Game.getInstance(1, 6);
		theGame.newGame();
		int result = theGame.redeemCards(one, redeem);
		System.out.println(result);
		assertTrue(result == 4);
	}
	@Test
	public void testRedeemCardsFail(){
		ArrayList<Card> redeem = new ArrayList<Card>();
		Card walCard = new Card("The Wall","cavalry");
		Card someCard = new Card("Skagos","artillery");
		Card nextCard = new Card("Something","cavalry");
		Player one = new HumanPlayer(1);
		Player two = new HumanPlayer(1);
		Country wall = new Country("The Wall", 6.75, 3.5, Continents.BLUE);
		Country skagos = new Country("Skagos", 10, 3, Continents.BLUE);
		skagos.setForcesVal(3);
		one.occupyCountry(wall);
		one.occupyCountry(skagos);
		redeem.add(someCard);
		redeem.add(nextCard);
		redeem.add(walCard);
		Game theGame = Game.getInstance(1, 6);
		theGame.newGame();
		int result = theGame.redeemCards(one, redeem);
		System.out.println(result);
		assertTrue(result == -1);
	}
	
	
}
