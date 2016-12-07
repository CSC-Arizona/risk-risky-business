/*
 * 	File:		AI.java
 * 	Purpose:	AI class extends player and contains control of AI type players, both easy and hard.
 */

package Model;

import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JMenuItem;

public class AI extends Player implements Serializable {

	private JMenuItem myDiff;
	private Random rand;
	private AIStrategy strategy;
	private ArrayList<Country> fringes;
	
	//DELETE USSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
	static int w1, w2,f1,w3,f2,f3,w4,w5,w6,w7,w8,w9,wa,fa;
	// private Game theGame;

	public AI(AIStrategy strat, int numOfPlayers) {
		super(numOfPlayers);
		fringes = null;
		strategy = strat;
		strategy.setMe(this);
		rand = new Random();
	}// end AI constructor

	// checks an ai's countries neighbors, to see if they are occupied. if they
	// are, go to the next one, otherwise
	// return that country as a selection. Used for placement in the first turn.
	public Country checkAllNeighbors() {
		int i = 0, j = 0;
		// get my first countries neighbors
		ArrayList<Country> neighbors = getCountries().get(i).getNeighbors();
		while (i < neighbors.size()) {
			//System.out.println("While1: "+ ++w1);
			j = 0;
			while (j < neighbors.size() && neighbors.get(j).getOccupier() != null) {
				j++;
				//System.out.println("While2: "+ ++w2);
			}

			if (j < neighbors.size())
				return neighbors.get(j);

			i++;
			if (i < getCountries().size())
				neighbors = getCountries().get(i).getNeighbors();
		}
		return null;
	}// end checkAllNeighbors

	public int chooseMyDiceToRoll(int max) {
		return max;
	}// end chooseMyDice

	public Country pickRandomOwnedCountry() {
		Random rand = new Random();
		int randNum = rand.nextInt(getCountries().size());
		return getCountries().get(randNum);
	}// end pickRandomOwnedCountry

	// returns true if it is finished attacking, and false otherwise
	// grabs a country to attack, and the country that it is attacking from
	// if there is no country to attack, return. if it loses a battle, check if
	// there are still other countries it can attack
	public String aiAttack() {

		// Country attacking = getCountryToAttack();
		// if (attacking == null)
		// return null;
		// Country attackingFrom = findAttackingCountry(attacking);
		//
		// // change this for dice roll later, but for now, just take over
		// if (attackingFrom.getForcesVal() - 1 > attacking.getForcesVal()) {
		// String str = this.getName() + " defeated " +
		// attacking.getOccupier().getName() + " and took " +
		// attacking.getName() + ".\n";
		// int oldForces = attacking.getForcesVal();
		// attacking.getOccupier().loseCountry(attacking);
		// attacking.removeUnits(oldForces);
		// attacking.addForcesVal(attackingFrom.getForcesVal() - 1);
		// attacking.setOccupier(this);
		// this.occupyCountry(attacking);
		// System.out.println(this.getName() + " took " + attacking.getName());
		// attackingFrom.removeUnits(attackingFrom.getForcesVal() - 1);
		// return str;
		return null;
	}// end aiAttack

	/*
	 * for when dice roll exists if(myStrat == AIStrat.EASY){
	 * if(attackingFrom.getForcesVal() > 1){ do dice roll stuff against
	 * attacking and take all units but 1 from attackinFrom return false; } else
	 * return true; } else{ if(attackingFrom.getForces() ==
	 * attacking.getForces() or up to 2 less) do dice roll stuff against
	 * attacking and take all units but 1 from attacking from return false else
	 * return true;t }
	 */

	public boolean finishedAttacking() {
		int i = 0;
		for(Country country : findFringeCountries())
		{
			//System.out.println("For1: "+ ++f1);
			//System.out.println("Fringe at " + country.getName() + " with " + country.getForcesVal() + " forces");
			if(country.getForcesVal() == 1)
				i++;
		}
		
		if(i == findFringeCountries().size())
			return true;
		else if(strategy instanceof MediumAI || strategy instanceof HardAI)
		{
			if(strategy.findCountriesToAttack() == null)
				return true;
		}
		
		return false;

	}// end finishedAttacking

	// returns a country it can attack
	public Country getCountryToAttack() {
//		boolean found = false;
//		ArrayList<Country> countries = getCountries();
//		Country attackMe = null;
//		
//		while (!found){
//			System.out.println("Whilea: " + ++wa);
//			int ran = (int)(Math.random() * countries.size());
//			attackMe = countries.get(ran);
//			
//			for (Country c : attackMe.getNeighbors()){
//				System.out.println("Fa: " + ++fa);
//				if (!this.equals(c.getOccupier())){
//					found = true;
//					break;
//				}//end if
//			}//end for
//			
//		}//end while
//		
		// System.out.println("get country to attack");
		Country attackMe = pickRandomFromList(findCountriesToAttack());
		return attackMe;
	}// end
	
	

	// picks a random country from the list of countries to attack
	private Country pickRandomFromList(ArrayList<Country> countriesToAttack) {
		if (countriesToAttack == null)
			return null;

		Random rand = new Random();
		int randInt = 0;

		randInt = rand.nextInt(countriesToAttack.size());

		return countriesToAttack.get(randInt);
	}// end pickRandomFromList

	// creates the ai's menuItem for changing difficulty
	public void makeMenuItem(int i, ActionListener aiDiffChangeListener) {
		myDiff = new JMenuItem("AI " + i);
		myDiff.addActionListener(aiDiffChangeListener);
		myDiff.setActionCommand(String.valueOf(i));
	}// end makeMenuItem

	// returns its jMenuItem
	public JMenuItem getMenuItem() {
		return myDiff;
	}// end getMenuItem

	// returns the ai's current strategy as a string, used for checking if the
	// ai difficulty menu in the gui was working

	// returns a randomlist of countries to add units to out of the ai's owned
	// countries
	private ArrayList<Country> pickSetOfRandomOwnedCountry() {
		ArrayList<Country> countries = new ArrayList<>();
		Random rand = new Random();
		int randNum = 0;
		int i = 0;
		while (getAvailableTroops() > i) {
			//System.out.println("While3: "+ ++w3);//next: w4, f2
			i++;
			randNum = rand.nextInt(getCountries().size());
			countries.add(getCountries().get(randNum));
		}
		return countries;
	}// end pickSetOfRandomOwnedCountry

	// gets all fringe countries, then for each neihbor that fringe country has,
	// if it isn't owned by me
	// check if i have more units on my country than that country, if I do, add
	// that country to my list of countriesWorthAttacking
	public ArrayList<Country> findCountriesToAttack() {
		// System.out.println("find countries to attack");
		ArrayList<Country> fringeCountries = findFringeCountries();
		ArrayList<Country> countriesWorthAttacking = new ArrayList<>();
		for (Country country : fringeCountries) {
			//System.out.println("For2: "+ ++f2);//next: w4, f3
			ArrayList<Country> neighbors = country.getNeighbors();
			for (Country neighboringCountry : neighbors) {
				//System.out.println("For3: "+ ++f3);//next: w4, f4
			//	if (neighboringCountry.getOccupier().getFaction().compareTo(this.getFaction()) != 0) {
				if (this.equals(neighboringCountry.getOccupier())){
					if (country.getForcesVal() - 1 > neighboringCountry.getForcesVal())
						countriesWorthAttacking.add(neighboringCountry);
//=======
//				if (!neighboringCountry.getOccupier().equals(this)) {
//					countriesWorthAttacking.add(neighboringCountry);
//>>>>>>> 18daf5c34a1b194e76b246f361d946f663f0cfa2
				} // end if
			} // end for
		} // end for

		if (countriesWorthAttacking.size() == 0)
			return null;

		return countriesWorthAttacking;
	}// end findCountriesToAttack

	// starts at first country, checks if it is surrounded by friendlies, if it
	// is
	// moves all of its units except for one to its neighbors

	public Country pickRandomFromFringe() {
		ArrayList<Country> fringeCountries = findFringeCountries();

		int randNum = 0;
		// System.out.println(fringeCountries.size() + " size of list to choose
		// from");
		if (fringeCountries.size() == 0)
			return null;
		randNum = rand.nextInt(fringeCountries.size());
		return fringeCountries.get(randNum);
	}

	public Country pickRandomCountry() {
		Map map = Map.getInstance(0);
		Country[] countries = map.getCountries();
		int randNum = rand.nextInt(50);
		return countries[randNum];
	}

	public ArrayList<Country> findFringeCountries() {
		if (fringes != null)
			return fringes;
		
		ArrayList<Country> fringeCountries = new ArrayList<>();

		int i = 0, j = 0;
		ArrayList<Country> neighbors = getCountries().get(i).getNeighbors();
		while (i < getCountries().size()) {
			//System.out.println("While4: "+ ++w4);//next: w5, f4
			j = 0;
			while (j < neighbors.size()) {
				//System.out.println("While5: "+ ++w5);//next: w6, f4
				if (!this.equals(neighbors.get(j).getOccupier())) {
					fringeCountries.add(getCountries().get(i));
					j = neighbors.size();
				}
				j++;
			}
			i++;
			if (i < getCountries().size())
				neighbors = getCountries().get(i).getNeighbors();
		}
		
		fringes = fringeCountries;
		return fringeCountries;
	}

	@Override
	public ArrayList<Card> redeemCards() {
		if (getCards().size() >= 5) {
			return findmyCardsToRedeem();
		} // end if
		else
			return null;
	}

	
	private ArrayList<Card> findmyCardsToRedeem(){
		ArrayList<Card> cards = null;
		
		cards = findThreeInfantry();
		if (cards != null)
			return cards;
		
		cards = findThreeCalvary();
		if (cards != null)
			return cards;
		
		cards = findThreeArtillery();
		if (cards != null)
			return cards;
		
		cards = findOneOfEach();
		if (cards != null)
			return cards;
		
		throw new IllegalStateException("Didn't redeem cards!");
		
	}//end findMyCardsToRedeem
	
	private ArrayList<Card> findOneOfEach(){
		Card inf = new Card(null, "infantry", false);
		Card cal = new Card(null, "cavalry", false);
		Card art = new Card(null, "artillery", false);
		ArrayList<Card> cards = getCards();
		boolean infFound=false, calFound=false, artFound=false;
		ArrayList<Card> trade = new ArrayList<Card>();
		
		int i=0;
		while (i<cards.size() && trade.size()<3){ 
			//System.out.println("While6: "+ ++w6);//next: w6, f4
			//If it's an infantry or wild, add it
			if (!infFound){
				if (cards.get(i).equals(inf)){
					trade.add(cards.get(i));
					infFound = true;
					continue;
				}//end if
					
			}//end if
			
			if (!calFound){
				if (cards.get(i).equals(cal)){
					trade.add(cards.get(i));
					calFound = true;
					continue;
				}//end if
			}//end if
			
			if (!artFound){
				if (cards.get(i).equals(art)){
					trade.add(cards.get(i));
					artFound = true;
					continue;
				}//end if
			}//end if
			
			i++;
		}//end while
		
		if (trade.size()==3)
			return trade;
		else
			return null;
	}//end one of each
	
	private ArrayList<Card> findThreeInfantry(){
		Card inf = new Card(null, "infantry", false);
		ArrayList<Card> cards = getCards();
		ArrayList<Card> trade = new ArrayList<Card>();
		
		int i=0;
		while (i<cards.size() && trade.size()<3){
			//System.out.println("While7: "+ ++w7);//next: w6, f4
			//If it's an infantry or wild, add it
			if (cards.get(i).equals(inf))
				trade.add(cards.get(i));
			
			i++;
		}//end while
		
		if (trade.size()==3)
			return trade;
		else
			return null;
	}// end infantry
	
	private ArrayList<Card> findThreeCalvary(){
		Card cav = new Card(null, "cavalry", false);
		ArrayList<Card> cards = getCards();
		ArrayList<Card> trade = new ArrayList<Card>();
		
		int i=0;
		while (i<cards.size() && trade.size()<3){
			//System.out.println("While8: "+ ++w8);//next: w6, f4
			//If it's an infantry or wild, add it
			if (cards.get(i).equals(cav))
				trade.add(cards.get(i));
			
			i++;
		}//end while
		
		if (trade.size()==3)
			return trade;
		else
			return null;
	}// end infantry
	
	private ArrayList<Card> findThreeArtillery(){
		Card art = new Card(null, "artillery", false);
		ArrayList<Card> cards = getCards();
		ArrayList<Card> trade = new ArrayList<Card>();
		
		int i=0;
		while (i<cards.size() && trade.size()<3){
			//System.out.println("While9: "+ ++w9);//next: w6, f4
			//If it's an infantry or wild, add it
			if (cards.get(i).equals(art))
				trade.add(cards.get(i));
			
			i++;
		}//end while
		
		if (trade.size()==3)
			return trade;
		else
			return null;
	}// end infantry
	
	
	
	
//	private ArrayList<Card> findmyCardsToRedeem() {
//
//		ArrayList<Card> myThreeCards = new ArrayList<>();
//		int infantryCount = 0, calvaryCount = 0, artilleryCount = 0, wildCount = 0;
//		// step through 5 cards, and count how many of each
//		for (Card card : getCards()) {
//			switch (card.getUnitType()) {
//			case 1:
//				infantryCount++;
//				break;
//			case 2:
//				calvaryCount++;
//				break;
//			case 3:
//				artilleryCount++;
//				break;
//			case 0:
//				wildCount++;
//				break;
//			}
//		}
//		if (infantryCount >= 3 || (wildCount == 1 && infantryCount >= 2) || (wildCount == 2 && infantryCount >= 1)) {
//			myThreeCards = findThreeInfantry();
//		} else if (calvaryCount >= 3 || (wildCount == 1 && calvaryCount >= 2)
//				|| (wildCount == 2 && calvaryCount >= 1)) {
//			myThreeCards = findThreeCalvary();
//		} else if (artilleryCount >= 3 || (wildCount == 1 && calvaryCount >= 2)
//				|| (wildCount == 2 && artilleryCount >= 1)) {
//			myThreeCards = findThreeArtillery();
//		} else {
//			myThreeCards = findOneOfEach();
//		}
//
//		return myThreeCards;
//	}// end findmyCardsToRedeem
//
//	private ArrayList<Card> findOneOfEach() {
//		ArrayList<Card> myThreeCards = new ArrayList<>();
//		boolean infantry = false, calvary = false, artillery = false;
//		for (Card card : getCards()) {
//			if (!infantry && (card.getUnit().compareTo("infantry") == 0 || card.getUnit().compareTo("WILD") == 0)) {
//				myThreeCards.add(card);
//				infantry = true;
//			}
//
//			if (!calvary && (card.getUnit().compareTo("calvary") == 0 || card.getUnit().compareTo("WILD") == 0)) {
//				myThreeCards.add(card);
//				calvary = true;
//			}
//
//			if (!artillery && (card.getUnit().compareTo("artillery") == 0 || card.getUnit().compareTo("WILD") == 0)) {
//				myThreeCards.add(card);
//				artillery = true;
//			}
//		}
//		return myThreeCards;
//	}// end findOneOfEach
//
//	private ArrayList<Card> findThreeArtillery() {
//		ArrayList<Card> threeArtillery = new ArrayList<>();
//		for (Card card : getCards()) {
//			if (card.getUnit().compareTo("artillery") == 0) {
//				threeArtillery.add(card);
//			}
//
//			if (threeArtillery.size() == 3) {
//				break;
//			}
//		}
//		return threeArtillery;
//	}// end findThreeArtillery
//
//	private ArrayList<Card> findThreeCalvary() {
//		ArrayList<Card> threeCalvary = new ArrayList<>();
//		for (Card card : getCards()) {
//			if (card.getUnit().compareTo("calvary") == 0) {
//				threeCalvary.add(card);
//			}
//
//			if (threeCalvary.size() == 3) {
//				break;
//			}
//		}
//		return threeCalvary;
//	}// end findThreeCalvary
//
//	private ArrayList<Card> findThreeInfantry() {
//
//		ArrayList<Card> threeInfantry = new ArrayList<>();
//		for (Card card : getCards()) {
//			if (card.getUnit().compareTo("infantry") == 0 || card.getUnit().compareTo("WILD") == 0) {
//				threeInfantry.add(card);
//			}
//
//			if (threeInfantry.size() == 3) {
//				break;
//			}
//		}
//		return threeInfantry;
//	}// end findThreeInfantry

	public AIStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(AIStrategy strat) {
		strategy = strat;
		strategy.setMe(this);
	}

}
