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
		int j = 0;
		while (true){
			j++;
			
			TheGame theGame = TheGame.getInstance(0, 6, true);
			int i = 0;
			int hardWin = 0, mediumWin = 0, easyWin = 0, numAttacks=0;
			while (i < 1000) {
				long startTime = System.nanoTime();
				//if (i % 200 == 0)
					System.out.print(i + " ");
//				System.out.println("New Game");
//				System.out.println("Game " + (i+1));
				theGame.startGame();
//				System.out.println(theGame.getCurrentPlayer().getName() + " Won.");
				if (((AI) theGame.getCurrentPlayer()).getStrategy() instanceof EasyAI) {
					easyWin++;
				} else if (((AI) theGame.getCurrentPlayer()).getStrategy() instanceof MediumAI) {
					mediumWin++;
				} else
					hardWin++;
				i++;
				System.out.println("Attacks made in this game: " + theGame.getNumAttacks());
				numAttacks +=theGame.getNumAttacks();
				theGame.newGame(6);
				long endTime = System.nanoTime();
				System.out.println("The tests took " + (double)(1000000000 * (endTime - startTime)) + " seconds to run.\n");
			}
			
			System.out.println("Test #" + j);
			System.out.println("\nEasy won " + easyWin + " times.");
			System.out.println("Medium won " + mediumWin + " times.");
			System.out.println("Hard won " + hardWin + " times.");
			System.out.println("On average, AIs made " + numAttacks/1000 + " attacks per game.");
			
		}
		
	}

}
