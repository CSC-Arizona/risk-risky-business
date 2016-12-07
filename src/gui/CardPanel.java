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

	public CardPanel(Image im, int x, int y) {
		super();
		myImage = im;
		xWidth = x;
		yHeight = y;
	}// end constructor

	public void paintComponent(Graphics g) {
	//	xWidth = this.getWidth()/40;
	//	yHeight = this.getHeight()/40;
		g.drawImage(myImage, 15, 15, (int) (3.25 * xWidth), (int) (8 * yHeight), null); //this.getWidth()-10, this.getHeight()-10, null);//(int) (0.5 * xWidth), (int) (1.5 * yHeight), null);
	}// end paintComponent
}// end CardPanel