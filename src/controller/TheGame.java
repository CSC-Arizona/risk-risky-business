package controller;

/* Name: TheGame (What's the name of TheGame??)
 * File: TheGame.java
 * Purpose: Coordinates games of risk between human and AI players. 
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Model.AI;
import Model.Card;
import Model.Country;
import Model.Deck;
import Model.Dice;
import Model.DiscardPile;
import Model.EasyAI;
import Model.HardAI;
import Model.HumanPlayer;
import Model.Map;
import Model.MediumAI;
import Model.Player;

public class TheGame implements Serializable {
	private ArrayList<Player> players;
	private Player currentPlayer;
	private Map gameMap;
	private Deck deck;
	private DiscardPile discard;
	private Country selectedCountry;
	private Country moveFrom, moveTo;
	private boolean placePhase, reinforcePhase, deployPhase, attackPhase,
			gameOver, redeemCardPhase, mainGamePhase, cardEarned, gameStarted;
	private static TheGame theGame;
	private boolean tournamentMode, canPlace;
	private String gameLog;
	private int humans, ais, totalPlayers;
	private int numRedemptions, countriesClaimed;
	private int countriesBefore, countriesAfter;
	private ArrayList<Dice> attackDice;
	private ArrayList<Dice> defenseDice;
	private int maxAttackDice, maxDefendDice;
	private ArrayList<Card> cardsToRedeem;
	public static final String FILE_NAME = "game.ser";
	private int attackerHits;
	private int numAttacks, numDeploys, numReinforces, numCard;
	private double attackTime, deployTime, reinforceTime, cardTime;

	/**********************************************************************************
	 ********************************** Game Creation************************************
	 **********************************************************************************/
	private TheGame(int numOfHumanPlayers, int numOfAIPlayers, boolean tourny) {
		// Getting the sizes
		numAttacks = 0;
		humans = numOfHumanPlayers;
		ais = numOfAIPlayers;
		totalPlayers = humans + ais;
		countriesClaimed = 0;
		countriesBefore = 0;
		countriesAfter = 0;
		cardEarned = false;
		tournamentMode = tourny;
		gameStarted = false;
		maxAttackDice = 3;
		maxDefendDice = 2;
		if (!tournamentMode)
			newGame();
		else
			newGame(numOfAIPlayers);
	}// end constructor

	/*
	 * Return an instance of the singleton game
	 */
	public static TheGame getInstance(int numOfHumanPlayers,
			int totalNumOfPlayers, boolean tourny) {
		if (theGame == null)
			theGame = new TheGame(numOfHumanPlayers, totalNumOfPlayers, tourny);

		return theGame;
	}// end getInstance

	/*
	 * clears the instance variable game so a new game can be created
	 */
	public void clear() {
		theGame = null;
	}// end clear

	/*
	 * newGame
	 * 
	 * Create a new game with the specified number of humans and AIs inside of
	 * the game
	 */
	public void newGame() {
		if (players != null)
			players.removeAll(players);
		numAttacks = 0;
		selectedCountry = null;
		gameMap = Map.getInstance(0);
		gameMap = gameMap.newMap();
		deck = Deck.getInstance();
		deck = deck.newDeck();
		discard = new DiscardPile();
		players = new ArrayList<>();
		addHumanPlayers();
		addAI();
		currentPlayer = players.get(0);
		numRedemptions = 0;
		canPlace = false;
		gameOver = false;
		countriesClaimed = 0;
		attackerHits = -1;
		gameStarted = false;
		cardEarned = false;
		gameLog = "";
		changeToPlacementPhase();
	}// end newGame

	/*
	 * newGame(int i)
	 * 
	 * Create a new game for tournament mode i - the number of AI players
	 */
	public void newGame(int i) {
		if (players != null)
			players.removeAll(players);
		numAttacks = 0;
		gameOver = false;
		selectedCountry = null;
		countriesClaimed = 0;
		totalPlayers = 6;
		gameMap = Map.getInstance(1);
		gameMap = gameMap.newTourneyMap();
		deck = Deck.getInstance();
		deck = deck.newDeck();
		discard = new DiscardPile();
		players = new ArrayList<>();
		mainGamePhase = false;
		placePhase = true;
		reinforcePhase = false;
		redeemCardPhase = false;
		deployPhase = false;
		attackPhase = false;
		gameLog = "";
		cardEarned = false;

		// Counters
		attackTime = 0;
		deployTime = 0;
		reinforceTime = 0;
		cardTime = 0;

		addAI();
		canPlace = false;
		numRedemptions = 0;

	}// end newGame

	/*
	 * addAI
	 * 
	 * Creates all of the AI players for the game and adds them to the list of
	 * players
	 */
	private void addAI() {
		if (tournamentMode) {
			for (int i = 0; i < ais; i++) {
				if (i < 2) {
					players.add(new AI(new EasyAI(), totalPlayers));

				} else if (i < 4) {
					players.add(new AI(new MediumAI(), totalPlayers));
				} else
					players.add(new AI(new HardAI(), totalPlayers));
				players.get(i).setFaction(i);
				players.get(i).setName("");

			}
		} else
			for (int i = 0; i < ais; i++)
				players.add(new AI(new EasyAI(), totalPlayers));
	}// end
		// addAi

	/*
	 * addAI
	 * 
	 * Creates all of the AI players for the game and adds them to the list of
	 * players
	 */
	private void addHumanPlayers() {
		for (int i = 0; i < humans; i++)
			players.add(new HumanPlayer(totalPlayers));
	}// end addHumanPlayers

	/**********************************************************************************
	 ****************************** Changing the game phase******************************
	 **********************************************************************************/

	/*
	 * These methods handle moving our code from one phase to the next
	 */
	public void nextPhase() throws IllegalStateException {
		if (isPlacePhase()) {
			changeToReinforcePhase();
		} // ed if
		else if (isReinforcePhase()) {
			changeToRedeemCardsPhase();
		} // end else if
		else if (isRedeemCardPhase()) {
			changeToDeployTroopsPhase();
		} // end else if
		else if (isDeployPhase()) {
			changeToAttackPhase();
		} // end else if
		else if (isAttackPhase()) {
			changeToReinforcePhase();
		} // end else if
		else if (isGameOver()) {
			gameLog += "The Game is Over.\n" + currentPlayer.getName()
					+ " has won.";
		} else {
			throw new IllegalStateException("The phase was invalid");
		}
		selectedCountry = null;
	}// end nextPhase

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isAttackPhase() {
		return attackPhase;
	}

	public boolean isDeployPhase() {
		return deployPhase;
	}

	public boolean isRedeemCardPhase() {
		return redeemCardPhase;
	}

	public boolean isReinforcePhase() {
		return reinforcePhase;
	}

	public boolean isPlacePhase() {
		return placePhase;
	}

	public boolean isPlayPhase() {
		return mainGamePhase;
	}

	// changes all the flags for a proper placement phase
	private void changeToPlacementPhase() {
		mainGamePhase = false;
		placePhase = true;
		reinforcePhase = false;
		redeemCardPhase = false;
		deployPhase = false;
		attackPhase = false;
		gameOver = false;
	}

	// changes all the flags for a proper reinforce phase
	private void changeToReinforcePhase() {
		placePhase = false;
		reinforcePhase = true;
		redeemCardPhase = false;
		deployPhase = false;
		attackPhase = false;
		gameOver = false;
	}// end changetoReinforcePhase

	// changes all the flags for a proper redeem card phase
	private void changeToRedeemCardsPhase() {
		// If this is our first card redemption
		if (mainGamePhase == false)
			mainGamePhase = true;
		placePhase = false;
		reinforcePhase = false;
		redeemCardPhase = true;
		deployPhase = false;
		attackPhase = false;
		gameOver = false;
	}// end changeToRedeemCardsPhase

	// changes the flags for a proper deploy phase
	// also adds units to the current player, and writes to the game log
	private void changeToDeployTroopsPhase() {
		placePhase = false;
		reinforcePhase = false;
		redeemCardPhase = false;
		deployPhase = true;
		attackPhase = false;
		gameOver = false;

		currentPlayer.addAvailableTroops(gameMap
				.getContinentBonuses(currentPlayer));
		currentPlayer.getTroops();
		gameLog += "Continent bonus applied: "
				+ gameMap.getContinentBonuses(getCurrentPlayer()) + "\n";
	}// end changeToDeployTroopsPhase

	// changes flags for a proper attack phase
	private void changeToAttackPhase() {
		placePhase = false;
		reinforcePhase = false;
		redeemCardPhase = false;
		deployPhase = false;
		attackPhase = true;
		gameOver = false;
		cardEarned = false;
	}// end changetoattackphase

	/*
	 * nextPlayer()
	 * 
	 * sets the currentPlayer to whoever is next in the list and calls nextPhase
	 * to keep the game up to date
	 */
	private void nextPlayer() {
		int i = players.indexOf(currentPlayer);
		i++;
		// Wraparound
		if (i == players.size())
			i = 0;

		currentPlayer = players.get(i);
		selectedCountry = null;
		gameLog += "\nNext player's turn: " + getCurrentPlayer().getName()
				+ "\n";
	}// end nextPlayer

	/**********************************************************************************
	 *************************** Coordinating Gameplay***********************************
	 **********************************************************************************/
	public void startGame() {
		// Randomly picks a player from the total number of players
		int firstPlayer = (int) (Math.random() * totalPlayers);
		gameStarted = true;
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
		currentPlayer = players.get(0);

		// Only play if the first player is an AI
		if (currentPlayer instanceof AI)
			play();
	}// end startGame

	public void play() {
		// Always take the human turn that triggered this call to play
		// if there is a human player
		if (currentPlayer instanceof HumanPlayer)
			humanTurn();

		// Then, continue as long as an AI is playing
		while (currentPlayer instanceof AI) {
			aiTurn();
			if (gameOver)
				break;
		} // end while
	}// end play

	/*
	 * aiTurn
	 * 
	 * Tells the AI to play one move
	 */
	private void aiTurn() throws IllegalStateException {
		if (isPlacePhase()) {
			boolean placed = false;
			// Ask the AI to pick a country until they pick a country without an
			// owner
			while (!placed) {
				selectedCountry = ((AI) currentPlayer).getStrategy()
						.placeUnit();

				if (selectedCountry.getOccupier() == null)
					placed = true;
			} // end while
			placeArmies(1);
			nextPlayer();
			if (countriesClaimed == 50)
				nextPhase();
		} // end if

		// During initial reinforce phase
		else if (isReinforcePhase() && !isPlayPhase()) {
			while (selectedCountry == null) {
				selectedCountry = ((AI) currentPlayer).getStrategy()
						.placeLeftOverUnits();
			}// end
			placeArmies(1);
			nextPlayer();
			// If the next player has no more troops, then go to redeem cards!
			if (currentPlayer.getAvailableTroops() == 0)
				nextPhase();
		} // end else if

		// Do nothing if the game is over!
		else if (isFinished()) {
			
		}// end else if

		// During redeem cards
		else if (isRedeemCardPhase()) {

			cardsToRedeem = ((AI) currentPlayer).redeemCards();

			if (cardsToRedeem != null)
				currentPlayer.addAvailableTroops(redeemCards());

			skipCardRedemption();

		} // end else if

		// During deployment
		else if (isDeployPhase()) {

			deployTroops();

			if (currentPlayer.getAvailableTroops() == 0)
				nextPhase();

		} // end else if

		// During attack phase
		else if (isAttackPhase()) {
			// gets a country to attack, if there is no country to attack, moves
			// to he next phase
			moveTo = ((AI) currentPlayer).getStrategy().getCountryToAttack();
			if (moveTo == null)
				this.skipAttackPhase();

			else {// if there was a country to attack, grab the country that we
					// are attacking from
					// if for some reason that is null, do not attack.
				moveFrom = ((AI) currentPlayer).getStrategy()
						.findAttackingCountry(moveTo);
				if (moveFrom != null)
					attack();

				// If the AI decided to finish attacking
				if (((AI) currentPlayer).finishedAttacking())
					this.skipAttackPhase();
			}

		} // end else if

		// during official reinforcement
		else if (isReinforcePhase()) {
			gameLog += ((AI) currentPlayer).getStrategy().reinforce();
			nextPhase();
			nextPlayer();

		} // end else if
	}// end aiturn

	/*
	 * Gets an arraylist of countries from an ai, and places units on them until
	 * there are no units to place if the current player is a human player, lets
	 * the player pick a country, and place how many units they would like
	 * depending on how many they have to place. Does error checking for
	 * improper input.
	 */
	private void deployTroops() {
		if (currentPlayer instanceof AI) {
			ArrayList<Country> selectedCountries = new ArrayList<Country>();
			selectedCountries = ((AI) currentPlayer).getStrategy()
					.placeNewTroops();
			int i = 0;

			while (currentPlayer.getAvailableTroops() > 0) {
				selectedCountry = selectedCountries.get(i);
				if (currentPlayer.getAvailableTroops() > 1)
					placeArmies(2);
				else
					placeArmies(1);
				i++;
				if (i == selectedCountries.size())
					i = 0;
			} // end while
		} // end if
		else {
			boolean continueFlag = false;
			int armiesToPlaceInt = 0;
			String armiesToPlaceStr = JOptionPane
					.showInputDialog("How many armies do you want to place? (You can place "
							+ currentPlayer.getAvailableTroops() + ")");

			try {
				armiesToPlaceInt = Integer.parseInt(armiesToPlaceStr);
				continueFlag = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "That was invalid number.",
						"Error", JOptionPane.ERROR_MESSAGE);
			} // end catch
			if (continueFlag) {
				if (armiesToPlaceInt < 0
						|| armiesToPlaceInt > currentPlayer
								.getAvailableTroops()) {
					JOptionPane.showMessageDialog(null, "Invalid number.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					placeArmies(armiesToPlaceInt);
				} // end else
			} // end if
		} // end else
	}// end deploytroops

	/*
	 * humanTurn
	 * 
	 * plays one human turn, checking which phase it it is, and moving on from
	 * there
	 */
	private void humanTurn() throws IllegalStateException {
		if (isPlacePhase()) {
			placeArmies(1);
			nextPlayer();
			if (countriesClaimed == 50)
				nextPhase();
		} // end if
		else if (isReinforcePhase() && !isPlayPhase()) {
			placeArmies(1);
			nextPlayer();
			// If the next player has no more troops, then go to redeem cards!
			if (currentPlayer.getAvailableTroops() == 0)
				nextPhase();
		} // end else if
		else if (isRedeemCardPhase()) {
			currentPlayer.addAvailableTroops(redeemCards());
			nextPhase();
		} // end else if
		else if (isDeployPhase()) {
			deployTroops();

			if (currentPlayer.getAvailableTroops() == 0)
				nextPhase();
		} // end else if
		else if (isAttackPhase()) {
			// If we're here, the human is already done with attack
			nextPhase();
		} // end else if
		else if (isReinforcePhase()) {
			// If we're here, the human is already done with reinforcement
			nextPhase();
			nextPlayer();
		} // end else if
		else if (isFinished()) {
			// Do nothing!
		}// end else if
		else {
			throw new IllegalStateException("Illegal phase!");
		} // end else
	}// end aiturn

	/*
	 * Gets a country selected by the ai, if that country is available, place a
	 * unit there and return true, otherwise reutnr false
	 */
	public boolean aiChoicePlacement() {
		selectedCountry = ((AI) currentPlayer).getStrategy().placeUnit();
		if (checkIfCountryAvailable(selectedCountry)) {
			placeArmies(1);
			selectedCountry = null;
			return true;
		} // end if
		return false;
	}// end aiChoicePlacement

	/*
	 * checkIfCountryAvailable
	 * 
	 * returns true if a country hasno owner and false otherwise
	 */
	private boolean checkIfCountryAvailable(Country countryToCheck) {
		return countryToCheck.getOccupier() == null;
	}// end checkIfCountryAvailable

	/*
	 * If it is the place phase, and the selected country is valid, place a unit
	 * there, and change the occupier.
	 */
	public void placeArmies(int num) throws IllegalStateException {
		if (isPlacePhase()) {
			// Sets player to this country's occupier and adds country
			// to player's list

			// Put inside of this if statement to prevent null pointer
			// exceptions when you close
			// the game before selecting a country for the first time
			if (selectedCountry != null) {
				selectedCountry.setOccupier(currentPlayer);
				gameLog += currentPlayer.getName() + " claimed "
						+ selectedCountry.toString() + "\n";
				countriesClaimed++;
			}// end if

		} // end if
		else {
			gameLog += currentPlayer.getName() + " placed " + num
					+ " units on " + selectedCountry.toString() + "\n";
		} // end else
		selectedCountry.addForcesVal(num);
		currentPlayer.subtractFromAvailableTroops(num);
	}// end placeArmies

	/*
	 * Given the player's current decision, redeem or don't redeem cards if
	 * redemption was chosen, give the player the proper amount of units, check
	 * if the player owns any of the countries on the cards and allocate units
	 * as appropriate and discard those cards
	 */
	public int redeemCards() {
		// If cards is null, the player didn't want to redeem anythign
		if (cardsToRedeem == null)
			return 0;

		// If we're here, we're redeeming
		numRedemptions++;
		int numArmies = 0;

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
		}// end switch case
		gameLog += currentPlayer.getName() + " redeemed cards. Earned "
				+ numArmies + "extra armies to deploy ";

		boolean added = false;
		for (Card c : cardsToRedeem) {
			for (Country t : currentPlayer.getCountries()) {
				if (c.getCountry().compareTo(t.getName()) == 0) {
					// add 2 armies to that country
					added = true;
					t.addForcesVal(2);
					gameLog += "and 2 bonus armies on " + t.getName() + "\n";
					break; // can only redeem a country card for extra armies
							// once per turn
				}
			}
			if (added)
				break;
		}

		// Now, discard the cards
		deck.addToDiscardPile(cardsToRedeem, discard);
		currentPlayer.discardCards(cardsToRedeem);
		cardsToRedeem = null;

		return numArmies;
	}// end redeemCards

	/*
	 * return the correct number of attack die, depending on what is chosen in
	 * the gui, and the number of units on the attacking country
	 */
	public int getNumAttackDice() {
		int forces = moveFrom.getForcesVal();
		if (maxAttackDice == 3) {
			if (forces > 3) {
				return 3;
			} // end if
			else if (forces > 2) {
				return 2;
			} // end else if
			else {
				return 1;
			} // end else
		} else if (maxAttackDice == 2) {
			if (forces > 2)
				return 2;
			else
				return 1;
		} // end else if
		else
			return 1;
	}// end getNumAttackDice

	// get the number of defense die, depending on what is chosen in the gui,
	// and how many units are on the defending country
	public int getNumDefenseDice() {

		int forces = moveTo.getForcesVal();
		if (maxDefendDice == 2) {
			if (forces > 1)
				return 2;
			else
				return 1;
		} // end if

		else
			return 1;
	} // end else

	/*
	 * Attack: get the correct amount of die, and then check if the
	 * attackWasSuccesful or not. Subtract units from countries that took
	 * damage, and check if the defending country has 0 units. If so, have the
	 * attacker occupy that country. Then check if a user has been defeated, and
	 * if the game is finished.
	 */
	public boolean attack() {

		attackDice = Dice.roll(getNumAttackDice());
		defenseDice = Dice.roll(getNumDefenseDice());

		gameLog += moveFrom.getOccupier().getName() + " attacked "
				+ moveTo.getOccupier().getName() + " at " + moveTo.getName()
				+ ".\n";

		String result = "";
		int attackResult = wasAttackSuccessful();

		// attack won

		if (attackResult > 0) {
			result += currentPlayer.getName() + " won the attack.\n";
			attackerHits = 2;
			if (currentPlayer instanceof HumanPlayer)
				JOptionPane.showMessageDialog(null, currentPlayer.getName()
						+ " won the attack", "Success",
						JOptionPane.INFORMATION_MESSAGE);

			moveTo.removeUnits(attackResult);

			if (moveTo.getForcesVal() < 1) {
				countryWasTaken();
				removeLosers();
				isFinished();
			} // end if

			clearSelections();
			gameLog += result;
			return true;
		} // end if

		// attack lost
		else if (attackResult < 0) {
			attackerHits = 0;
			if (currentPlayer instanceof HumanPlayer)
				JOptionPane.showMessageDialog(null, currentPlayer.getName()
						+ " lost the attack", "Failure",
						JOptionPane.INFORMATION_MESSAGE);
			// Get rid of the negative so we don't accidentally add
			moveFrom.removeUnits(-1 * attackResult);

			result += currentPlayer.getName() + " lost the attack.\n";
			clearSelections();
			gameLog += result;
			return false;

		} // end else if

		// Otherwise, lose one and one
		else {
			attackerHits = 1;
			result += currentPlayer.getName() + " was forced to retreat.\n";
			if (currentPlayer instanceof HumanPlayer)
				JOptionPane.showMessageDialog(null, currentPlayer.getName()
						+ " was forced to retreat.", "tie",
						JOptionPane.INFORMATION_MESSAGE);

			moveFrom.removeUnits(1);
			moveTo.removeUnits(1);

			if (moveTo.getForcesVal() <= 0) {
				countryWasTaken();
				removeLosers();
				isFinished();
			} // end if

			clearSelections();
			gameLog += result;
			return true;
		} // end else
	}// end attack

	/*
	 * Makes the necessary changes to the countries if the attackee lost a
	 * country
	 */
	private void countryWasTaken() {
		gameLog += currentPlayer.getName() + " defeated "
				+ moveTo.getOccupier().getName() + " and took "
				+ moveTo.getName() + ".\n";

		int units = 0;

		// Move all of ai's units but one to the new country
		if (currentPlayer instanceof AI) {
			units = moveFrom.getForcesVal() - 1;
		} // end if

		// let the person choose how many to move
		else {
			// Find out how many units they want to move
			units = 0;
			while (units == 0) {
				String unitsToMove = JOptionPane
						.showInputDialog("How Many armies do you want to move? You must move at least 1.");
				try {
					units = Integer.parseInt(unitsToMove);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null,
							"That was invalid number.", "Error",
							JOptionPane.ERROR_MESSAGE);
					units = 0;
					continue;
				} // end catch

				if (units < 1 || units > moveFrom.getForcesVal() - 1) {
					JOptionPane.showMessageDialog(null,
							"That was invalid number.", "Error",
							JOptionPane.ERROR_MESSAGE);
					units = 0;
				} // end if
			} // end while
		} // end else

		moveTo.addForcesVal(units);
		moveFrom.removeUnits(units);
		moveTo.setOccupier(currentPlayer);

		cardEarned = true;
	}// end countryWasTaken

	/*
	 * 
	 * return -2: Defender won (subtract 2 from attacker) return -1: Defender
	 * won (subtract 1 from attacker, depending on num dice) return 0: Tie
	 * (subtract 1 from each) return 1: Attacker won (subtract 1 from defender,
	 * depending on num dice) return 2: Attack won (subtract 2 from defender)
	 */
	public int wasAttackSuccessful() {
		int attackWins = 0;
		int defenseWins = 0;
		int aHigh = attackDice.get(0).getValue();
		int dHigh = defenseDice.get(0).getValue();

		gameLog += "Dice Rolls: \n\t Attacker's highest die= "
				+ attackDice.get(0).getValue()
				+ "\n\t Defender's highest die = "
				+ defenseDice.get(0).getValue() + "\n";

		if (aHigh > dHigh) {

			attackWins++;
		} // end if
		else {
			defenseWins++;
		} // end else if

		// If both parties rolled more than one, compare the second
		if (attackDice.size() > 1 && defenseDice.size() > 1) {
			aHigh = attackDice.get(1).getValue();
			dHigh = defenseDice.get(1).getValue();

			gameLog += "Dice Rolls: \n\t Attacker's second highest die= "
					+ attackDice.get(1).getValue()
					+ "\n\t Defender's second highest die = "
					+ defenseDice.get(1).getValue() + "\n";

			if (aHigh > dHigh) {

				attackWins++;
			} // end if
				// Ties or d high goes to defender
			else {
				defenseWins++;
			} // end else
		} // end if
			// return numArmies > moveTo.getForcesVal();
		return attackWins - defenseWins;
	}// end wasAttackSuccessful

	/**********************************************************************************
	 *************************** Shuffling Armies in Countries***************************
	 **********************************************************************************/
	// Main idea: player chooses which cards to redeem (max of 3)
	// if player has 5 cards, he MUST have a match, so call this function until
	// the player chooses the matching 3 cards

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
			unitsToMove = JOptionPane
					.showInputDialog("How Many armies? You must leave 1.");
			try {
				unitsToReturn = Integer.parseInt(unitsToMove);
				continueFlag = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "That was invalid number.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			if (continueFlag) {
				if (unitsToReturn >= totalUnits) {
					JOptionPane.showMessageDialog(null,
							"You must leave 1 army.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					moveFlag = true;
				}
			}
		}
		return unitsToReturn;

	}// end unitsToReturn

	/*
	 * Moves numUnits from fromCountry to toCountry, but checks if both
	 * countries are owned by current, and that they are both connected by other
	 * friendly countries
	 */
	public boolean moveUnitsToCountry(int numUnits, Country fromCountry,
			Country toCountry, Player current) {

		boolean result = false;
		ArrayList<Country> visited = new ArrayList<Country>();
		visited.add(fromCountry);
		findPath(fromCountry, visited, toCountry, current);
		if (canPlace) {
			toCountry.addForcesVal(numUnits);
			fromCountry.removeUnits(numUnits);
			result = true;
		}
		canPlace = false;
		return result;
	}// end moveUnitsToCountry

	/*
	 * finds the shortest path between two connected countries on the map
	 */
	private void findPath(Country fromCountry, ArrayList<Country> visited,
			Country toCountry, Player currentP) {
		if (canPlace)
			return;
		ArrayList<Country> countries = visited.get(visited.size() - 1)
				.getNeighbors();
		for (Country c : countries) {
			if (!(c.getOccupier().equals(currentP)))
				continue;
			if (visited.contains(c))
				continue;
			if (c.equals(toCountry)) {
				visited.add(c);
				canPlace = true;
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

	/*
	 * Checks if there are any players who have zero countries. If so, remove
	 * them from the list of players.
	 */
	public void removeLosers() {

		Player removeMe = null;
		for (Player player : players) {
			if (player.getCountries().size() == 0) {

				gameLog += player.getName() + " has been wiped off the map.\n";
				removeMe = player;
			}

		}
		if (removeMe != null) {
			discard.addToPile(removeMe.discardCards());
			if (removeMe instanceof HumanPlayer)
				humans--;
			players.remove(removeMe);
			totalPlayers--;
		}

	}// end removeLosers

	/*
	 * checks if there is only 1 player left in the list of players. If so, the
	 * game is over, and sets the flags accordingly. Returns true if game over,
	 * false otherwise.
	 */
	public boolean isFinished() {

		if (players.size() == 1) {
			gameOver = true;
			mainGamePhase = false;
			placePhase = false;
			reinforcePhase = false;
			redeemCardPhase = false;
			deployPhase = false;
			attackPhase = false;
		} else
			gameOver = false;

		return gameOver;
	}// end isFinished

	/**********************************************************************************
	 *************************** The Getters and the Setters*****************************
	 **********************************************************************************/
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public int getNumHumans() {
		return humans;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public int getNumAttacks() {
		return numAttacks;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Map getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map gameMap) {
		this.gameMap = gameMap;
	}

	public Deck getDeck() {
		return deck;
	}

	public DiscardPile getDiscardPile() {
		return discard;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public Country getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(Country selectedCountry) {
		this.selectedCountry = selectedCountry;
		selectedCountry = null;
	}

	public String getGameLog() {
		return gameLog;
	}

	public Country getMoveFrom() {
		return moveFrom;
	}

	public void setMoveFrom() {
		this.moveFrom = selectedCountry;
		selectedCountry = null;
	}

	public Country getMoveTo() {
		return moveTo;
	}

	public void setMoveTo() {
		this.moveTo = selectedCountry;
	}

	public String getPhase() {
		if (isPlacePhase())
			return "Place Phase";
		else if (isDeployPhase())
			return "Deploy Phase";
		else if (isReinforcePhase())
			return "Reinforce Phase";
		else if (isAttackPhase())
			return "Attack Phase";
		else if (isRedeemCardPhase())
			return "Card Redemption Phase";
		return null;
	}

	public Map getMap() {
		return getGameMap();
	}

	public boolean playerIsOwner() {
		return currentPlayer.equals(selectedCountry.getOccupier());
	}// end playerIsOwner

	/*
	 * Skips to the next phase after attack phase
	 */
	public boolean skipAttackPhase() {
		boolean tmp = cardEarned;
		clearSelections();
		if (cardEarned) {
			if (!tournamentMode)
				currentPlayer.addCard(deck.deal(discard));

			gameLog += currentPlayer.getName() + " earned a new card.\n";
			cardEarned = false;
		} // end if

		nextPhase();
		return tmp;
	}// end skipAttackPhase

	/*
	 * skip to the next phase after card redemption phase
	 */
	public boolean skipCardRedemption() {
		if (!currentPlayer.mustRedeemCards()) {
			nextPhase();
			return true;
		} // end if
		else {
			return false;
		} // end else
	}// end skipCardRedemption

	public int getNumRedemptions() {
		return numRedemptions;
	}// end getNumRedemptions

	/*
	 * skips players reinforcement phase, and then calls play
	 */
	public void passReinforcementPhase() {
		clearSelections();
		play();
	}// end passReinforce

	/*
	 * resets moveFrom, moveTo, and selectedCountry
	 */
	public void clearSelections() {
		moveFrom = null;
		moveTo = null;
		selectedCountry = null;
	}// end clearSelections

	/*
	 * transfers numArmies
	 */
	public boolean transferTroops(int numArmies) {
		canPlace = false;
		return moveUnitsToCountry(numArmies, moveFrom, moveTo, currentPlayer);
	}// end transferTroops

	public ArrayList<Dice> getAttackDice() {
		return attackDice;
	}

	public ArrayList<Dice> getDefenseDice() {
		return defenseDice;
	}

	public void setCardsToRedeem(ArrayList<Card> cards) {
		cardsToRedeem = cards;
	}

	public int getHit() {
		return attackerHits;
		// attackerHits is set in attack
		// return 1 if offense loses one and wins one
		// return 0 if offense loses entirely
		// return 2 if offense wins entirely
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void changeAttackDice(int i) {
		maxAttackDice = i;
	}

	public void changeDefendDice(int i) {
		maxDefendDice = i;
	}
}// end theGame
