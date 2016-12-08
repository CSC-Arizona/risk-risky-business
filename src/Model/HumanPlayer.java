/*

	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		HumanPlayer.java
 * 	Purpose:	HumanPlayer class extends player and contains control of human type players.
 */

package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class HumanPlayer extends Player implements Serializable{
	private ArrayList<Card> cardsToRedeem;


	public HumanPlayer(int numOfPlayers) {
		super(numOfPlayers);
		cardsToRedeem = null;
	}// end constructor

	
	public void setCardsToRedeem(ArrayList<Card> cards){
		cardsToRedeem = cards;
	}//end setCardsToredeem
	
	/*
	 * Creates an arraylist of Card called tmp, sets that equal to cardsToRedeem, then sets cardsToRedeem to null, nad returns temp
	 */
	@Override
	public ArrayList<Card> redeemCards() {
		ArrayList<Card> tmp = cardsToRedeem;
		cardsToRedeem = null;
		return tmp;

	}// end redeemCards

}// end HumanPlayer class
