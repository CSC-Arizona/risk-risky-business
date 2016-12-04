package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Model.Game;

/**
 * (Heavily inspired by DuckHuntStart) 
 * 
 * @author Jewell Finder, Dylan Tobia, Abigail Dodd, Sydney Komro
 * 
 */
public class AnimationPanel extends JPanel implements Observer {

	private BufferedImage sheet, background;
	private JLabel scoreLabel;
	private Point upperLeft;
	private Cursor cursor;
	private Game model;
	private int misses;

	public AnimationPanel() {
		loadImages();
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gr = (Graphics2D) g;

		if (model == null) {
			return;
		}

		gr.drawImage(background, 0, 0, null);

		if (starting) {
			// Draw the characters
			// roll 
			// draw the swoosh attack

		} else if (model.getHit() == 0) {
			//if getHit == 0, offense misses entirely
			// wiggle the offense

		} else if (model.getHit() == 1){
			//if getHit == 1, winner won one and lost one
			// wiggle both

		} else { 
			//critical hit! All offense blows land 
			//wiggle defense
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

	public void updateAnimations() {
		// What do I need to draw? (check state)
		if (starting) {
			// Draw the characters
			// roll 
			// draw the swoosh attack

		} else if (model.getHit() == 0) {
			//if getHit == 0, offense misses entirely
			// wiggle the offense

		} else if (model.getHit() == 1){
			//if getHit == 1, winner won one and lost one
			// wiggle both

		} else { 
			//critical hit! All offense blows land 
			//wiggle defense
		}
	}

	/*
	 * TODO: Locations of the first front and back of each sprite, offset of each frame, etc
	 * (Numbers have not been changed from DuckHunt, but number of frames for each character is correct)
	 */
	public static final int SPRITE_DISTANCE = 34;
	public static final int SPRITE_WIDTH = 32, SPRITE_HEIGHT = 32;
	public static final int SWOOSH_HEIGHT = 50, SWOOSH_WIDTH = 35;
	
	public static final int JON_FRONT = 0, JON_BACK = 0, JON_FRAMES = 4;
	public static final int DROGO_FRONT = 0, DROGO_BACK= 0, DROGO_FRAMES = 3;
	public static final int DANNY_FRONT = 0, DANNY_BACK = 0, DANNY_FRAMES = 3;
	public static final int JOFFREY_FRONT = 0, JOFFREY_BACK = 0, JOFFREY_FRAMES = 2;
	public static final int WUNWUN_FRONT = 0, WUNWUN_BACK = 0, WUNWUN_FRAMES = 4;
	public static final int WALKER_FRONT = 0, WALKER_BACK = 0, WALKER_FRAMES = 4;
	
	public static final int OFFENSE_SPOT = 270, DEFENSE_SPOT = 0;
	public static final int JIGGLE_MAX = 1, JIGGLE_MIN = -1, JIGGLE_TIME = 15; //Don't think this needs to be changed

	/*
	 * Animation Counters
	 */

	private int duckSpriteNum;
	private int spriteNum = 0;
	private int jiggle = 0, jiggleCount = 0, frameCounter = 0,
			dogXValue = 0, dogTickCounter = 0;
	private boolean jiggleMan = true, starting = true;
	private int duckFallX, duckFallY;

	/*
	 * Private Helpers
	 */

	private void loadImages() {
		//Not using a background image currently
//		try {
//			background = ImageIO.read(new File("images" + File.separator
//					+ "DuckHuntBackground.png"));
//		} catch (IOException e) {
//			System.out.println("Could not find 'DuckHuntBackground.PNG'");
//		}
		try {
			sheet = ImageIO.read(new File("images" + File.separator
					+ "GoTSpriteSheet.png"));

		} catch (IOException e) {
			System.out.println("Could not find 'GoTSpriteSheet.png'");
		}
	}

	public BufferedImage getOffenseSprite() {
		//the back image of the offense
		return sheet;
		//return sheet.getSubimage(DOG_LAUGHING_X, DOG_LAUGHING_Y,
		//		DOG_LAUGHING_WIDTH, DOG_LAUGHING_HEIGHT);
	}

	public BufferedImage getDefenseSprite() {
		//the front image of the defense
		return sheet;
		//return sheet.getSubimage(DOG_LAUGHING_X, DOG_LAUGHING_Y,
		//		DOG_LAUGHING_WIDTH, DOG_LAUGHING_HEIGHT);
	}

	private void jiggleOffense() {
		//TODO: Taken directly from duckHunt, needs to be changed? 
		if (jiggleMan) {
			jiggle++;
			if (jiggle > JIGGLE_MAX) {
				jiggleMan = false;
			}
		} else {
			jiggle--;
			if (jiggle < JIGGLE_MIN) {
				jiggleMan = true;
			}
		}
		if (jiggleCount++ > JIGGLE_TIME) {
			// Stop jiggling
			jiggleCount = 0;
			misses = 0;
		}
	}
	
	private void jiggleDefense() {
		//see jiggleOffense
	}
}