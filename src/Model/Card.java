package Model;

import java.util.ArrayList;

public class Card {
	private String country;
	private String unit;
	
	public Card(String country, String unit){
		this.country = country;
		this.unit = unit;
	}//end card constructor
	
	public String getCountry(){
		return country;
	}//end getCountry
	
	public String getUnit(){
		return unit;
	}//end getUnit

}
