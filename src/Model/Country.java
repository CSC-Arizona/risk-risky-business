/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Country.java
 * 	Purpose:	Holds all information for a single country on the Risk map and make all buttons for the given country.
 */

package Model;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JButton;

public class Country extends Observable implements Serializable {

	private String name;
	private double x;
	private double y;
	private int xWidth;
	private int yHeight;
	private int forcesVal;
	private double buttonWidth;
	private double buttonHeight;
	private Player occupier;
	private JButton myButton;
	private ArrayList<Country> neighbors;

	/*
	 * Country(): Constructor sets all instance variables and default button
	 * size
	 */
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

	/*
	 * addNeighbor() parameters: Country neighbor returns: none purpose: adds
	 * the given country object to the neighbors arraylist.
	 */
	public void addNeighbor(Country neighbor) {
		neighbors.add(neighbor);
	}// end addNeighbor

	/*
	 * makeButton() parameters: int xWidth, int yHeight, ActionListener act
	 * returns: none purpose: creates a new button of given width and height
	 * with given actionlistener. calls updateButtonSize()
	 */
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

	/*
	 * updateButton() parameters: int xWidth, int yHeight returns: none purpose:
	 * changes the location of myButton to given xWidth and yHeight. calls
	 * updateButtonSize()
	 */
	public void updateButton(int xWidth, int yHeight) {
		this.xWidth = xWidth;
		this.yHeight = yHeight;
		myButton.setLocation((int) (x * xWidth), (int) (y * yHeight));
		updateButtonSize();
	}// end updateButton

	/*
	 * changeButtonSize() parameters: double width, double height returns: none
	 * purpose: change buttonWidth and buttonHeight to given width and height
	 */
	public void changeButtonSize(double width, double height) {
		// To be lazy - if the default was already good enough, I can enter
		// 0 to have one of the changes ignored
		if (width > 0)
			this.buttonWidth = width;
		if (height > 0)
			this.buttonHeight = height;
		// updateButtonSize();
	}// end changeButtonSize

	/*
	 * updateButtonSize() parameters: none returns: none purpose: sets the size
	 * of the button, taking instance variables into account.
	 */
	private void updateButtonSize() {
		myButton.setSize((int) (buttonWidth * xWidth),
				(int) (buttonHeight * yHeight));
	}// end updateButtonSize

	/*
	 * all getters and setters: return information held in private instance
	 * variables OR set new information to be held in private instance variables
	 */
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
		return getButton();
	}// end getMyButton

	public ArrayList<Country> getNeighbors() {
		return neighbors;
	}// end getNeighbors

	public void setForcesToZero() {
		forcesVal = 0;
	}// end setForcesToZero

	public void setOccupier(Player player) {
		// If there was a previous owner
		if (occupier != null)
			occupier.loseCountry(this);

		// Now, set the new owner
		occupier = player;
		occupier.occupyCountry(this);
		setChanged();
		notifyObservers();
	}// end setOccupier

	public void addForcesVal(int i) {
		forcesVal += i;
		setChanged();
		notifyObservers();
	}// end setForcesVal

	/*
	 * equals() parameters: Country comp returns: boolean purpose: Strictly used
	 * for finding if a country contains another country as a neighbor. The
	 * arraylist's contains method uses the equals method to do contains.
	 */
	public boolean equals(Country comp) {
		return name.equals(comp.getName());
	}// end equals

	/*
	 * Override Object's toString() method
	 */
	public String toString() {
		return name;
	}// end toString

	/*
	 * returnMyOwnersFaction() parameters: none returns: Faction enum purpose:
	 * returns the faction of the occupier of this object
	 */
	public Faction returnMyOwnersFaction() {
		return this.getOccupier().getFaction();
	}// end returnMyOwnersFaction

	/*
	 * removeUnits() parameters: int numOfUnitsToMove returns: none purpose:
	 * subtracts the given number of units from the forcesVal instance variable
	 */
	public void removeUnits(int numOfUnitsToMove) {
		forcesVal -= numOfUnitsToMove;
		setChanged();
		notifyObservers();

	}// end removeUnits

	public boolean isMyNeighbor(Country other) {
		// That means other wasn't in neighbors
		/*
		 * if (neighbors.indexOf(other) == -1){ return false; } else return
		 * true;
		 */
		return neighbors.contains(other);
	}// end isMyNeighbor

}// end countryClasss
