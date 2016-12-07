/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Run6Bots.java
 * 	Purpose:	This tournament mode class plays the game with just all AI players to test the AI strategies
 */

package Model;

public class Run6Bots {

	public static void main(String[] args) {
		TheGame theGame = TheGame.getInstance(0, 6, true);
		int i = 0;
		int hardWin = 0, mediumWin = 0, easyWin = 0;

		while (i < 3) {

			System.out.println("New Game");
			System.out.println("Game " + (i+1));
			theGame.startGame();
			System.out.println(theGame.getCurrentPlayer().getName() + " Won.");
			if (((AI) theGame.getCurrentPlayer()).getStrategy() instanceof EasyAI) {
				easyWin++;
			} else if (((AI) theGame.getCurrentPlayer()).getStrategy() instanceof MediumAI) {
				mediumWin++;
			} else
				hardWin++;
			i++;

			theGame.newGame(6);

		}

		System.out.println("Easy won " + easyWin + " times.");
		System.out.println("Medium won " + mediumWin + " times.");
		System.out.println("Hard won " + hardWin + " times.");
	}

}
