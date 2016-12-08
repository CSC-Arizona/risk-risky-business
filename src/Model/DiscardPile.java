package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class DiscardPile implements Serializable{
	
	private ArrayList<Card> pile;
	private int size;
	/*
	 * Constructor
	 */
	public DiscardPile(){
		pile = new ArrayList<Card>();
		size=0;
	}
	
	public ArrayList<Card> getPile(){
		return pile;
	}
	
	public int getSize(){
		return size;
	}
	/*
	 * adds a card to the pile and increments the size variable
	 */
	public void addToPile(Card e){
		pile.add(e);
		size++;
	}
	
	/*
	 * adds a whole arraylist to the pile, and increments the size variable by the size of the array to add
	 */
	public void addToPile(ArrayList<Card> e){
		pile.addAll(e);
		size+=e.size();
	}
	
	/*
	 * empties the pile, and sets the size variable to zero
	 */
	public void removeAll(){
		pile.clear();
		size=0;
	}
}
