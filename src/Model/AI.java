/*	File:		AI.java
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

	private int numAttacks;
	
	//DELETE USSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
	static int w1, w2,f1,w3,f2,f3,w4,w5,w6,w7,w8,w9,wa,fa;
	// private Game theGame;


	public AI(AIStrategy strat, int numOfPlayers) {
		super(numOfPlayers);
		numAttacks = 0;
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
			j = 0;
			while (j < neighbors.size() && neighbors.get(j).getOccupier() != null) {
				j++;
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
	public boolean finishedAttacking() {
		
		//If you're close to finished, just conquerrrrr
		if (getCountries().size() > 44)
			return false;
		
	

		
		
		
		int i = 0;
		for (Country country : findFringeCountries()) {
			if (country.getForcesVal() == 1)
				i++;
		}

		if (i == findFringeCountries().size())
			return true;
		else if (strategy instanceof MediumAI || strategy instanceof HardAI) {
			if (strategy.findCountriesToAttack() == null)
				return true;
		}
//		
//		return false;
		
		return false;


	}// end finishedAttacking

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
//			System.out.println("While3: "+ ++w3);//next: w4, f2

			i++;
			randNum = rand.nextInt(getCountries().size());
			countries.add(getCountries().get(randNum));
		}
		return countries;
	}// end pickSetOfRandomOwnedCountry

	// starts at first country, checks if it is surrounded by friendlies, if it
	// is
	// moves all of its units except for one to its neighbors

	public Country pickRandomFromFringe() {
		ArrayList<Country> fringeCountries = findFringeCountries();

		int randNum = 0;
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
//		if (fringes != null)
//			return fringes;

		ArrayList<Country> fringeCountries = new ArrayList<>();

		int i = 0, j = 0;
		ArrayList<Country> neighbors = getCountries().get(i).getNeighbors();
		while (i < getCountries().size()) {
			j = 0;
			while (j < neighbors.size()) {
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

	private ArrayList<Card> findmyCardsToRedeem() {
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

	}// end findMyCardsToRedeem

	private ArrayList<Card> findOneOfEach() {
		Card inf = new Card(null, "infantry", false);
		Card cal = new Card(null, "cavalry", false);
		Card art = new Card(null, "artillery", false);
		ArrayList<Card> cards = getCards();
		boolean infFound = false, calFound = false, artFound = false;
		ArrayList<Card> trade = new ArrayList<Card>();

		int i = 0;
		while (i < cards.size() && trade.size() < 3) {
			// If it's an infantry or wild, add it
			if (!infFound) {
				if (cards.get(i).equals(inf)) {
					trade.add(cards.get(i));
					infFound = true;
					continue;
				} // end if

			} // end if

			if (!calFound) {
				if (cards.get(i).equals(cal)) {
					trade.add(cards.get(i));
					calFound = true;
					continue;
				} // end if
			} // end if

			if (!artFound) {
				if (cards.get(i).equals(art)) {
					trade.add(cards.get(i));
					artFound = true;
					continue;
				} // end if
			} // end if

			i++;
		} // end while

		if (trade.size() == 3)
			return trade;
		else
			return null;
	}// end one of each

	private ArrayList<Card> findThreeInfantry() {
		Card inf = new Card(null, "infantry", false);
		ArrayList<Card> cards = getCards();
		ArrayList<Card> trade = new ArrayList<Card>();

		int i = 0;
		while (i < cards.size() && trade.size() < 3) {
			// If it's an infantry or wild, add it
			if (cards.get(i).equals(inf))
				trade.add(cards.get(i));

			i++;
		} // end while

		if (trade.size() == 3)
			return trade;
		else
			return null;
	}// end infantry

	private ArrayList<Card> findThreeCalvary() {
		Card cav = new Card(null, "cavalry", false);
		ArrayList<Card> cards = getCards();
		ArrayList<Card> trade = new ArrayList<Card>();

		int i = 0;
		while (i < cards.size() && trade.size() < 3) {
			// If it's an infantry or wild, add it
			if (cards.get(i).equals(cav))
				trade.add(cards.get(i));

			i++;
		} // end while

		if (trade.size() == 3)
			return trade;
		else
			return null;
	}// end infantry

	private ArrayList<Card> findThreeArtillery() {
		Card art = new Card(null, "artillery", false);
		ArrayList<Card> cards = getCards();
		ArrayList<Card> trade = new ArrayList<Card>();

		int i = 0;
		while (i < cards.size() && trade.size() < 3) {
			// If it's an infantry or wild, add it
			if (cards.get(i).equals(art))
				trade.add(cards.get(i));

			i++;
		} // end while

		if (trade.size() == 3)
			return trade;
		else
			return null;
	}// end infantry


	public AIStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(AIStrategy strat) {
		strategy = strat;
		strategy.setMe(this);
	}

}
