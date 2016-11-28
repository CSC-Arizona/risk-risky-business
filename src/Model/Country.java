package Model;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JButton;

/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Country.java
 * 	Purpose:	Represent a single country
 */

public class Country extends Observable {

	private String name;
	private double x;
	private double y;
	private int forcesVal;
	// private Continent continent;
	private Player occupier;
	private JButton myButton;
	private ArrayList<Country> neighbors;

	public Country(String name, double x, double y, Continent continent) {
		this.name = name;
		this.x = x;
		this.y = y;
		x = -1;
		y = -1;
		// Not great design, but it's easy!
		continent.addCountry(this);
		forcesVal = 0;
		occupier = null;
		neighbors = new ArrayList<Country>();
		myButton = null;
	}// end constructor

	public void addNeighbor(Country neighbor) {
		neighbors.add(neighbor);
	}// end addNeighbor

	public void drawMyButton() {
		// TODO
	}// end drawMyButton

	public void makeButton(int xWidth, int yHeight, ActionListener act) {
		myButton = new JButton();
		myButton.setLocation((int) (x * xWidth), (int) (y * yHeight));
		myButton.setContentAreaFilled(false);
		myButton.setActionCommand(name);
		if (name.length() < 5)
			myButton.setSize(75, 25);
		else
			myButton.setSize(name.length() * 8, 25);

		myButton.addActionListener(act);

	}// end makeButton

	public void updateButton(int xWidth, int yHeight) {
		myButton.setLocation((int) (x * xWidth), (int) (y * yHeight));
	}// end updateButton

	public void changeButtonSize(int height, int width) {
		myButton.setSize(width, height);
	}// end changeButtonSize

	public JButton getButton() {
		return myButton;
	}// end getButton

	public String getName() {
		return name;
	}// end getName

	public double getX() {
		return x;
	}// end getX

	public double getY() {
		return y;
	}// end getY

	public int getForcesVal() {
		return forcesVal;
	}// end getForcesVal

	public Player getOccupier() {
		return occupier;
	}// end getOccupier

	public JButton getMyButton() {
		return myButton;
	}// end getMyButton

	public ArrayList<Country> getNeighbors() {
		return neighbors;
	}// end getNeighbors

	/*
	 * equals
	 * 
	 * Strictly used for finding if a country contains another country as a
	 * neighbor. The arraylist's contains method uses the equals method to do
	 * contains
	 */
	public boolean equals(Country comp) {
		return name.equals(comp.getName());
	}// end equals

	public void setOccupier(Player player) {
		occupier = player;
		setChanged();
		notifyObservers();

	}// end setOccupier

	public void setForcesVal(int i) {
		forcesVal += i;

	}// end setForcesVal


	public String toString() {
		return name;
	}// end toString

	public Faction returnMyOwnersFaction() {
		return this.getOccupier().getFaction();
	}// end returnMyOwnersFaction

	public void removeUnits(int numOfUnitsToMove) {
		forcesVal -= numOfUnitsToMove;

	}// end removeUnits

}// end countryClasss
