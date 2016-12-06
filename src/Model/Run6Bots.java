/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Run6Bots.java
 * 	Purpose:	This tournament mode class plays the game with just all AI players to test the AI strategies
 */

package Model;

public class Run6Bots {

	public static void main(String[] args) {
		TheGame theGame = TheGame.getInstance(0,6, true);
		int i = 0;
//		for(Player player : theGame.getPlayers())
//		{
//			if(i < 2){
//				((AI) player).setStrategy(new EasyAI((AI) player));
//				player.setFaction(i);
//				player.setName("");
//			}
//			else if(i < 4)
//			{
//				((AI) player).setStrategy(new MediumAI((AI) player));
//				player.setFaction(i);
//				player.setName("");
//			}
//			else{
//				((AI) player).setStrategy(new HardAI((AI) player));
//				player.setFaction(i);
//				player.setName("");
//			}
//			
//			i++;
//		}
		
		i = 0;
		int hardWin = 0, mediumWin = 0, easyWin = 0;
		
		System.out.println("the game is starting");
		theGame.startGame();
		
		while(i < 1000)
		{
			
			if(((AI) theGame.getCurrentPlayer()).getStrategy() instanceof EasyAI){
				easyWin++;}
			else if(((AI) theGame.getCurrentPlayer()).getStrategy() instanceof MediumAI){
				mediumWin++;}
			else 
				hardWin++;
			i++;
			System.out.println("New game about to start");
			theGame.newGame(6);
			theGame.startGame();
			
		}
		
		System.out.println("Easy won " + easyWin + " times.");
		System.out.println("Medium won " + mediumWin + " times.");
		System.out.println("Hard won " + hardWin + " times.");
	}

}
