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

import Model.Faction;
import Model.Game;
import Model.TheGame;

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
	private TheGame model;
	private int misses;
	private Faction offense, defense; 

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
			gr.drawImage(getOffenseSprite(), OffenseXValue, OFFENSE_Y, null);
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
	public static final int SPRITE_DISTANCE = 0;
	public static final int SPRITE_WIDTH = 95, SPRITE_HEIGHT = 104;
	public static final int SWOOSH_HEIGHT = 100, SWOOSH_WIDTH = 116;
	
	public static final int SPRITE_FRONT_X = 130, SPRITE_BACK_X = 520; //Starting X's are the same for all of them
	public static final int SPRITE_Y = getFaction(Faction.STARK);					    //TODO: getFaction() to set which sprite this is
	public static final int JON_Y = 304, JON_FRAMES = 4;
	public static final int DROGO_Y= 411, DROGO_FRAMES = 3;
	public static final int DANNY_Y = 519, DANNY_FRAMES = 3;
	public static final int JOFFREY_Y = 622, JOFFREY_FRAMES = 2;
	public static final int WUNWUN_Y = 107, WUNWUN_FRAMES = 4;
	public static final int WALKER_Y = 213, WALKER_FRAMES = 4;
	
	public static final int OFFENSE_Y = 270, DEFENSE_Y = 0; //TODO: This needs going to be where they are placed on the GUI
	public static final int JIGGLE_MAX = 1, JIGGLE_MIN = -1, JIGGLE_TIME = 15; //Don't think this needs to be changed

	/*
	 * Animation Counters
	 */

	private int spriteNum = 0;
	private int jiggle = 0, jiggleCount = 0, frameCounter = 0,
			OffenseXValue = 0, OffenseTickCounter = 0, DefenseXValue;
	private boolean jiggleMan = true, starting = true;

	/*
	 * Private Helpers
	 */

	private void loadImages() {
		
		try {
			background = ImageIO.read(new File("images" + File.separator
					+ "GoTMapRisk.jpg"));
		} catch (IOException e) {
			System.out.println("Could not find 'GoTMapRisk.jpg'");
		}
		try {
			sheet = ImageIO.read(new File("images" + File.separator
					+ "GoTSpriteSheet.png"));

		} catch (IOException e) {
			System.out.println("Could not find 'GoTSpriteSheet.png'");
		}
	}

	private static int getFaction(Faction faction) {
		if(faction == Faction.STARK)
			return JON_Y; 
		if(faction == Faction.TARGARYEN)
			return DANNY_Y; 
		if(faction == Faction.DOTHRAKI)
			return DROGO_Y; 
		if(faction == Faction.LANNISTER)
			return JOFFREY_Y;
		if(faction == Faction.WHITEWALKERS)
			return WALKER_Y;
		if(faction == Faction.WILDLINGS)
			return WUNWUN_Y;
		return 0;
	}


	public BufferedImage getOffenseSprite() {
		//the back image of the offense
		return sheet.getSubimage(SPRITE_FRONT_X, JON_Y,
				SPRITE_WIDTH, SPRITE_HEIGHT);
	}

	public BufferedImage getDefenseSprite() {
		//the front image of the defense
		
		return sheet.getSubimage(SPRITE_FRONT_X, JON_Y,
				SPRITE_WIDTH, SPRITE_HEIGHT);
	}

	private void jiggleOffense() {
		//TODO: Taken directly from duckHunt, should function okay?
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
}