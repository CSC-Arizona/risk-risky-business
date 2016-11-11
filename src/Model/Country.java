package Model;

import java.util.ArrayList;

import javax.swing.JButton;

/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Country.java
 * 	Purpose:	Represent a single country
 */

public class Country {
	
	private String name;
	private double x;
	private double y;
	private int forcesVal;
	private Continents continent;
	private Player occupier;
	private JButton myButton;
	private ArrayList<Country> neighbors;

	public Country(String name, double x, double y, Continents continent){
		this.name = name;
		this.x = x;
		this.y = y;
		x = -1;
		y = -1;
		this.continent = continent;
		forcesVal = 0;
		occupier = null;
		neighbors = new ArrayList<Country>();
	}//end constructor
	
	
	public void addNeighbor(Country neighbor){
		neighbors.add(neighbor);
	}//end addNeighbor
	
	public void drawMyButton(){
		//TODO
	}//end drawMyButton
	
	
	public String getName(){
		return name;
	}//end getName
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	/*
	 * equals
	 * 
	 * Strictly used for finding if a country contains another
	 * country as a neighbor. The arraylist's contains method
	 * uses the equals method to do contains
	 */
	public boolean equals(Country comp){
		return name.equals(comp.getName());
	}//end equals
	
	
}
