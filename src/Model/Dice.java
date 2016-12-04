package Model;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable{

	private int value;
	Random rnd = new Random();

	// initialize to 0 (has not been rolled)
	public Dice() {
		this.value = 0;
	}// end constructor

	// Rolls a given number of 6 sided die
	// TODO: This just returns the total sum. I think this may need
	// to be changed in order to follow the rules of Risk, depending on
	// how we are handling combat
	public int roll(int numDice) {
		int total = 0;
		int num;
		for (int i = 0; i < numDice; i++) {
			num = rnd.nextInt(6) + 1;
			// System.out.println("Die " + (i+1) + ": " + num);
			total += num;
		}
		// System.out.println("Total: " + total);
		this.value = total;
		return total;
	}// end roll

	// Returns the value of the die,
	// returns 0 if die has not been rolled once
	public int getValue() {
		return this.value;
	}// end getValue
}
