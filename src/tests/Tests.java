package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Model.Card;
import Model.Deck;

public class Tests {

	@Test
	public void testArmy() {
		//There's nothing to test
	}
	
	@Test 
	public void testCard(){ 
		Card us = new Card("murrica", "infantry");
		assertEquals(us.getCountry(), "murrica");
		assertEquals(us.getUnit(), "infantry");
	}
	
	@Test 
	public void testDeck(){ 
		Card us = new Card("murrica", "infantry");
		Card ca = new Card("canada, eh", "cavalry");
		
		Deck testDeck;
		//need to finish this up
		
	}
	
	//need tests for Dice
	
}
