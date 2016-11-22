package Model;

import java.util.ArrayList;

public class Card {
	private String country;
	private String unit;
	
	public Card(String country, String unit){
		this.country = country;
		this.unit = unit;
	}
	
	public String getCountry(){
		return country;
	}
	
	public String getUnit(){
		return unit;
	}

}
