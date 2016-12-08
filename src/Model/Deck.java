/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Deck.java
 * 	Purpose:	Singleton Deck class holding all card objects of the risk game with shuffling and dealing. Also holds a discard pile.
 */

package Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import Model.Card;

/*
 * Deck has 52 cards: 50 (one for each territory) + 2 (wild cards)
 * NOTE: Deck class is a singleton, should never have more than one! :) 
 */
public class Deck implements Serializable{

	private ArrayList<Card> riskDeck;
	private int size;
	private static Deck uniqueDeck;

	/*
	 * Constructor
	 */
	private Deck() {
		riskDeck = new ArrayList<Card>();
		fillDeck(riskDeck);
		DiscardPile placeHolder = new DiscardPile();
		shuffle(placeHolder);
		size = 52;
	}// end constructor

	// Used only for testing card
	public ArrayList<Card> getDeck() {
		return riskDeck;
	}
	
	/*
	 * creates a brand new deck
	 */
	public Deck newDeck(){
		uniqueDeck=new Deck();
		return uniqueDeck;
	}


	/*
	 * getInstance() 
	 * 		if unique deck is null, create a new one, otherwise return uniqueDeck
	 */
	public static synchronized Deck getInstance() {
		if (uniqueDeck == null)
			uniqueDeck = new Deck();
		return uniqueDeck;
	}// end getInstance
	
	/*
	 * ReadObject, special method for loading singleton objects
	 */
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
	    ois.defaultReadObject();
	    uniqueDeck = this;
	}

	/*
	 * Special object for loading singleton objects
	 */
	private Object readResolve()  {
	    return uniqueDeck;
	}

	/*
	 * shuffles the discard pile into the deck if the deck itself is empty. otherwise just creates, fills, and shuffles a new deck
	 */
	public void shuffle(DiscardPile pile) {
		if (size == 0 && pile.getSize()>0) {
			riskDeck.clear();
			riskDeck.addAll(pile.getPile());
			Collections.shuffle(riskDeck);
			size = riskDeck.size();
			pile.removeAll();
		} else {
			riskDeck.clear();
			fillDeck(riskDeck);
			Collections.shuffle(riskDeck);
			size = 52;
		}
	}// end shuffle

	// returns null if the deck has run out of cards.
	// grabs one card off of the top of the deck, and returns it.
	public Card deal(DiscardPile pile) {
		size = riskDeck.size();
		if (size > 0) {
			Card result;
			result = riskDeck.get(0);
			riskDeck.remove(0);
			size--;
			return result;
		} else {
			shuffle(pile);
			return deal(pile);
		}
	}// end deal

	public int getSize() {
		return size;
	}// end getSize

	public boolean isEmpty() {
		if (size == 0)
			return true;
		else
			return false;
	}

	/*
	 * adds card c to the discard pile
	 */
	public void discard(Card c, DiscardPile pile) {
		pile.addToPile(c);
	}

	// possible units: infantry, cavalry, artillery. Add all territories
	// (countries) and 2 wild cards.
	// fills the deck with new, unique cards
	private void fillDeck(ArrayList<Card> deck) {
		deck.add(new Card("The Wall", "infantry"));
		deck.add(new Card("Skagos", "cavalry"));
		deck.add(new Card("Wolfswood", "artillery"));
		deck.add(new Card("Winterfell", "infantry"));
		deck.add(new Card("The Rills", "cavalry"));
		deck.add(new Card("The Neck", "artillery"));
		deck.add(new Card("The Flint Cliffs", "infantry"));
		deck.add(new Card("The Grey Cliffs", "cavalry"));
		deck.add(new Card("The Vale", "artillery"));
		deck.add(new Card("Riverlands", "infantry"));
		deck.add(new Card("Iron Islands", "cavalry"));
		deck.add(new Card("Westerlands", "artillery"));
		deck.add(new Card("Crownlands", "infantry"));
		deck.add(new Card("The Reach", "cavalry"));
		deck.add(new Card("Shield Lands", "artillery"));
		deck.add(new Card("Whispering Sound", "infantry"));
		deck.add(new Card("Storm Lands", "cavalry"));
		deck.add(new Card("Red Mountains", "artillery"));
		deck.add(new Card("Dorne", "infantry"));
		deck.add(new Card("Braavosi Coastland", "cavalry"));
		deck.add(new Card("Andalos", "artillery"));
		deck.add(new Card("Hills of Norvos", "infantry"));
		deck.add(new Card("Rhoyne Lands", "cavalry"));
		deck.add(new Card("Forrest of Qohor", "artillery"));
		deck.add(new Card("The Golden Fields", "infantry"));
		deck.add(new Card("The Disputed Lands", "cavalry"));
		deck.add(new Card("Rhoynian Veld", "artillery"));
		deck.add(new Card("Sar Mell", "infantry"));
		deck.add(new Card("Western Waste", "cavalry"));
		deck.add(new Card("Sea of Sighs", "artillery"));
		deck.add(new Card("Elyria", "infantry"));
		deck.add(new Card("Valyria", "cavalry"));
		deck.add(new Card("Sarnor", "artillery"));
		deck.add(new Card("Parched Fields", "infantry"));
		deck.add(new Card("Abandoned Lands", "cavalry"));
		deck.add(new Card("Western Grass Sea", "artillery"));
		deck.add(new Card("Kingdoms of the Jfeqevron", "infantry"));
		deck.add(new Card("Eastern Grass Sea", "cavalry"));
		deck.add(new Card("The Footprint", "artillery"));
		deck.add(new Card("Vaes Dothrak", "infantry"));
		deck.add(new Card("Realms of Jhogrvin", "cavalry"));
		deck.add(new Card("Ibben", "artillery"));
		deck.add(new Card("Painted Mountains", "infantry"));
		deck.add(new Card("Slaver's Bay", "cavalry"));
		deck.add(new Card("Lhazar", "artillery"));
		deck.add(new Card("Samyrian Hills", "infantry"));
		deck.add(new Card("Bayasabhad", "cavalry"));
		deck.add(new Card("Ghiscar", "artillery"));
		deck.add(new Card("The Red Waste", "infantry"));
		deck.add(new Card("Qarth", "cavalry"));
		deck.add(new Card("WILD", "WILD"));
		deck.add(new Card("WILD", "WILD"));
	}// end fillDeck

	/*
	 * adds a whole array list of cards to the discard pile
	 */
	public void addToDiscardPile(ArrayList<Card> cards, DiscardPile pile) {
		pile.addToPile(cards);
	}
}// end Deck Class
