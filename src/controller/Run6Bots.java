/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Run6Bots.java
 * 	Purpose:	This tournament mode class plays the game with just all AI players to test the AI strategies
 */

package controller;

import Model.AI;
import Model.EasyAI;
import Model.MediumAI;

public class Run6Bots {

	public static void main(String[] args) {
		// Get an instance of the game using the special tournament mode
		// flag turned on
		TheGame theGame = TheGame.getInstance(0, 6, true);
		int i = 0;
		int hardWin = 0, mediumWin = 0, easyWin = 0, numAttacks = 0;

		while (i < 1000) {
			// We liked to see this running so that we knew the game was
			// running, but you
			// can comment it out if you only want to see the results
			System.out.println("Game : " + (i + 1));

			theGame.startGame();
			if (((AI) theGame.getCurrentPlayer()).getStrategy() instanceof EasyAI) {
				easyWin++;
			} else if (((AI) theGame.getCurrentPlayer()).getStrategy() instanceof MediumAI) {
				mediumWin++;
			} else
				hardWin++;
			i++;
			// New game, same number of players
			theGame.newGame(6);

		}

		System.out.println("\nEasy won " + easyWin + " times.");
		System.out.println("Medium won " + mediumWin + " times.");
		System.out.println("Hard won " + hardWin + " times.");

	}

}
