package Model;

import java.util.ArrayList;
import java.util.Collections;

import Model.Card;

/*
 * NOTE: Deck class is a singleton, should never have more than one! :) 
 */
public class Deck {

	private ArrayList<Card> riskDeck;
	private int size;
	private static Deck uniqueDeck;

	private Deck() {
		riskDeck = new ArrayList<Card>();
		fillDeck(riskDeck);
		size = 44;
	}

	public static synchronized Deck getInstance() {
		if (uniqueDeck == null)
			uniqueDeck = new Deck();
		return uniqueDeck;
	}

	public void shuffle() {
		riskDeck.clear();
		fillDeck(riskDeck);
		Collections.shuffle(riskDeck);
		size = 44;
	}

	public Card deal() {
		if(size>0){
			Card result;
			result = riskDeck.get(size);
			riskDeck.remove(size);
			size--;
			return result;
		}
		else
			return null;
	}
	
	public int getSize(){
		return size;
	}

	private void fillDeck(ArrayList<Card> deck) {
		// TODO: add all cards to deck
		deck.add(new Card("Wildlings", "Mammoth")); // example, not sure what
													// the plan is :)
	}

}
