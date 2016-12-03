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

	public Card(String country, String unit) {
		this.country = country;
		this.unit = unit;
		this.filename = this.findMyFileName();
		myImage = new ImageIcon(filename);
	}// end card constructor

	public Image getMyImage() {
		return myImage.getImage();
	}

	private String findMyFileName() {
		return "RiskCards//" + removeWhiteSpace(country) + "Card.png";
	}// end findMyFileName

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

	public String getFilename() {
		return filename;
	}

	public String getCountry() {
		return country;
	}// end getCountry

	public String getUnit() {
		return unit;
	}// end getUnit
	
	public String toString(){
		return country + ", " + unit;
	}//end toString

}
