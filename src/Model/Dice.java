package Model;

import java.util.Random;

public class Dice {

	private int value; 
	Random rnd = new Random();
	
	//initialize to 0 (has not been rolled)
	public Dice(){
		this.value = 0; 
	}
	
	//Rolls a 6 sided die
	public int roll(){ 
		int num = rnd.nextInt(6) + 1;
		this.value = num;
		return num;
	}
	
	//Returns the value of the die, 
	//returns 0 if die has not been rolled once
	public int getValue(){
		return this.value;
	}
}
