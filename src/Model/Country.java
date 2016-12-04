package Model;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JButton;

/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Country.java
 * 	Purpose:	Represent a single country
 */

public class Country extends Observable implements Serializable{

	private String name;
	private double x;
	private double y;
	private int xWidth;
	private int yHeight;
	private int forcesVal;
	private double buttonWidth;
	private double buttonHeight;
	// private Continent continent;
	private Player occupier;
	private JButton myButton;
	private ArrayList<Country> neighbors;

	public Country(String name, double x, double y, Continent continent) {
		this.name = name;
		this.x = x;
		this.y = y;
		xWidth = 1;
		yHeight = 1;
		// Not great design, but it's easy!
		continent.addCountry(this);
		forcesVal = 0;
		occupier = null;
		neighbors = new ArrayList<Country>();
		myButton = null;

		// Determine default button size
		if (name.length() < 5) {
			buttonWidth = 1;
			buttonHeight = 1;
		} else {
			buttonWidth = name.length() / 4;
			buttonHeight = 1;
		}
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
		myButton.setSize((int) (buttonWidth * xWidth),
				(int) (buttonHeight * yHeight));
		myButton.addActionListener(act);
		updateButtonSize();
	}// end makeButton

	public void updateButton(int xWidth, int yHeight) {
		this.xWidth = xWidth;
		this.yHeight = yHeight;
		myButton.setLocation((int) (x * xWidth), (int) (y * yHeight));
		updateButtonSize();
	}// end updateButton

	public void changeButtonSize(double width, double height) {
		// To be lazy - if the default was already good enough, I can enter
		// 0 to have one of the changes ignored
		if (width > 0)
			this.buttonWidth = width;
		if (height > 0)
			this.buttonHeight = height;
		// updateButtonSize();
	}// end changeButtonSize

	private void updateButtonSize() {
		myButton.setSize((int) (buttonWidth * xWidth),
				(int) (buttonHeight * yHeight));
	}// end updateButtonSize

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
