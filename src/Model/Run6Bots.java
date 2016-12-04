/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Run6Bots.java
 * 	Purpose:	This tournament mode class plays the game with just all AI players to test the AI strategies
 */

package Model;

public class Run6Bots {

	public static void main(String[] args) {
		Game theGame = Game.getInstance(0,6, true);
		int i = 0;
		for(Player player : theGame.getPlayers())
		{
			if(i < 3){
				((AI) player).setMyStrat(AIStrat.EASY);
				player.setFaction(i);
				player.setName("");
			}
			else{
				((AI) player).setMyStrat(AIStrat.HARD);
				player.setFaction(i);
				player.setName("");
			}
			
			i++;
		}
		
		i = 0;
		int hardWin = 0, easyWin = 0;
		while(i < 1000)
		{
			theGame.startGame();
			if(((AI) theGame.getCurrentPlayer()).getStrat().compareTo("EASY") == 0)
				easyWin++;
			else
				hardWin++;
			
			i++;
			theGame.newGame(6);
		}
		
		System.out.println("Easy won " + easyWin + " times.");
		System.out.println("Hard won " + hardWin + " times.");
	}

}
