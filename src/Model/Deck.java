package Model;

import java.util.ArrayList;

/*
 * NOTE: Deck class is a singleton, should never have more than one! :) 
 */
public class Deck {
	
	private ArrayList<Card> riskDeck;
	private static Deck uniqueDeck;
	
	private Deck(){
		riskDeck = new ArrayList<Card>();
		fillDeck();
	}
	
	public static synchronized Deck getInstance(){
		if(uniqueDeck ==null)
			uniqueDeck = new Deck();
		return uniqueDeck;
	}
	
	public void shuffle(){
		//TODO: stub
	}
	
	public Card deal(){
		//TODO: stub
		return null;
	}
	
	private void fillDeck(){
		//TODO: add all cards to deck
		riskDeck.add(new Card("Wildlings", "Mammoth")); //example, not sure what the plan is :) 
	}

}
