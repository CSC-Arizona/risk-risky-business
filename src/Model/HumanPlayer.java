package Model;

import java.util.ArrayList;

public class HumanPlayer extends Player {
	
	private ArrayList<Card> redeemable = new ArrayList<Card>();
	private int redemptions=0;

	public HumanPlayer(int numOfPlayers) {
		super(numOfPlayers);
	}// end constructor
	
	public void setCardsToRedeem(ArrayList<Card> cards, int numRedemptions){
		redeemable = cards;
		redemptions = numRedemptions;
	}//end setCardsToRedeem
	
	@Override
	public int redeemCards() {
		ArrayList<Card> cardsToRedeem = redeemable;
		int numArmies = -1;
		if (cardsToRedeem.size() < 3){ // if the user didn't select 3 cards
			return -1;
		}
		Card one = cardsToRedeem.get(0);
		Card two = cardsToRedeem.get(1);
		Card three = cardsToRedeem.get(2);

		// redeemable: three of the same unit type, one of each type, two + wild
		// if can redeem:
		if ((one.getUnit().compareTo(two.getUnit()) == 0 && one.getUnit().compareTo(three.getUnit()) == 0
				&& three.getUnit().compareTo(two.getUnit()) == 0)
				|| (one.getUnit().compareTo(two.getUnit()) != 0 && one.getUnit().compareTo(three.getUnit()) != 0
						&& three.getUnit().compareTo(two.getUnit()) != 0)
				|| (one.getUnit().compareTo("WILD") == 0 )
				|| (two.getUnit().compareTo("WILD") == 0 )
				|| (three.getUnit().compareTo("WILD") == 0)) {

			numArmies = 0;
			redemptions++;
			switch (redemptions) {
			case 1:
				numArmies = 4;
				break;
			case 2:
				numArmies = 6;
				break;
			case 3:
				numArmies = 8;
				break;
			case 4:
				numArmies = 10;
				break;
			case 5:
				numArmies = 12;
				break;
			case 6:
				numArmies = 15;
				break;
			default:
				numArmies = 15 + 5 * (redemptions - 6);
				break;
			}

			// if any one of the redeemable cards contains a country that the
			// player has, add 2 armies to that country.
			boolean added = false;
			for (Card c : cardsToRedeem) {
				for (Country t : this.getCountries()) {
					if (c.getCountry().compareTo(t.getName()) == 0) {
						// add 2 armies to that country
						added = true;
						int currentForces = t.getForcesVal();
						System.out.println("current Forces" + currentForces + t.getName());
						t.setForcesVal(2);
						System.out.println("updated Forces" + t.getForcesVal() + t.getName());
						break; //can only redeem a country card for extra armies once per turn
					}
				}
				if(added)
					break;
			}
			if (!added)
				System.out.println("no country cards to redeem");
		} else
			System.out.println("unable to redeem cards");
		
		return numArmies;
		// if numArmies is -1 when returned, cards cannot be redeemed
	}// end redeemCards

}// end HumanPlayer class
