package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public interface AIStrategy extends Serializable{

	Random rand = new Random();
	
	String reinforce();
	ArrayList<Country> placeNewTroops();
	Country placeLeftOverUnits();
	Country placeUnit();
	void setMe(AI ai);
	
	
	

}
