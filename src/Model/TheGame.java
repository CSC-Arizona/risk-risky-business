package Model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class TheGame implements Serializable{
	private ArrayList<Player> players;
	private Player currentPlayer;
	private Map gameMap;
	private Deck deck;
	private Country selectedCountry;
	private boolean placePhase, reinforcePhase, deployPhase, attackPhase,
			gameOver, redeemCardPhase, mainGamePhase;
	private static TheGame theGame;
	private boolean tournamentMode, canPlace;
	private String gameLog;
	private int humans, ais, totalPlayers;
	private int numRedemptions, countriesClaimed;
	public static final String FILE_NAME = "game.ser";

	
	
	
	/**********************************************************************************
	**********************************Game Creation************************************
	**********************************************************************************/
	private TheGame(int numOfHumanPlayers, int numOfAIPlayers, boolean tourny) {
		// Getting the sizes
		humans = numOfHumanPlayers;
		ais = numOfAIPlayers;
		totalPlayers = humans + ais;
		countriesClaimed = 0;
		
		if(!tournamentMode)
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
			players.add(new AI(AIStrat.EASY, totalPlayers));
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
	******************************Changing the game phase******************************
	**********************************************************************************/
	
	/*
	 * These methods handle moving our code from one phase to the next
	 */
	public void nextPhase() throws IllegalStateException {
		if (isPlacePhase()) {
			changeToReinforcePhase();
		}// ed if
		else if (isReinforcePhase()) {
			changeToRedeemCardsPhase();
		}// end else if
		else if (isRedeemCardPhase()) {
			changeToDeployTroopsPhase();
		}// end else if
		else if (isDeployPhase()) {
			changeToAttackPhase();
		}// end else if
		else if (isAttackPhase()) {
			changeToReinforcePhase();
		}// end else if
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
		//If this is our first card redemption
		if (mainGamePhase = false)
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
	}// end changeToDeployTroopsPhase

	private void changeToAttackPhase() {
		placePhase = false;
		reinforcePhase = false;
		redeemCardPhase = false;
		deployPhase = false;
		attackPhase = true;
		gameOver = false;
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
	}// end nextPlayer

	/**********************************************************************************
	***************************Coordinating Gameplay***********************************
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
		play();
	}// end startGame
	
	public void play(){
		//Always take the human turn that triggered this call to play
		// if there is a human player
		if (currentPlayer instanceof HumanPlayer)
			humanTurn();
		
		//Then, continue as long as an AI is playing
		while (currentPlayer instanceof AI){
			aiTurn();
		}//end while
	}//end play
	/*
	 * aiTurn
	 * 
	 * Tells the AI to play one move
	 */
	private void aiTurn() throws IllegalStateException{
		if (isPlacePhase()){
			boolean placed = false;
			//Ask the AI to pick a country until they pick a country without an owner
			while (!placed) {
				selectedCountry = ((AI) currentPlayer).pickRandomCountry(gameMap.getCountries());
				if (selectedCountry.getOccupier() == null)
					placed = true;
			}//end while
			placeArmies(1);
			nextPlayer();
			if (countriesClaimed==50)
				nextPhase();
		}//end if
		else if (isReinforcePhase() && !isPlayPhase()){
			while (selectedCountry == null){
				selectedCountry = ((AI)currentPlayer).placeNewTroops();
			}
			placeArmies(1);
			nextPlayer();
			//If the next player has no more troops, then go to redeem cards!
			if (currentPlayer.getAvailableTroops() == 0)
				nextPhase();
		}//end else if
		else if (isRedeemCardPhase()){
			currentPlayer.addAvailableTroops(redeemCards());
			nextPhase();
		}//end else if
		else if (isDeployPhase()){
			
			nextPhase();
		}//end else if
		else if (isAttackPhase()){
			
			nextPhase();
		}//end else if
		else if (isReinforcePhase()){
			
			nextPhase();
			nextPlayer();
		}//end else if
		else {
			throw new IllegalStateException("Illegal phase!");
		}//end else
	}//end aiturn
	
	/*
	 * humanTurn
	 * 
	 * plays one human turn
	 */
	private void humanTurn() throws IllegalStateException{
		if (isPlacePhase()){
			placeArmies(1);
			nextPlayer();
			if (countriesClaimed==50)
				nextPhase();
		}//end if
		else if (isReinforcePhase() && !isPlayPhase()){
			placeArmies(1);
			nextPlayer();
			//If the next player has no more troops, then go to redeem cards!
			if (currentPlayer.getAvailableTroops() == 0)
				nextPhase();
		}//end else if
		else if (isRedeemCardPhase()){
			currentPlayer.addAvailableTroops(redeemCards());
			nextPhase();
		}//end else if
		else if (isDeployPhase()){
			
			nextPhase();
		}//end else if
		else if (isAttackPhase()){
			
			nextPhase();
		}//end else if
		else if (isReinforcePhase()){
			
			nextPhase();
			nextPlayer();
		}//end else if
		else {
			throw new IllegalStateException("Illegal phase!");
		}//end else
	}//end aiturn
	
	
	public boolean aiChoicePlacement() {
		selectedCountry = ((AI) currentPlayer).pickRandomCountry(gameMap.getCountries());
		if (checkIfCountryAvailable(selectedCountry)) {
			placeArmies(1);
			selectedCountry = null;
			return true;
		}//end if
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
	
	
	public void placeArmies(int num) throws IllegalStateException{
		if (isPlacePhase()){
			//Sets player to this country's occupier and adds country
			//to player's list
			selectedCountry.setOccupier(currentPlayer);
			gameLog+=currentPlayer.getName() + " claimed " + selectedCountry.toString() +"\n";
			countriesClaimed++;
		}//end if
		else {
			gameLog += currentPlayer.getName() + " place " + num + " units on " + selectedCountry.toString() + "\n";
		}
		selectedCountry.setForcesVal(num);
		currentPlayer.subtractFromAvailableTroops(num);
	}//end placeArmies
	
	
	/*
	 * Given the player's current decision, redeem or don't redeem cards
	 */
	public int redeemCards(){
		ArrayList<Card> cards = currentPlayer.redeemCards();
		
		//If cards is null, the player didn't want to redeem anythign
		if (cards == null)
			return 0;
		
		//If we're here, we're redeeming
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
		}//end switch case
		
		
		return numArmies;
	}//end redeemCards
	
	
	public String attack(Country attackFrom, Country selectedCountry2,
			int numArmies) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	/**********************************************************************************
	***************************Shuffling Armies in Countries***************************
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

	/**********************************************************************************
	***************************The Getters and the Setters*****************************
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
	}

	public String getGameLog() {
		return gameLog;
	}

	public void setGameLog(String gameLog) {
		this.gameLog = gameLog;
	}

	public String getPhase() {
		// TODO Auto-generated method stub
		return "TBD";
	}

	public Map getMap() {
		return getGameMap();
	}

	public void skipAttackPhase() {
		// TODO Auto-generated method stub
		
	}

	public boolean skipCardRedemption() {
		return !currentPlayer.mustRedeemCards();
	}

	public int getNumRedemptions() {
		return numRedemptions;
	}



}// end theGame
