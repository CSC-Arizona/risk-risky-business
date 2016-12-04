package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

public class Game implements Serializable{
	private ArrayList<Player> players;
	private Map gameMap;
	private Deck deck;
	private Country selectedCountry, aiSelectedCountry;
	private boolean placePhase, playPhase, reinforcePhase, deployPhase, attackPhase, gameOver, redeemCardPhase;
	private int humans;
	private int totalPlayers, armiesPlaced, playerLocation;
	private static Game theGame;
	private int numRedemptions;
	private boolean canPlace, tournamentMode;
	private int countriesBefore;
	private int countriesAfter;
	private String gameLog;
	public static final String FILE_NAME = "game.ser";

	private Game(int numOfHumanPlayers, int numOfAIPlayers, boolean tourny) {
		humans = numOfHumanPlayers;
		totalPlayers = numOfHumanPlayers + numOfAIPlayers;
		armiesPlaced = 0;
		placePhase = true;
		playPhase = false;
		attackPhase = false;
		reinforcePhase = false;
		deployPhase = false;
		gameOver = false;
		playerLocation = 0;
		numRedemptions = 0;
		canPlace = false;
		redeemCardPhase = false;
		tournamentMode = tourny;
		if(!tournamentMode)
			newGame();
		else
			newGame(numOfAIPlayers);

	}// end constructor

	public static Game getInstance(int numOfHumanPlayers, int totalNumOfPlayers, boolean tourny) {
		if (theGame == null)
			theGame = new Game(numOfHumanPlayers, totalNumOfPlayers, tourny);

		return theGame;
	}// end getInstance

	public void clear(){
		theGame = null;
	}
	
	public void newGame() {
		if (players != null)
			players.removeAll(players);
		selectedCountry = null;
		aiSelectedCountry = null;
		gameMap = Map.getInstance(0);
		gameMap = gameMap.newMap();
		deck = Deck.getInstance();
		deck = deck.newDeck();
		// deck.shuffle();
		players = new ArrayList<>();
		addHumanPlayers(humans);
		addAI(totalPlayers - humans);
		numRedemptions = 0;

	}// end newGame
	
	//used for tournament mode
	public void newGame(int i)
	{
		if(players != null)
			players.removeAll(players);
		
		selectedCountry = null;
		aiSelectedCountry = null;
		gameMap = Map.getInstance(1);
		gameMap = gameMap.newTourneyMap();
		deck = Deck.getInstance();
		deck=deck.newDeck();
		// deck.shuffle();
		players = new ArrayList<>();
		addAI(i);
		numRedemptions = 0;
	}

	// makes sure all country buttons are enabled
	private void turnOnCountryButtons() {
		for (Country country : gameMap.getCountries()) {
			if (country.getMyButton() != null)
				country.getMyButton().setEnabled(true);
		}//end for
	}// end turnOnCountryButtons

	public void setPlayers(ArrayList<Player> thePlayers) {
		players = thePlayers;
	}

	public int getNumRedemptions() {
		return numRedemptions;
	}// end getNumRemptions

	public void incrementNumRedemptions() {
		numRedemptions++;
	}// end incrementNumRedemptions
	

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
		if (isPlacePhase()){//armiesPlaced < 50) {
			System.out.println("I WAS AT A AT CRASH");
			if (countryToPlace.getOccupier() == null) {
				players.get(playerLocation).occupyCountry(countryToPlace);
				countryToPlace.setOccupier(players.get(playerLocation));
				countryToPlace.addForcesVal(numToPlace);
				armiesPlaced++;
				if (armiesPlaced == 50) {
					//placePhase = false;
					//reinforcePhase = true;
					nextPhase();
				} // end if
				gameLog+=players.get(playerLocation).getName() + " claimed" + countryToPlace.toString() +"\n";
				System.out.println(armiesPlaced);
				System.out.println("Next players turn");
				System.out.println("Army placed at : " + countryToPlace.toString());
				players.get(playerLocation).subtractFromAvailableTroops(numToPlace);

			} else {
				System.out.println("That country is already Occupied");
				System.out.println(armiesPlaced);

			} // end else
		} else if (isDeployPhase()) {
			System.out.println("I WAS AT B AT CRASH");
			if (players.get(playerLocation).getAvailableTroops() > 0
					&& countryToPlace.getOccupier().equals(players.get(playerLocation))) {
				countryToPlace.addForcesVal(numToPlace);
				players.get(playerLocation).subtractFromAvailableTroops(numToPlace);
				if (players.get(playerLocation).getAvailableTroops() == 0) {
					//deployPhase = false;
					//attackPhase = true;
					//nextPhase();
				}//end if
				gameLog+=players.get(playerLocation).getName() + " placed " + numToPlace + " units on " + countryToPlace.getName() + "\n";
			}//end if
		} else if (players.get(playerLocation).getAvailableTroops() > 0)
		// place remaining armies
		{
			System.out.println("I WAS AT C AT CRASH");
			// placePhase = false;
			// reinforcePhase = true;

			if (countryToPlace.getOccupier().equals(players.get(playerLocation))) {
				countryToPlace.addForcesVal(numToPlace);
				armiesPlaced++;
				gameLog+="Reinforced " + countryToPlace + " " + armiesPlaced+"\n";
				System.out.println("Reinforced " + countryToPlace + " " + armiesPlaced);// selectedCountry.getName());
				players.get(playerLocation).subtractFromAvailableTroops(numToPlace);
				gameLog+=players.get(playerLocation).getName() + " placed " + numToPlace + " units on " + countryToPlace.getName() + "\n";
			} else
				System.out.println("You don't occupy this country");

		} else {
			System.out.println("I WAS AT D AT CRASH");
			//placePhase = false;
			//reinforcePhase = false;
			//playPhase = true;
			//deployPhase = true;
			nextPhase();
			// we should now be back at the beginning of the players list
			// already,
			// so we need to give
			// that player units for the first turn
		}//end else
	}// end placeArmies

	private void addAI(int numOfAI) {
		for (int i = 0; i < numOfAI; i++)
			players.add(new AI(AIStrat.EASY, totalPlayers));
	}// end addAi

	public Country getSelectedCountry() {
		return selectedCountry;
	}// end

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
		
		gameLog+= "\nNext player's turn: " + getCurrentPlayer().getName() + "\n";
		// If it's the play phase, apply any continent bonuses
		if (isPlayPhase()) {
			players.get(playerLocation).addAvailableTroops(gameMap.getContinentBonuses(getCurrentPlayer()));
			System.out.println("Continent bonus applied:" + gameMap.getContinentBonuses(getCurrentPlayer()));
			gameLog+="Continent bonus applied: " + gameMap.getContinentBonuses(getCurrentPlayer()) + "\n";
		}

		if (players.get(playerLocation) instanceof AI) {
			aiTurn();
		} else if (isDeployPhase() && players.get(playerLocation) instanceof HumanPlayer)
			players.get(playerLocation).getTroops();

	}// end nextPlayer

	private void aiTurn() {
		boolean done = false;
		if (gameOver) {// for now do nothing
			return;
		}
		while (!done) {
			if (isPlacePhase()) {
				boolean placed = false;
				while (!placed) {
					placed = aiChoicePlacement();
				}
				done = true;
			} else if (isReinforcePhase() && !isPlayPhase()) {
				aiReinforcePlacement();
				done = true;
			} else if (isPlayPhase() && isDeployPhase()) {
				players.get(playerLocation).getTroops();
		//		((AI) players.get(playerLocation)).setRedemptions(numRedemptions);
				int redeem = 0;//TODO replace the 0 with: players.get(playerLocation).redeemCards();
				if (redeem > 0)
					numRedemptions++;
				while (players.get(playerLocation).getAvailableTroops() > 0) {
					aiReinforcePlacement();
				}//end while
				//deployPhase = false;
				//attackPhase = true;
				nextPhase();
			} else if (isPlayPhase() && isAttackPhase()) {
				String finishedAttacking = "";
				int aiCountries = players.get(playerLocation).getCountries().size();
				while (finishedAttacking != null) {
					gameLog += finishedAttacking+"\n";
					finishedAttacking = ((AI) players.get(playerLocation)).aiAttack();
				}
//	TODO uncomment these lines when cardRedemption is finished
				//if (aiCountries < players.get(playerLocation).getCountries().size())
//					players.get(playerLocation).addCard(deck.deal());

				removeLosers();
				isFinished();
				//attackPhase = false;
				//reinforcePhase = true;
				nextPhase();
			} else if (isPlayPhase() && isReinforcePhase()) {
				aiPlayReinforce();
				//reinforcePhase = false;
				//deployPhase = true;
				nextPhase();
				done = true;
			}
		}
		nextPlayer();
		//nextPlayer();
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
	
	public boolean isRedeemCardPhase(){
		return redeemCardPhase;
	}

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

	public int getUnits(Country countryToRemoveUnits) {
		boolean moveFlag = false, continueFlag = false;
		int totalUnits = countryToRemoveUnits.getForcesVal(), unitsToReturn = 0;
		String unitsToMove = "";

		while (!moveFlag) {
			unitsToMove = JOptionPane.showInputDialog("How many armies do you want to move over? You must leave 1.");
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

	public boolean attack(Country yours, Country theirs, int numDice) {
		Dice die = new Dice(0);
		boolean winFlag = false;
		//Update the gameLog
//		gameLog+=yours.getOccupier().getName() + " attacked " + theirs.getName() + " with " + numArmies + " units.\n";
		int enemyDice = 0;
		if(theirs.getOccupier() instanceof HumanPlayer)
		{
			//prompt player for how many dice to throw
		}
		else	
			if(theirs.getForcesVal() >= 2)
			{
				enemyDice = 2;
			}
			else
				enemyDice = 1;
		
		ArrayList<Dice> attackingDice = die.roll(numDice);
		ArrayList<Dice> defendingDice = die.roll(enemyDice);
		
		for(int i = 0; i < defendingDice.size(); i++)
		{
			
			
			if(attackingDice.get(i).getValue() > defendingDice.get(i).getValue())
			{
				theirs.removeUnits(1);
				if(theirs.getForcesVal() == 0)
				{	winFlag = true;
					break;
				
				}
				
			}
			else{
				yours.removeUnits(1);
				if(yours.getForcesVal() == 1){
					winFlag = false;
					break;
				}
			}
		}
		
				
		if(winFlag)
		{
			Player loser = theirs.getOccupier();
			loser.loseCountry(theirs);
			theirs.setOccupier(getCurrentPlayer());
			getCurrentPlayer().occupyCountry(theirs);
			return true;
		}
		else
			return false;


	}// end attack

	

	public boolean isAttackPhase() {
		return attackPhase;
	}// end isAttackPhase

	public void skipAttackPhase() {
		if (countriesBefore < countriesAfter){
			getCurrentPlayer().addCard(deck.deal());
			gameLog+= getCurrentPlayer().getName() + " earned a new card.\n";
		}
			
	//	attackPhase = false;
	//	reinforcePhase = true;
		nextPhase();
		countriesBefore = 0;
		countriesAfter = 0;
	}// end skipAttackPhase

	public void finishTurn() {
		//deployPhase = true;
		//attackPhase = false;
		//reinforcePhase = false;
		nextPhase();
		nextPlayer();
	}// end finishTurn

	// checks if all countries are occupied by the same player, if so returns
	// true, otherwise returns false
	public boolean isFinished() {

		if (players.size() == 1) {
			gameOver = true;
			playPhase = false;
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

	// checks if all players have at least one country. If they do not, remove
	// them from the game.
	public void removeLosers() {
		ArrayList<Player> playersToRemove = new ArrayList<>();
		ArrayList<Integer> playersToRemoveLocations = new ArrayList<>();
		for (Player player : players) {
			if (player.getCountries().size() == 0) {
				System.out.println(player.getName() + " has been wiped off the map.");
				gameLog+=player.getName() + " has been wiped off the map.\n";
				playersToRemove.add(player);
				playersToRemoveLocations.add(players.indexOf(player));
			}

		}
		ArrayList<Card> cardsToAddToDiscard = new ArrayList<Card>();
		for (Player player : playersToRemove) {
			cardsToAddToDiscard.addAll(player.discardCards());
		}
		if(cardsToAddToDiscard.size()>0)
			deck.addToDiscardPile(cardsToAddToDiscard);
		players.removeAll(playersToRemove);
		totalPlayers -= playersToRemove.size();
		for(Integer removedPlayersLoc : playersToRemoveLocations)
		{
			if(removedPlayersLoc < playerLocation)
			{
				playerLocation--;
			}
		}

	}// end removeLosers
	
	public boolean skipCardRedemption(){
		if (!getCurrentPlayer().mustRedeemCards()){
			nextPhase();
			return true;
		}//end if
		else
			return false;
	}//end skipCardRedemption
	
	
	/*
	 * These methods handle moving our code from one phase to the next
	 */
	public void nextPhase() throws IllegalStateException{
		if (isPlacePhase()){
			changeToReinforcePhase();
		}//ed if
		else if (isReinforcePhase()){
			changeToRedeemCardsPhase();
		}//end else if
		else if (isRedeemCardPhase()){
			changeToDeployTroopsPhase();
		}//end else if
		else if (isDeployPhase()){
			changeToAttackPhase();
		}//end else if
		else if (isAttackPhase()){
			changeToReinforcePhase();
		}//end else if
		else if (isFinished()){
			System.out.println("The game is over");
		}
		else {
			throw new IllegalStateException("The phase was invalid");
		}
	}//end nextPhase
	
//	private boolean placePhase, playPhase, reinforcePhase, deployPhase, attackPhase, gameOver, redeemCardPhase;
	
	private void changeToReinforcePhase(){
		placePhase = false;
		reinforcePhase = true;
		redeemCardPhase = false;
		deployPhase = false;
		attackPhase = false;
		gameOver = false;
	}//end changetoReinforcePhase
	
	private void changeToRedeemCardsPhase(){
		playPhase = true;
		placePhase = false;
		reinforcePhase = false;
		redeemCardPhase = true;
		deployPhase = false;
		attackPhase = false;
		gameOver = false;
	}//end changeToRedeemCardsPhase
	
	private void changeToDeployTroopsPhase(){
		placePhase = false;
		reinforcePhase = false;
		redeemCardPhase = false;
		deployPhase = true;
		attackPhase = false;
		gameOver = false;
	}//end changeToDeployTroopsPhase
	
	private void changeToAttackPhase(){
		placePhase = false;
		reinforcePhase = false;
		redeemCardPhase = false;
		deployPhase = false;
		attackPhase = true;
		gameOver = false;
	}//end changetoattackphase

	
	
	public Deck getDeck() {
		return deck;
	}

	public Map getMap() {
		return gameMap;
	}

	public String getGameLog() {
		return gameLog;
	}
}// end GameClasss
