/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		CardPanel.java
 * 	Purpose:	CardPanel class sets up the card panel inside the stats panel for the risk gui.
 */

package gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class CardPanel extends JPanel {

	private Image myImage;
	private int xWidth;
	private int yHeight;

	/*
	 * constructor
	 */
	public CardPanel(Image im, int x, int y) {
		super();
		myImage = im;
		xWidth = x;
		yHeight = y;
	}// end constructor

	/*
	 * paintComponent
	 * 		Called in repaint. It pains the image to be a set size relative to the 
	 * 		size of the frame (calculated with xWidth and yHeight)
	 */
	public void paintComponent(Graphics g) {
		g.drawImage(myImage, 15, 15, (int) (3.25 * xWidth), (int) (8 * yHeight), null); 
	}// end paintComponent
}// end CardPanel