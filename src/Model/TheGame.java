package Model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class TheGame implements Serializable {
	private ArrayList<Player> players;
	private Player currentPlayer;
	private Map gameMap;
	private Deck deck;
	private Country selectedCountry;
	private Country moveFrom, moveTo;
	private boolean placePhase, reinforcePhase, deployPhase, attackPhase, gameOver, redeemCardPhase, mainGamePhase,
			cardEarned;
	private static TheGame theGame;
	private boolean tournamentMode, canPlace;
	private String gameLog;
	private int humans, ais, totalPlayers;
	private int numRedemptions, countriesClaimed;
	private int countriesBefore, countriesAfter;
	private ArrayList<Dice> attackDice;
	private ArrayList<Dice> defenseDice;
	private boolean useMaxDice = true;
	private ArrayList<Card> cardsToRedeem;
	public static final String FILE_NAME = "game.ser";

	/**********************************************************************************
	 ********************************** Game Creation************************************
	 **********************************************************************************/
	private TheGame(int numOfHumanPlayers, int numOfAIPlayers, boolean tourny) {
		// Getting the sizes
		humans = numOfHumanPlayers;
		ais = numOfAIPlayers;
		totalPlayers = humans + ais;
		countriesClaimed = 0;
		countriesBefore = 0;
		countriesAfter = 0;
		cardEarned = false;
		tournamentMode = tourny;
		if (!tournamentMode)
			newGame();
		else
			newGame(numOfAIPlayers);
	}// end constructor

	/*
	 * Return an instance of the singleton game
	 */
	public static TheGame getInstance(int numOfHumanPlayers, int totalNumOfPlayers, boolean tourny) {
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
		selectedCountry = null;
		gameMap = Map.getInstance(0);
		gameMap = gameMap.newMap();
		deck = Deck.getInstance();
		deck = deck.newDeck();
		players = new ArrayList<>();
		addHumanPlayers();
		addAI();
		currentPlayer = players.get(0);
		numRedemptions = 0;
		canPlace = false;
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

		selectedCountry = null;
		gameMap = Map.getInstance(1);
		gameMap = gameMap.newTourneyMap();
		deck = Deck.getInstance();
		deck = deck.newDeck();
		// deck.shuffle();
		players = new ArrayList<>();
		addAI();
		currentPlayer = players.get(0);
		numRedemptions = 0;
		changeToPlacementPhase();
	}// end newGame

	/*
	 * addAI
	 * 
	 * Creates all of the AI players for the game and adds them to the list of
	 * players
	 */
	private void addAI() {
		for (int i = 0; i < ais; i++)
			players.add(new AI(new EasyAI(), totalPlayers));
	}// end addAi

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
			System.out.println("The game is over");
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

	private void changeToPlacementPhase() {
		mainGamePhase = false;
		placePhase = true;
		reinforcePhase = false;
		redeemCardPhase = false;
		deployPhase = false;
		attackPhase = false;
		gameOver = false;
	}

	private void changeToReinforcePhase() {
		placePhase = false;
		reinforcePhase = true;
		redeemCardPhase = false;
		deployPhase = false;
		attackPhase = false;
		gameOver = false;
	}// end changetoReinforcePhase

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

	private void changeToDeployTroopsPhase() {
		placePhase = false;
		reinforcePhase = false;
		redeemCardPhase = false;
		deployPhase = true;
		attackPhase = false;
		gameOver = false;

		currentPlayer.addAvailableTroops(gameMap.getContinentBonuses(currentPlayer));
		currentPlayer.getTroops();
		gameLog += "Continent bonus applied: " + gameMap.getContinentBonuses(getCurrentPlayer()) + "\n";
	}// end changeToDeployTroopsPhase

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
		gameLog += "\nNext player's turn: " + getCurrentPlayer().getName() + "\n";
	}// end nextPlayer

	/**********************************************************************************
	 *************************** Coordinating Gameplay***********************************
	 **********************************************************************************/
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
		currentPlayer = players.get(0);
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
			System.out.println(getPhase());
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
				selectedCountry = ((AI) currentPlayer).getStrategy().placeUnit();

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
				selectedCountry = ((AI) currentPlayer).getStrategy().placeLeftOverUnits();
			}
			placeArmies(1);
			nextPlayer();
			// If the next player has no more troops, then go to redeem cards!
			if (currentPlayer.getAvailableTroops() == 0)
				nextPhase();
		} // end else if

		// During redeem cards
		else if (isRedeemCardPhase()) {
			cardsToRedeem = ((AI)currentPlayer).redeemCards();
			
			if (cardsToRedeem!=null)
				currentPlayer.addAvailableTroops(redeemCards());
			
			nextPhase();
		} // end else if

		// During deployment
		else if (isDeployPhase()) {
			deployTroops();

			if (currentPlayer.getAvailableTroops() == 0)
				nextPhase();
		} // end else if

		// During attack phase
		else if (isAttackPhase()) {

			//printAllCountriesAndOccupiers();
//			moveTo = ((AI) currentPlayer).getCountryToAttack();
//			moveFrom = ((AI) currentPlayer).findAttackingCountry(moveTo);
//			attack();

			moveTo = ((AI) currentPlayer).getStrategy().getCountryToAttack();
			if(moveTo == null)
				this.skipAttackPhase();
			moveFrom = ((AI) currentPlayer).getStrategy().findAttackingCountry(moveTo);
			if(moveFrom != null)
				attack();


			// If the AI decided to finish attacking
			if (((AI) currentPlayer).finishedAttacking())
				this.skipAttackPhase();
		} // end else if

		// during official reinforcement
		else if (isReinforcePhase()) {
			gameLog += ((AI) currentPlayer).getStrategy().reinforce();
			nextPhase();
			nextPlayer();
		} // end else if

		// Otherwise, problem!
		else {
			throw new IllegalStateException("Illegal phase!");
		} // end else
	}// end aiturn
	
	/*
	 * For testing purposes!!!
	 */
	public void printAllCountriesAndOccupiers(){
		Country[] countries = gameMap.getCountries();
		for (int i=0; i < countries.length; i++){
			System.out.println(countries[i].getName() + ": " + countries[i].getOccupier());
		}//end for
	}//end print all

	private void deployTroops() {
		if (currentPlayer instanceof AI) {
			ArrayList<Country> selectedCountries = new ArrayList<Country>();
			selectedCountries = ((AI) currentPlayer).getStrategy().placeNewTroops();
			int i = 0;
			while (i < selectedCountries.size() && currentPlayer.getAvailableTroops() > 0) {
				selectedCountry = selectedCountries.get(i);
				placeArmies(1);
				i++;
			} // end while
		} // end if
		else {
			// if (selectedCountry != null){
			boolean continueFlag = false;
			int armiesToPlaceInt = 0;
			String armiesToPlaceStr = JOptionPane
					.showInputDialog("How many armies do you want to place? (You can place "
							+ theGame.getCurrentPlayer().getAvailableTroops() + ")");

			try {
				armiesToPlaceInt = Integer.parseInt(armiesToPlaceStr);
				continueFlag = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "That was invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
			} // end catch
			if (continueFlag) {
				if (armiesToPlaceInt < 0 || armiesToPlaceInt > currentPlayer.getAvailableTroops()) {
					JOptionPane.showMessageDialog(null, "Invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					placeArmies(armiesToPlaceInt);
				} // end else
			} // end if
				// }//end if
		} // end else
	}// end deploytroops

	/*
	 * humanTurn
	 * 
	 * plays one human turn
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
		else {
			throw new IllegalStateException("Illegal phase!");
		} // end else
	}// end aiturn

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

	public void placeArmies(int num) throws IllegalStateException {
		if (isPlacePhase()) {
			// Sets player to this country's occupier and adds country
			// to player's list
			selectedCountry.setOccupier(currentPlayer);
			gameLog += currentPlayer.getName() + " claimed " + selectedCountry.toString() + "\n";
			countriesClaimed++;
		} // end if
		else {
			gameLog += currentPlayer.getName() + " placed " + num + " units on " + selectedCountry.toString() + "\n";
		}//end else 
		selectedCountry.addForcesVal(num);
		currentPlayer.subtractFromAvailableTroops(num);
	}// end placeArmies

	/*
	 * Given the player's current decision, redeem or don't redeem cards
	 */
	public int redeemCards() {
		// ArrayList<Card> cardsToRedeem = currentPlayer.redeemCards();

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
		
		gameLog+= currentPlayer.getName() + " redeemed cards and earned " + numArmies + " armies.\n";

		// Now, discard the cards
		deck.addToDiscardPile(cardsToRedeem);
		currentPlayer.discardCards(cardsToRedeem);
		cardsToRedeem = null;

		return numArmies;
	}// end redeemCards

	public int getNumAttackDice() {
		int forces = moveFrom.getForcesVal();
		if (useMaxDice) {
			if (forces > 3) {
				return 3;
			} // end if
			else if (forces > 2) {
				return 2;
			} // end else if
			else {
				return 1;
			} // end else
		} else {
			int diceAllowed = 0;
			if (forces > 3) {
				diceAllowed = 3;
			} // end if
			else if (forces > 2) {
				diceAllowed = 2;
			} // end else if
			else {
				return 1;
			} // end else

			// Let the AI choose
			if (currentPlayer instanceof AI) {
				return ((AI) currentPlayer).chooseMyDiceToRoll(diceAllowed);
			} // end if

			// Otherwise, it's a human
			int diceToUse = -1;

			while (diceToUse == -1) {
				String sNumDice = JOptionPane
						.showInputDialog("How many dice would you like to throw? You can throw up to " + diceAllowed);

				try {
					diceToUse = Integer.parseInt(sNumDice);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Invalid number.", "Dice Error", JOptionPane.ERROR_MESSAGE);
					continue;
				} // end catch

				if (diceToUse > diceAllowed || diceToUse < 0) {
					diceToUse = -1;
					JOptionPane.showMessageDialog(null, "Invalid number.", "Dice Error", JOptionPane.ERROR_MESSAGE);
				} // end if
			} // end while

			return diceToUse;
		}
	}// end getNumAttackDice

	public int getNumDefenseDice() {
		int forces = theGame.getMoveTo().getForcesVal();
		if (useMaxDice) {
			if (forces > 1)
				return 2;
			else
				return 1;
		} // end if
		else {
			int diceAllowed = 0;
			if (forces > 1) {
				diceAllowed = 2;
			} // end else if
			else {
				return 1;
			} // end else

			if (theGame.getMoveTo().getOccupier() instanceof HumanPlayer) {
				int diceToUse = -1;

				while (diceToUse == -1) {
					String sNumDice = JOptionPane.showInputDialog(
							"How many dice would you like to throw? You can throw up to " + diceAllowed);

					try {
						diceToUse = Integer.parseInt(sNumDice);
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Invalid number.", "Dice Error", JOptionPane.ERROR_MESSAGE);
						continue;
					} // end catch

					if (diceToUse > diceAllowed || diceToUse < 0) {
						diceToUse = -1;
						JOptionPane.showMessageDialog(null, "Invalid number.", "Dice Error", JOptionPane.ERROR_MESSAGE);
					} // end if
				} // end while

				return diceToUse;
			} // end if
			return ((AI) theGame.getMoveTo().getOccupier()).chooseMyDiceToRoll(diceAllowed);
		} // end else
	}// end getNumDefenseDice

	public boolean attack() {
	
		attackDice = Dice.roll(getNumAttackDice());
		defenseDice = Dice.roll(getNumDefenseDice());

		gameLog += moveFrom.getOccupier().getName() + " attacked " + moveTo.getOccupier().getName() + " at "
				+ moveTo.getName() + ".\n";

		String result = "";
		int attackResult = wasAttackSuccessful();

		// attack won

		if (attackResult >0) {
			result += currentPlayer.getName() + " won the attack.\n";
			
			if (currentPlayer instanceof HumanPlayer)
				JOptionPane.showMessageDialog(null,
					theGame.getCurrentPlayer().getName() + " won the attack", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			// Resetting units
			// moveTo.setForcesToZero();
			// moveTo.addForcesVal(numArmies);
			// moveFrom.removeUnits(numArmies);
			moveTo.removeUnits(attackResult);
			
			if (moveTo.getForcesVal() < 1) {
				countryWasTaken();
				removeLosers();
				isFinished();
			}// end if
			
			clearSelections();
			gameLog += result;
			return true;
		}// end if

		// attack lost
		else if (attackResult < 0){
			if (currentPlayer instanceof HumanPlayer)
				JOptionPane.showMessageDialog(null,
					theGame.getCurrentPlayer().getName() + " lost the attack", "Failure",
					JOptionPane.INFORMATION_MESSAGE);
			//Get rid of the negative so we don't accidentally add
			moveFrom.removeUnits(-1 * attackResult);

			
			result += currentPlayer.getName() + " lost the attack.\n";
			clearSelections();
			gameLog += result;
			return false;

		}// end else if
		
		//Otherwise, lose one and one
		else {
			result += currentPlayer.getName() + " was forced to retreat.\n";
			if (currentPlayer instanceof HumanPlayer)
				JOptionPane.showMessageDialog(null,
					theGame.getCurrentPlayer().getName() + " was forced to retreat.", "tie",
					JOptionPane.INFORMATION_MESSAGE);
			
			moveFrom.removeUnits(1);
			moveTo.removeUnits(1);
			

			if (moveTo.getForcesVal() <= 0) {
				countryWasTaken();
				removeLosers();
				isFinished();
			}// end if
			
			clearSelections();
			gameLog += result;
			return true;
		}//end else
	}// end attack
	
	/*
	 * Makes the necessary changes to the countries if the attackee lost a country
	 */
	private void countryWasTaken(){
		gameLog += currentPlayer.getName() + " defeated "
				+ moveTo.getOccupier().getName() + " and took "
				+ moveTo.getName() + ".\n";
		
		int units = 0;
		
		//let the AI choose how many to move
		//STUB!!!! change for AI behavior change
		if (currentPlayer instanceof AI){
			units = moveFrom.getForcesVal() - 1;
		}//end if
		
		//let the person choose how many to move
		else {
			//Find out how many units they want to move
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
				}// end catch
				
				if (units < 1 || units > moveFrom.getForcesVal() - 1) {
					JOptionPane.showMessageDialog(null,
							"That was invalid number.", "Error",
							JOptionPane.ERROR_MESSAGE);
					units = 0;
				}//end if
			}// end while
		}//end else
		
		
		moveTo.addForcesVal(units);
		moveFrom.removeUnits(units);
		moveTo.setOccupier(currentPlayer);

		cardEarned = true;
	}//end countryWasTaken


				


	// attack lost

	// if (numArmies <= moveTo.getForcesVal()) {
	// // theirs.setForcesVal(numArmies);
	// moveFrom.removeUnits(numArmies); // you lose the armies fought with
	// result+=moveFrom.getOccupier().getName() + " lost the battle.\n";
	// } else if (moveTo.getForcesVal() < numArmies) {
	// result += moveFrom.getOccupier().getName() + " defeated " +
	// moveTo.getOccupier().getName() + " and took " + moveTo.getName() +
	// ".\n";
	// countriesBefore = getCurrentPlayer().getCountries().size();
	// //moveTo.getOccupier().loseCountry(moveTo);
	// moveTo.removeUnits(moveTo.getForcesVal());
	// moveTo.setForcesVal(numArmies);
	// moveTo.setOccupier(moveFrom.getOccupier());
	// moveFrom.getOccupier().occupyCountry(moveTo);
	// moveFrom.removeUnits(numArmies);
	// countriesAfter = getCurrentPlayer().getCountries().size();
	// // players.get(playerLocation).addCard(deck.deal());
	// }//end else if
	/*
	 * 
	 * return -2: Defender won (subtract 2 from attacker)
	 * return -1: Defender won (subtract 1 from attacker, depending on num dice)
	 * return 0: Tie (subtract 1 from each)
	 * return 1: Attacker won (subtract 1 from defender, depending on num dice)
	 * return 2: Attack won (subtract 2 from defender)
	 */
	public int wasAttackSuccessful() {
		int attackWins = 0;
		int defenseWins = 0;
		int aHigh = attackDice.get(0).getValue();
		int dHigh = defenseDice.get(0).getValue();
		
		gameLog += "Dice Rolls: \n\t Attacker's highest die= " + attackDice.get(0).getValue() + "\n\t Defender's highest die = "+ defenseDice.get(0).getValue() + "\n";
		
		if (aHigh > dHigh) {

			attackWins++;
		}// end if
		else {
			defenseWins++;
		}// end else if
		
		//If both parties rolled more than one, compare the second
		if (attackDice.size()>1 && defenseDice.size()>1){
			aHigh = attackDice.get(1).getValue();
			dHigh = defenseDice.get(1).getValue();

			gameLog += "Dice Rolls: \n\t Attacker's second highest die= " + attackDice.get(1).getValue() + "\n\t Defender's second highest die = "+ defenseDice.get(1).getValue()+"\n";

			
			if (aHigh > dHigh) {

				attackWins++;
			}// end if
				// Ties or d high goes to defender
			else {
				defenseWins++;
			}// end else
		}// end if
			// return numArmies > moveTo.getForcesVal();
		return attackWins - defenseWins;
	}// end wasAttackSuccessful
		// return numArmies > moveTo.getForcesVal();
		// end wasAttackSuccessful



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
				if (unitsToReturn >= totalUnits || unitsToReturn < 0) {
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
			toCountry.addForcesVal(numUnits);
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

	public void removeLosers() {
		ArrayList<Player> playersToRemove = new ArrayList<>();
		ArrayList<Integer> playersToRemoveLocations = new ArrayList<>();
		for (Player player : players) {
			if (player.getCountries().size() == 0) {
				System.out.println(player.getName() + " has been wiped off the map.");
				gameLog += player.getName() + " has been wiped off the map.\n";
				playersToRemove.add(player);
			}

		}
		ArrayList<Card> cardsToAddToDiscard = new ArrayList<Card>();
		for (Player player : playersToRemove) {
			cardsToAddToDiscard.addAll(player.discardCards());
		}
		if (cardsToAddToDiscard.size() > 0)
			deck.addToDiscardPile(cardsToAddToDiscard);
		players.removeAll(playersToRemove);
		totalPlayers -= playersToRemove.size();

	}// end removeLosers

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
		// TODO notify gui somehow so that it knows who won, and display that
		// player's victory, as well is turn off all
		// buttons
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

	public Player getCurrentPlayer() {
		return currentPlayer;
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

	public boolean skipAttackPhase() {
		boolean tmp = cardEarned;
		clearSelections();
		if (cardEarned) {
			
			
			//TODO: REMOVE MEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE!!!!
			for (int i=0; i < 5; i++)
				currentPlayer.addCard(deck.deal());
			
			gameLog += currentPlayer.getName() + " earned a new card.\n";
			cardEarned = false;
		} // end if

		nextPhase();
		return tmp;
	}// end skipAttackPhase

	/*
	 * public void skipAttackPhaseAI(){ skipAttackPhase(); nextPhase(); }//end
	 * skipAI
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

	public void passReinforcementPhase() {
		clearSelections();
		// nextPhase();
		// nextPlayer();
		play();
	}// end passReinforce

	public void clearSelections() {
		moveFrom = null;
		moveTo = null;
		selectedCountry = null;
	}// end clearSelections

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
		//TODO: 
		//return 1 if offense loses one and wins one
		//return 0 if offense loses entirely
		//return 2 if offense wins entirely
		return 0;
	}

}// end theGame
