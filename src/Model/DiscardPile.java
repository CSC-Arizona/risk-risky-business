package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class DiscardPile implements Serializable{
	
	private ArrayList<Card> pile;
	private int size;
	
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
	
	public void addToPile(Card e){
		pile.add(e);
		size++;
	}
	
	public void addToPile(ArrayList<Card> e){
		pile.addAll(e);
	}
	
	public void removeAll(){
		pile.clear();
		size=0;
	}
}
