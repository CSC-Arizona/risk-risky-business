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
		xWidth = this.getWidth()/40;
		yHeight = this.getHeight()/40;
		g.drawImage(myImage, 10, 10, this.getWidth()-10, this.getHeight()-10, null);//(int) (0.5 * xWidth), (int) (1.5 * yHeight), null);
	}// end paintComponent
}// end CardPanel