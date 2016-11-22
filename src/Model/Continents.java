package Model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * 
 */

public class Continents {
	private String name; 
	private ArrayList<Country> countries;
	Color color; 
	
	public Continents(String name, ArrayList<Country> countries, Color color){ 
		this.name = name;
		this.countries = countries;
		this.color = color;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean continentBonus(){
		Iterator<Country> itr = countries.iterator();
		for(Country curr: countries){ 
			if(curr.getOccupier() != itr.next().getOccupier())
				return false;
		}
		
		return true; 
	}
	
}