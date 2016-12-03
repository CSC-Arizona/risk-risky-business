package Model;

public class TournamentMode {

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
