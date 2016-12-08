/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Dice.java
 * 	Purpose:	Dice class "rolls" a number of dice given and returns an array of dice objects.
 */

package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Dice implements Serializable, Comparable<Dice> {

	private int value;
	

	// initialize to 0 (has not been rolled)
	public Dice(int i) {
		this.value = i;
	}// end constructor
	

	// Rolls a given number of 6 sided die
	// Returns a sorted arraylist of dice.
	// The amount of dice is passed into the function by the numDice method
	public static ArrayList<Dice> roll(int numDice) {
		Random rnd = new Random();
		ArrayList<Dice> myRolledDice = new ArrayList<>();
		int num = 0;
		for (int i = 0; i < numDice; i++) {
			num = rnd.nextInt(6) + 1;
			// System.out.println("Die " + (i+1) + ": " + num);
			Dice die = new Dice(num);
			myRolledDice.add(die);
		}

		Collections.sort(myRolledDice);

		return myRolledDice;
	}// end roll

	// Returns the value of the die,
	// returns 0 if die has not been rolled once
	public int getValue() {
		return this.value;
	}// end getValue

	/*
	 * compares one dice's value to another
	 */
	@Override
	public int compareTo(Dice other) {

		if (this.value < other.value)
			return 1;
		else
			return -1;
	}

}
