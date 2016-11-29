package Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

public class Game {
	private ArrayList<Player> players;
	private Map gameMap;
	private Deck deck;
	private Country selectedCountry, aiSelectedCountry;
	private boolean placePhase, playPhase, reinforcePhase, deployPhase, attackPhase;
	private int humans;
	private int totalPlayers, armiesPlaced, playerLocation;
	private static Game theGame;
	private int numRedemptions;
	private boolean canPlace;

	private Game(int numOfHumanPlayers, int numOfAIPlayers) {
		humans = numOfHumanPlayers;
		totalPlayers = numOfHumanPlayers + numOfAIPlayers;
		armiesPlaced = 0;
		placePhase = true;
		playPhase = false;
		attackPhase = false;
		reinforcePhase = false;
		deployPhase = false;
		playerLocation = 0;
		numRedemptions = 0;
		canPlace = false;
		newGame();

	}// end constructor

	public static Game getInstance(int numOfHumanPlayers, int totalNumOfPlayers) {
		if (theGame == null)
			theGame = new Game(numOfHumanPlayers, totalNumOfPlayers);

		return theGame;
	}// end getInstance

	public void newGame() {
		selectedCountry = null;
		aiSelectedCountry = null;
		gameMap = Map.getInstance();
		deck = Deck.getInstance();
		// deck.shuffle();
		players = new ArrayList<>();
		addHumanPlayers(humans);
		addAI(totalPlayers - humans);
		numRedemptions = 0;

	}// end newGame

	public void setPlayers(ArrayList<Player> thePlayers) {
		players = thePlayers;
	}

	/*
	 * Shuffles the players so they're not always in the same old boring order.
	 * 
	 * This method works, but, because we never actually set the names or
	 * factions of the players inside of this class, the order is not accurately
	 * reflected in the GUI. I'm working on fixing it now
	 */
	public void startGame() {
		// Randomly picks a player from the total number of players
		int firstPlayer = (int) (Math.random() * totalPlayers);

		Player first = players.remove(firstPlayer);

		for (int i = 0; i < players.size(); i++) {
			// For a bit of extra randomness, shuffles the players!
			int ranToMove = (int) (Math.random() * totalPlayers - 1);
			// Remove a random player
			Player tmp = players.remove(ranToMove);
			// And reinsert him at the end
			players.add(tmp);
		} // end for

		// And lets the lucky winner go first!
		players.add(0, first);

		// calls roundOfPlacement to let any AIs who may have been set to
		// go first play their parts
		roundOfPlacement();
	}// end startGame

	public void roundOfPlacement() {
		while (isPlacePhase() && getCurrentPlayer() instanceof AI)
			aiTurn();
		// Just in case the switch between placing and reinforcing happened
		// in between two AIs
		if (isReinforcePhase())
			roundOfReinforcement();
	}// end roundOfPlacement

	public void roundOfReinforcement() {
		while (isReinforcePhase() && getCurrentPlayer() instanceof AI)
			aiTurn();
	}

	// this is called by the countryClickListener, and "places" an army in a
	// country, and sets the occupier to whichever player is up
	public void placeArmies(Country countryToPlace, int numToPlace) {
		// place initial 50 armies
		if (armiesPlaced < 50) {
			if (countryToPlace.getOccupier() == null) {
				players.get(playerLocation).occupyCountry(countryToPlace);
				countryToPlace.setOccupier(players.get(playerLocation));
				countryToPlace.setForcesVal(numToPlace);
				armiesPlaced++;
				if (armiesPlaced == 50) {
					placePhase = false;
					reinforcePhase = true;
				} // end if

				System.out.println(armiesPlaced);
				System.out.println("Next players turn");
				System.out.println("Army placed at : " + countryToPlace.toString());
				players.get(playerLocation).subtractFromAvailableTroops(numToPlace);

			} else {
				System.out.println("That country is already Occupied");
				System.out.println(armiesPlaced);

			} // end else
		} else if (isDeployPhase()) {
			if (players.get(playerLocation).getAvailableTroops() > 0
					&& countryToPlace.getOccupier().equals(players.get(playerLocation))) {
				countryToPlace.setForcesVal(numToPlace);
				players.get(playerLocation).subtractFromAvailableTroops(numToPlace);
				if (players.get(playerLocation).getAvailableTroops() == 0) {
					deployPhase = false;
					attackPhase = true;
				}

			}
		} else if (players.get(playerLocation).getAvailableTroops() > 0)
		// place remaining armies
		{
			// placePhase = false;
			// reinforcePhase = true;

			if (countryToPlace.getOccupier().equals(players.get(playerLocation))) {
				countryToPlace.setForcesVal(numToPlace);
				armiesPlaced++;
				System.out.println("Reinforced " + countryToPlace + " " + armiesPlaced);// selectedCountry.getName());
				players.get(playerLocation).subtractFromAvailableTroops(numToPlace);

			} else
				System.out.println("You don't occupy this country");

		} else {

			placePhase = false;
			reinforcePhase = false;
			playPhase = true;
			deployPhase = true;
			// we should now be back at the beginning of the players list
			// already,
			// so we need to give
			// that player units for the first turn
		}

	}// end placeArmies

	private void addAI(int numOfAI) {
		for (int i = 0; i < numOfAI; i++)
			players.add(new AI(AIStrat.EASY, totalPlayers));// this will change
															// later,
		// depending on what difficulty
		// is chosen;

	}// end addAi

	public Country getSelectedCountry() {
		return selectedCountry;
	}// end
		// getSelectedCountry

	public void setSelectedCountry(Country selectedCountry) {
		this.selectedCountry = selectedCountry;
	}// end setSelectedCountry

	private void addHumanPlayers(int numOfHumanPlayers) {
		for (int i = 0; i < numOfHumanPlayers; i++) {
			players.add(new HumanPlayer(totalPlayers));
		}

	}// end addHumanPlayers

	public Map getGameMap() {
		return gameMap;
	}// end getGameMap

	public ArrayList<Player> getPlayers() {
		return players;
	}// end getPlayers

	public void nextPlayer() {
		playerLocation++;
		if (playerLocation >= totalPlayers)
			playerLocation = 0;

		if (players.get(playerLocation) instanceof AI) {
			aiTurn();
		} else if (isDeployPhase())
			players.get(playerLocation).getTroops();

	}// end nextPlayer

	private void aiTurn() {
		boolean done = false;
		while (!done) {
			if (isPlacePhase()) {
				boolean placed = false;
				while (!placed) {
					placed = aiChoicePlacement();
				}
				done = true;
			} else if (isReinforcePhase()) {
				aiReinforcePlacement();
				done = true;
			} else if (isPlayPhase() && isDeployPhase()) {
				players.get(playerLocation).getTroops();
				while (players.get(playerLocation).getAvailableTroops() > 0) {
					aiReinforcePlacement();
				}
				deployPhase = false;
				attackPhase = true;
			} else if (isPlayPhase() && isAttackPhase()) {
				boolean finishedAttacking = false;
				while (!finishedAttacking) {
					finishedAttacking = ((AI) players.get(playerLocation)).aiAttack();
				}
				removeLosers();
				attackPhase = false;
				reinforcePhase = true;
			} else if (isPlayPhase() && isReinforcePhase()) {
				aiPlayReinforce();
				done = true;
			}
		}
		nextPlayer();
	}

	// calls the ai's reinforce method
	private void aiPlayReinforce() {
		((AI) players.get(playerLocation)).reinforce();

	}// end aiPlayReinforce

	public boolean isPlacePhase() {
		return placePhase;
	}// end isPlacePhase

	public boolean isPlayPhase() {
		return playPhase;
	}// end isPlayPhase

	public boolean isReinforcePhase() {
		return reinforcePhase;
	}// end isReinforcePhase

	public boolean isDeployPhase() {
		return deployPhase;
	}// end isDeplyPhase;

	public boolean aiChoicePlacement() {
		aiSelectedCountry = ((AI) players.get(playerLocation)).pickRandomCountry(gameMap.getCountries());
		if (checkIfCountryAvailable(aiSelectedCountry)) {

			placeArmies(aiSelectedCountry, 1);
			aiSelectedCountry = null;
			return true;
		}
		return false;
	}// end aiChoicePlacement

	public void aiReinforcePlacement() {
		Country aiSelectedCountry = null;
		while (aiSelectedCountry == null) {
			aiSelectedCountry = ((AI) players.get(playerLocation)).placeNewTroops();
		}
		placeArmies(aiSelectedCountry, 1);
		aiSelectedCountry = null;
	}// end aiReinforcePlacement

	public void aiUnitPlacement() {
		ArrayList<Country> selectedCountries = new ArrayList<>();
		while (selectedCountries.get(0) == null) {
			selectedCountries = ((AI) players.get(playerLocation)).countriesToReinforce();
		}
		int i = 0;
		while (i < selectedCountries.size()) {
			placeArmies(selectedCountries.get(i), 1);
			i++;
		}
	}

	private boolean checkIfCountryAvailable(Country countryToCheck) {

		return countryToCheck.getOccupier() == null;
	}// end checkIfCountryAvailable

	public Player getCurrentPlayer() {
		return players.get(playerLocation);
	}// end getCurrentPlayer

	// Main idea: player chooses which cards to redeem (max of 3)
	// if player has 5 cards, he MUST have a match, so call this function until
	// the player chooses the matching 3 cards
	public int redeemCards(Player player, ArrayList<Card> cardsToRedeem) {
		int numArmies = -1;
		if (cardsToRedeem.size() < 3) // if the user didn't select 3 cards
			return -1;
		Card one = cardsToRedeem.get(0);
		Card two = cardsToRedeem.get(1);
		Card three = cardsToRedeem.get(2);

		// redeemable: three of the same unit type, one of each type, two
		// different cards + wild
		// if can redeem:
		if ((one.getUnit().compareTo(two.getUnit()) == 0 && one.getUnit().compareTo(three.getUnit()) == 0
				&& three.getUnit().compareTo(two.getUnit()) == 0)
				|| (one.getUnit().compareTo(two.getUnit()) != 0 && one.getUnit().compareTo(three.getUnit()) != 0
						&& three.getUnit().compareTo(two.getUnit()) != 0)
				|| (one.getUnit().compareTo("WILD") == 0 && (two.getUnit().compareTo(three.getUnit()) != 0))
				|| (two.getUnit().compareTo("WILD") == 0 && (one.getUnit().compareTo(three.getUnit()) != 0))
				|| (three.getUnit().compareTo("WILD") == 0 && (one.getUnit().compareTo(two.getUnit()) != 0))) {

			numArmies = 0;
			numRedemptions++;
			switch (numRedemptions) {
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
				numArmies = 15 + 5 * (numRedemptions - 6);
				break;
			}

			// if any one of the redeemable cards contains a country that the
			// player has, add 2 armies to that country.
			boolean added = false;
			for (Card c : cardsToRedeem) {
				for (Country t : player.getCountries()) {
					if (c.getCountry().compareTo(t.getName()) == 0) {
						// add 2 armies to that country
						added = true;
						int currentForces = t.getForcesVal();
						System.out.println("current Forces" + currentForces + t.getName());
						t.setForcesVal(2);
						System.out.println("updated Forces" + t.getForcesVal() + t.getName());
					}
				}
			}
			if (!added)
				System.out.println("no country cards to redeem");
		} else
			System.out.println("unable to redeem cards");
		return numArmies;
		// if numArmies is -1 when returned, cards cannot be redeemed
	}// end redeemCards

	// pops up a pane to ask how many units to move, which returns a string
	// it then tries to parse that string into an int, and if it does compares
	// it to the total
	// number of units on a country. If it is larger, or equal to the total
	// number, throws an error, otherwise returns that number
	// to the gui
	public int getUnitsToMove(Country countryToRemoveUnits) {
		boolean moveFlag = false, continueFlag = false;
		int totalUnits = countryToRemoveUnits.getForcesVal(), unitsToReturn = 0;
		;
		String unitsToMove = "";

		while (!moveFlag) {
			unitsToMove = JOptionPane.showInputDialog("How Many armies? You must leave 1.");
			try {
				unitsToReturn = Integer.parseInt(unitsToMove);
				continueFlag = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "That was invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			if (continueFlag) {
				if (unitsToReturn >= totalUnits) {
					JOptionPane.showMessageDialog(null, "You must leave 1 army.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					// theGame.getSelectedCountry().removeUnits(unitsToReturn);
					moveFlag = true;
				}
			}
		}
		return unitsToReturn;

	}// end unitsToReturn

	public int getArmiesToAttack(Country countryToRemoveUnits) {
		boolean moveFlag = false, continueFlag = false;
		int totalUnits = countryToRemoveUnits.getForcesVal(), unitsToReturn = 0;
		String unitsToMove = "";

		while (!moveFlag) {
			unitsToMove = JOptionPane.showInputDialog("How many armies do you want to attack with?");
			try {
				unitsToReturn = Integer.parseInt(unitsToMove);
				continueFlag = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "That was invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			if (continueFlag) {
				if (unitsToReturn > totalUnits) {
					JOptionPane.showMessageDialog(null, "Invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					// theGame.getSelectedCountry().removeUnits(unitsToReturn);
					moveFlag = true;
				}
			}
		}
		return unitsToReturn;

	}// end getArmiesToAttack

	public boolean moveUnitsToCountry(int numUnits, Country fromCountry, Country toCountry, Player current) {

		boolean result = false;
		ArrayList<Country> visited = new ArrayList<Country>();
		visited.add(fromCountry);
		findPath(fromCountry, visited, toCountry, current);
		if (canPlace) {
			toCountry.setForcesVal(numUnits);
			fromCountry.removeUnits(numUnits);
			result = true;
		}
		canPlace = false;
		return result;
	}// end moveUnitsToCountry

	private void findPath(Country fromCountry, ArrayList<Country> visited, Country toCountry, Player currentP) {
		if (canPlace)
			return;
		ArrayList<Country> countries = visited.get(visited.size() - 1).getNeighbors();
		for (Country c : countries) {
			if (!(c.getOccupier().equals(currentP)))
				continue;
			if (visited.contains(c))
				continue;
			if (c.equals(toCountry)) {
				visited.add(c);
				canPlace = true;
				printPath(visited);
				visited.remove(visited.size() - 1);
				break;
			}
		}
		for (Country c : countries) {
			if (!(c.getOccupier().equals(currentP)))
				continue;
			if (visited.contains(c) || c.equals(toCountry)) {
				continue;
			}
			visited.add(c);
			findPath(fromCountry, visited, toCountry, currentP);
			visited.remove(visited.size() - 1);

		}
	}// end findPath

	private void printPath(ArrayList<Country> visited) {
		for (Country node : visited) {
			System.out.print(node);
			System.out.print(" ");
		}
		System.out.println();
	}// end printPath

	public String getPhase() {
		if (placePhase)
			return "Place Phase";
		else if (playPhase && deployPhase)
			return "Deploy Phase";
		else if (reinforcePhase)
			return "Reinforce Phase";
		else if (playPhase && attackPhase)
			return "Attack Phase";
		return null;
	}// end getPhase

	public String attack(Country yours, Country theirs, int numArmies) {
		String result = "";
		if (numArmies <= theirs.getForcesVal()) {
			// theirs.setForcesVal(numArmies);
			yours.removeUnits(numArmies); // you lose the armies fought with
		} else if (theirs.getForcesVal() < numArmies) {
			theirs.getOccupier().loseCountry(theirs);
			theirs.removeUnits(theirs.getForcesVal());
			theirs.setForcesVal(numArmies);
			theirs.setOccupier(yours.getOccupier());
			yours.getOccupier().occupyCountry(theirs);
			yours.removeUnits(numArmies);
			result = yours.toString();
		}
		return result;
	}// end attack

	public boolean isAttackPhase() {
		return attackPhase;
	}// end isAttackPhase

	public void skipAttackPhase() {
		attackPhase = false;
		reinforcePhase = true;
	}// end skipAttackPhase

	public void finishTurn() {
		deployPhase = true;
		attackPhase = false;
		reinforcePhase = false;
		nextPlayer();
	}// end finishTurn

	//checks if all countries are occupied by the same player, if so returns true, otherwise returns false
	public boolean isFinished() {
		int countryCounter = 0, i = 0, j = 1;
		Country countries[] = getGameMap().getCountries();
		while (j < 50) {
			if (countries[i].getOccupier().equals(countries[j].getOccupier())) {
				countryCounter++;
			}
			i++;
			j++;
		}

		if (countryCounter == 50)
			return true;

		return false;
	}// end isFinished

	//checks if all players have at least one country. If they do not, remove them from the game.
	public void removeLosers() {
		ArrayList<Player> playersToRemove = new ArrayList<>();
		for (Player player : players) {
			if (player.getCountries().size() == 0) {
				System.out.println(player.getName() + " has been defeated.");
				playersToRemove.add(player);
			}
		}

		players.removeAll(playersToRemove);
		//TODO add cards that players that lost had into the discard pile, if there is one
	}// end removeLosers
}// end GameClasss
