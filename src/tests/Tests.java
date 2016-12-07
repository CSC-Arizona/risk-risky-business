package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Model.*;

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
	public void testCardsInDeck() {
		// 91% coverage
		int incorrectlyNamed = 0;
		Deck test = Deck.getInstance();
		ArrayList<Card> cards = test.getDeck();
		for (int i = 0; i < cards.size(); i++) {

			try {
				Scanner read = new Scanner(new File(cards.get(i).getFilename()));
			} catch (FileNotFoundException e) {
				System.out.println("Bad file name: " + cards.get(i).getFilename());
				incorrectlyNamed++;
			}
		} // end for
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
		assertNotEquals(discard.getPile(), null);
		discard.addToPile(testDeck.deal(discard));
		assertEquals(discard.getSize(), 1);
		discard.removeAll();
		assertEquals(discard.getSize(), 0);
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
	public void testContinent() {
		//85.6% coverage
		if (players == null)
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
		for (Country c : contCountries) {
			c.setOccupier(players.get(0));
		}
		Continent cont2 = new Continent();
		assertEquals(cont.payOwnerBonus(players.get(0)), 1);

		assertEquals(cont.toString(), "Test is held by jim");
	}

	@Test
	public void testMap() {
		// 94.5% Coverage
		// TODO: Testcase doesn't pass, but it's the full coverage amount.
		Map map = Map.getInstance(1);
		// Map map1 = Map.newTourneyMap();
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
		//Game theGame = Game.getInstance(1, 6, false);
		//theGame.newGame();
		// int result = theGame.redeemCards(one, redeem);
		// System.out.println(result);
		// assertTrue(result == 4);
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
		//Game theGame = Game.getInstance(1, 6, false);
		//theGame.newGame();
		// int result = theGame.redeemCards(one, redeem);
		// System.out.println(result);
		// assertTrue(result == 4);
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
		//Game theGame = Game.getInstance(1, 6, false);
		//theGame.newGame();
		// int result = theGame.redeemCards(one, redeem);
		// System.out.println(result);
		// assertTrue(result == -1);

	}

	@Test
	public void testFaction() {
		// 85% ENUM - MAY NOT BE OVER 90%
		assertEquals(Faction.DOTHRAKI.getName(), "Khal of the Dothraki");
		assertEquals(Faction.WILDLINGS.getName(), "the Wildling");
		assertEquals(Faction.STARK.getName(), "of house Stark");

		assertEquals(Faction.STARK.getDefaultPlayerName(), "Jon");
	}

	@Test
	public void testGame() {
		// Oh god...
	}

	@Test
	public void testTournamentMode() {

	}

	@Test
	public void testPlayer()
	{
		Player player = new HumanPlayer(3);
		player.getTroops();
		assertEquals(player.getAvailableTroops(), 46);
		player.addTroops(1);
		assertEquals(player.getAvailableTroops(), 47);
		player.setFaction("Lannister");
		assertTrue(player.getFaction() == Faction.LANNISTER); 
		player.setFaction("Stark");
		assertTrue(player.getFaction() == Faction.STARK);
		player.setFaction("Targaryen");
		assertTrue(player.getFaction() == Faction.TARGARYEN);
		player.setFaction("White Walkers");
		assertTrue(player.getFaction() == Faction.WHITEWALKERS);
		player.setFaction("Wildlings");
		assertTrue(player.getFaction() == Faction.WILDLINGS);
		player.setFaction("Dothraki");
		assertTrue(player.getFaction() == Faction.DOTHRAKI);
		player.setName("jim");
		assertEquals(player.getName(), "jim");
		Continent cont = new Continent(0, "jake");
		Country country = new Country("Jammy", 0 ,0, cont);
		country.setOccupier(player);
		assertTrue(country.getOccupier() == player);
		assertTrue(player.getCountries().size() == 1);
		player.subtractFromAvailableTroops(1);
		assertEquals(player.getAvailableTroops(), 46);
		player.setFaction(0);
		assertTrue(player.getFaction() == Faction.STARK);
		player.setFaction(1);
		assertTrue(player.getFaction() == Faction.TARGARYEN);
		player.setFaction(2);
		assertTrue(player.getFaction() == Faction.DOTHRAKI);
		player.setFaction(3);
		assertTrue(player.getFaction() == Faction.LANNISTER);
		player.setFaction(4);
		assertTrue(player.getFaction() == Faction.WHITEWALKERS);
		player.setFaction(5);
		assertTrue(player.getFaction() == Faction.WILDLINGS);
		player.addAvailableTroops(1);
		assertEquals(player.getAvailableTroops(), 47);
		player.setName("");
		assertEquals(player.getName(), "Mance Rayder");
		assertTrue(player.equals(player));
		assertEquals(player.equals(null), false);
		for(int i = 0; i < 10; i++)
		{
			Country c = new Country(String.valueOf(i),0,0,cont);
			c.setOccupier(player);
		}
		player.getTroops();
		assertEquals(player.getAvailableTroops(), 50);
	}
	@Test
	public void testAI() {
		AI aiE = new AI(new EasyAI(), 0);
		AI aiM = new AI(new MediumAI(), 0);
		AI aiH = new AI(new HardAI(), 0); 
		aiE.setStrategy(new EasyAI(aiE));
		AIStrategy testStrat = aiE.getStrategy();
		assertEquals(aiE.getStrategy(), testStrat);
		Country country = aiE.getStrategy().placeUnit();
		assertTrue(country != null);
		for (Country c : country.getNeighbors()) {
			c.setOccupier(new AI(new EasyAI(), 0));
		}
		country.setOccupier(aiE);
		country.addForcesVal(1);
		Continent cont = new Continent(0, "CONTINENT");
		Country country2 = new Country("Jammy", 0, 0, cont);
		country.addNeighbor(country2);
		country2.addNeighbor(country);

		ArrayList<Country> countries = aiE.getCountries();
		assertEquals(countries.size(), 1);
		country2.setOccupier(aiE);
		countries = aiE.getStrategy().placeNewTroops();
		countries.get(0).addForcesVal(5);
		countries.get(1).addForcesVal(5);;
		assertTrue(countries.get(0).getForcesVal() > 0);
		assertTrue(countries.get(1).getForcesVal() > 0);
		aiE.getStrategy().reinforce();
		assertTrue(countries.get(0).getForcesVal() >= 2);
		assertTrue(countries.get(0).getForcesVal() >= 2);
		assertTrue(aiE.pickRandomCountry() != null);
		Country attackMe = aiE.getStrategy().getCountryToAttack();
		assertTrue(attackMe != null);
		Country attackFrom = aiE.getStrategy().findAttackingCountry(attackMe);
		assertTrue(attackFrom != null);
		aiM.setStrategy(new MediumAI(aiM));
		for(Country c : Map.getInstance(0).getCountries())
		{ 
			if(c.getOccupier() == null)
				c.setOccupier(new AI(new EasyAI(), 0)); 
			
			c.addForcesVal(5);
		}
		Country c3 = aiM.getStrategy().placeUnit();
		c3.setOccupier(aiM);
		country2.setOccupier(aiM);
		c3.addNeighbor(country2);
		country2.addNeighbor(c3);
		assertTrue(c3 != null); 
		for (Country c : c3.getNeighbors()) {
			c.setOccupier(new AI(new EasyAI(), 0));
		}
		c3.addForcesVal(10);
		aiM.getStrategy().reinforce(); 
		
		countries = aiM.getCountries();
		countries = aiM.getStrategy().placeNewTroops();
		attackMe = aiM.getStrategy().getCountryToAttack();
		attackFrom = aiM.getStrategy().findAttackingCountry(attackMe);
		
        aiH.setStrategy(new HardAI(aiH));
		
		Country c4 = aiH.getStrategy().placeUnit();
		c4.setOccupier(aiH);
		country2.setOccupier(aiH);
		c4.addNeighbor(country2);
		country2.addNeighbor(c4);
		assertTrue(c3 != null); 
		for (Country c : c4.getNeighbors()) {
			c.setOccupier(new AI(new EasyAI(), 0));
		}
		c4.addForcesVal(10);
		aiH.getStrategy().reinforce(); 
		
		countries = aiH.getCountries();
		countries = aiH.getStrategy().placeNewTroops();
		attackMe = aiH.getStrategy().getCountryToAttack();
		attackFrom = aiH.getStrategy().findAttackingCountry(attackMe);
		
	}

	@Test
	public void testAIStrat() {

	}
}
