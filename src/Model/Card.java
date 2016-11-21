package Model;

import java.util.ArrayList;

public class Card {
	private String country;
	private String unit;
	
	public Card(String country, String unit){
		this.country = country;
		this.unit = unit;
	}
	
	public String getCountry(){
		return country;
	}
	
	public String getUnit(){
		return unit;
	}
	
	public int redeemCards(Player player, ArrayList<Card> cardsToRedeem){
		int numArmies = 0;
		//TODO: check if cardsToRedeem are valid cards to redeem (as far as unit type)
		
		//TODO: if cards redeem on specific country, ensure that player has that country
		return numArmies;
	}

}
