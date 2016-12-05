/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Card.java
 * 	Purpose:	Card class holds all card information for risk game.
 */

package Model;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Card implements Serializable{
	private String country;
	private String unit;
	private String filename;
	private ImageIcon myImage;

	/*
	 * Card(): Constructor
	 * parameters: String country
	 * 			   String unit
	 * returns: none
	 * Sets up Card object with its country, unit, and ImageIcon based on its country.
	 */
	public Card(String country, String unit) {
		this.country = country;
		this.unit = unit;
		this.filename = this.findMyFileName();
		myImage = new ImageIcon(filename);
	}// end card constructor

	/*
	 * getMyImage()
	 * parameters: none
	 * returns: Image
	 * calls getImage on the Card's myImage variable set in the constructor.
	 */
	public Image getMyImage() {
		return myImage.getImage();
	}//end getMyImage
	
	/*
	 * getMyImageIcon
	 * parameters: none
	 * returns: ImageIcon
	 * getter to return the imageIcon for the current card object
	 */
	public ImageIcon getMyImageIcon(){
		return myImage;
	}//end getMyImageIcon

	/*
	 * findMyFileName()
	 * parameters: none
	 * returns: String
	 * returns the string filename for the current card object
	 */
	private String findMyFileName() {
		return "RiskCards//" + removeWhiteSpace(country) + "Card.png";
	}// end findMyFileName

	/*
	 * removeWhiteSpace()
	 * parameters: none
	 * returns: String
	 * modifies the current country name to match the card filename equivalent.
	 * Assists in matching the card objects with the card images on file.
	 */
	private String removeWhiteSpace(String orig) {
		String str = "";
		int i = 0;

		while (i < orig.length()) {
			// add non-white-space
			while (i < orig.length() && orig.charAt(i) != ' ') {
				str += orig.charAt(i);
				i++;
			} // end while

			// move past white space
			while (i < orig.length() && orig.charAt(i) == ' ') {
				i++;
			} // end while
		} // end outer while

		return str;
	}// removeWhiteSpace

	/*
	 * getFilename(); getCountry(); getUnits()
	 * getters for the Card objects
	 */
	public String getFilename() {
		return filename;
	}//end getFilename

	public String getCountry() {
		return country;
	}// end getCountry

	public String getUnit() {
		return unit;
	}// end getUnit
	
	/*
	 * Override toString method for Card Objects
	 */
	public String toString(){
		return country + ", " + unit;
	}//end toString

}
