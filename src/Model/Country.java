package Model;

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

	public Country(String name, double x, double y, Continent continent)
	{
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

	public void addNeighbor(Country neighbor)
	{
		neighbors.add(neighbor);
	}// end addNeighbor

	public void drawMyButton()
	{
		// TODO
	}// end drawMyButton

	public void makeButton(int xWidth, int yHeight, ActionListener act)
	{
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

	public void updateButton(int xWidth, int yHeight)
	{
		myButton.setLocation((int) (x * xWidth), (int) (y * yHeight));
	}

	public void changeButtonSize(int height, int width)
	{
		myButton.setSize(width, height);
	}

	public JButton getButton()
	{
		return myButton;
	}

	public String getName()
	{
		return name;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public int getForcesVal()
	{
		return forcesVal;
	}

	public Player getOccupier()
	{
		return occupier;
	}

	public JButton getMyButton()
	{
		return myButton;
	}

	public ArrayList<Country> getNeighbors()
	{
		return neighbors;
	}

	/*
	 * equals
	 * 
	 * Strictly used for finding if a country contains another country as a
	 * neighbor. The arraylist's contains method uses the equals method to do
	 * contains
	 */
	public boolean equals(Country comp)
	{
		return name.equals(comp.getName());
	}// end equals

	public void setOccupier(Player player)
	{
		occupier = player;
		setChanged();
		notifyObservers();

	}

	public void setForcesVal(int i)
	{
		forcesVal += i;

	}

	public String toString()
	{
		return name;
	}

	public Faction returnMyOwnersFaction()
	{
		return this.getOccupier().getFaction();
	}


}
