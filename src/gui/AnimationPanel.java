package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import controller.TheGame;
import Model.Dice;
import Model.Faction;

/**
 * (Heavily inspired by DuckHuntStart)
 * 
 * @author Jewell Finder, Dylan Tobia, Abigail Dodd, Sydney Komro
 * 
 */
public class AnimationPanel extends JPanel {

	private BufferedImage sheet, background;
	private ArrayList<BufferedImage> redDice = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> whiteDice = new ArrayList<BufferedImage>();
	private ArrayList<Dice> attackDice = new ArrayList<Dice>();
	private ArrayList<Dice> defenseDice = new ArrayList<Dice>();
	// private Cursor cursor;
	private TheGame model;
	private int misses;
	private Faction offense, defense;
	private int wind=0;
	private boolean attack=false;

//	public AnimationPanel(TheGame game) {
//		loadImages();
//		model = game;
//		setSize(500,500);
//		setPreferredSize(new Dimension(500,500));
////		Dice one = new Dice(1); 
////		Dice two = new Dice(2);
////		Dice three = new Dice(3);
////		Dice four = new Dice(4);
////		Dice five = new Dice(5);
////		attackDice.add(one);
////		attackDice.add(two);
////		attackDice.add(three);
////		defenseDice.add(four);
////		defenseDice.add(five);
//		attackDice= model.getAttackDice();
//		defenseDice= model.getDefenseDice();
//		//this.setSize(500, 500);
//		//this.repaint();
//	}
	
	public void setUpEverything(TheGame game)
	{
		loadImages();
		
		model = game;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D gr = (Graphics2D) g;
		super.paintComponent(gr);

		 if (model == null) {
			 System.out.println("model is null");
			 return;
		 }
		 System.out.println("model is not");
		
		// gr.drawImage(background, 0, 0, null);

		if (starting) {
			gr.drawImage(getOffenseSprite(), OffenseXValue, OFFENSE_Y, null);
			gr.drawImage(getDefenseSprite(), DefenseXValue, DEFENSE_Y, null);
			wind++;
			if(wind%5==1)
				spriteStatus++;
			if(frameCounter==12){
				starting=false;
				attack=true;
			}
			frameCounter++;
			
			// Draw the characters
			// roll
			// draw the swoosh attack
			
			

		} 
		else if(attack){
			gr.drawImage(getOffenseSprite(), OffenseXValue, OFFENSE_Y, null);
			gr.drawImage(getDefenseSprite(), DefenseXValue, DEFENSE_Y, null);
			wind++;
			if(wind%5==1)
				spriteStatus++;
			
			gr.drawImage(SWOOSH, swooshX, swooshY, null);
			drawAttack();
		}
		else if (model.getHit() == 0) {
			int i=0;
			for(Dice c : attackDice){
				gr.drawImage(redDice.get(c.getValue()-1),(60*(i+1)),10,50,50, null);
				i++;
			}
			for(Dice c : defenseDice){
				gr.drawImage(whiteDice.get(c.getValue()-1),(60*(i+1)),10,50,50, null);
				i++;
			}
			gr.drawImage(getOffenseSprite(), jiggleOffense(), OFFENSE_Y, null);
			gr.drawImage(getDefenseSprite(), DefenseXValue, DEFENSE_Y, null);
			wind++;
			if(wind%5==1)
				spriteStatus++;
			// if getHit == 0, offense misses entirely
			// wiggle the offense

		} else if (model.getHit() == 1) {
			int i=0;
			for(Dice c : attackDice){
				gr.drawImage(redDice.get(c.getValue()-1),(60*(i+1)),10,50,50, null);
				i++;
			}
			for(Dice c : defenseDice){
				gr.drawImage(whiteDice.get(c.getValue()-1),(60*(i+1)),10,50,50, null);
				i++;
			}
			gr.drawImage(getOffenseSprite(), jiggleOffense(), OFFENSE_Y, null);
			gr.drawImage(getDefenseSprite(), jiggleDefense(), DEFENSE_Y, null);
			wind++;
			if(wind%5==1)
				spriteStatus++;
			// if getHit == 1, winner won one and lost one
			// wiggle both

		} else {
			int i=0;
			for(Dice c : attackDice){
				gr.drawImage(redDice.get(c.getValue()-1),(60*(i+1)),10,50,50, null);
				i++;
			}
			for(Dice c : defenseDice){
				gr.drawImage(whiteDice.get(c.getValue()-1),(60*(i+1)),10,50,50, null);
				i++;
			}
			gr.drawImage(getOffenseSprite(), OffenseXValue, OFFENSE_Y, null);
			gr.drawImage(getDefenseSprite(), jiggleDefense(), DEFENSE_Y, null);
			wind++;
			if(wind%5==1)
				spriteStatus++;
			// critical hit! All offense blows land
			// wiggle defense
		}

	}

	public void updateAnimations() {
		// What do I need to draw? (check state)
		
		if (starting) {
			//spriteInTheWind();
			this.repaint();
		} 
		else if(attack){
			drawAttack();

		} else {

		}
	}

	/*
	 * TODO: Locations of the first front and back of each sprite, offset of
	 * each frame, etc (Numbers have not been changed from DuckHunt, but number
	 * of frames for each character is correct)
	 */
	public static final int SPRITE_DISTANCE = 0;
	public static final int SPRITE_WIDTH = 95, SPRITE_HEIGHT = 104;
	public static final int SWOOSH_HEIGHT = 100, SWOOSH_WIDTH = 116;
	public static final int SWOOSH_X = 142, SWOOSH_Y = 742;
	public BufferedImage SWOOSH;
	public static final int SPRITE_FRONT_X = 130, SPRITE_BACK_X = 520; // Starting
																		// X's
																		// are
																		// the
																		// same
																		// for
																		// all
																		// of
																		// them
	//public static final int SPRITE_Y = getFaction(Faction.STARK); // TODO:
																	// getFaction()
																	// to set
																	// which
																	// sprite
																	// this is
	public static final int JON_Y = 304, JON_FRAMES = 4;
	public static final int DROGO_Y = 411, DROGO_FRAMES = 3;
	public static final int DANNY_Y = 519, DANNY_FRAMES = 3;
	public static final int JOFFREY_Y = 622, JOFFREY_FRAMES = 2;
	public static final int WUNWUN_Y = 107, WUNWUN_FRAMES = 4;
	public static final int WALKER_Y = 213, WALKER_FRAMES = 4;

	public static final int OFFENSE_Y = 350, DEFENSE_Y = 150; // TODO: This
																// needs going
																// to be where
																// they are
																// placed on the
																// GUI
	public static final int JIGGLE_MAX = 1, JIGGLE_MIN = -1, JIGGLE_TIME = 15; // Don't
																				// think
																				// this
																				// needs
																				// to
																				// be
																				// changed

	/*
	 * Animation Counters
	 */

	public int sprite_defense_y;
	public int sprite_offense_y;
	public int sprite_defense_frames;
	public int sprite_offense_frames;
	private int spriteNum = 0;
	private int jiggle = 0, jiggleCount = 0, frameCounter = 0, OffenseXValue = 25, OffenseTickCounter = 0,
			DefenseXValue = 350;
	private boolean jiggleMan = true, starting = true;

	/*
	 * Private Helpers
	 */

	private void loadImages() {

		
			for(int i=0; i<6; i++){
				try {
					redDice.add(ImageIO.read(new File("images" + File.separator + "dice" + File.separator +"RedDice"+(i+1)+".jpg")));
				}catch(IOException e){
					System.out.println("Could not find Dice Images");
				}
			}
			for(int i=0; i<6; i++){
				try{
					whiteDice.add(ImageIO.read(new File("images" + File.separator +"dice" + File.separator +"WhiteDice"+(i+1)+".jpg")));
			
				} catch (IOException e) {
					System.out.println("Could not find Dice Images");
				}
			}
		try {
			sheet = ImageIO.read(new File("images" + File.separator + "GoTSpriteSheet.png"));
			SWOOSH = sheet.getSubimage(SWOOSH_X, SWOOSH_Y, SWOOSH_WIDTH, SWOOSH_HEIGHT);
		} catch (IOException e) {
			System.out.println("Could not find 'GoTSpriteSheet.png'");
		}
	}

	// call this from gui
	public void setOffenseFaction(Faction faction) {
		if (faction == Faction.STARK){
			sprite_offense_y = JON_Y;
			sprite_offense_frames = JON_FRAMES;
		}
		else if (faction == Faction.TARGARYEN){
			sprite_offense_y = DANNY_Y;
			sprite_offense_frames = DANNY_FRAMES;
		}
		else if (faction == Faction.DOTHRAKI){
			sprite_offense_y = DROGO_Y;
			sprite_offense_frames = DROGO_FRAMES;
		}
		else if (faction == Faction.LANNISTER){
			sprite_offense_y = JOFFREY_Y;
			sprite_offense_frames = JOFFREY_FRAMES;
		}
		else if (faction == Faction.WHITEWALKERS){
			sprite_offense_y = WALKER_Y;
			sprite_offense_frames = WALKER_FRAMES;
		}
		else if (faction == Faction.WILDLINGS){
			sprite_offense_y = WUNWUN_Y;
			sprite_offense_frames = WUNWUN_FRAMES;
		}
	}

	// call this from gui
	public void setDefenseFaction(Faction faction) {
		if (faction == Faction.STARK){
			sprite_defense_y = JON_Y;
			sprite_defense_frames = JON_FRAMES;
		}
		else if (faction == Faction.TARGARYEN){
			sprite_defense_y = DANNY_Y;
			sprite_defense_frames = DANNY_FRAMES;
		}
		else if (faction == Faction.DOTHRAKI){
			sprite_defense_y = DROGO_Y;
			sprite_defense_frames = DROGO_FRAMES;
		}
		else if (faction == Faction.LANNISTER){
			sprite_defense_y = JOFFREY_Y;
			sprite_defense_frames = JOFFREY_FRAMES;
		}
		else if (faction == Faction.WHITEWALKERS){
			sprite_defense_y = WALKER_Y;
			sprite_defense_frames = WALKER_FRAMES;
		}
		else if (faction == Faction.WILDLINGS){
			sprite_defense_y = WUNWUN_Y;
			sprite_defense_frames = WUNWUN_FRAMES;
		}
	}

	public BufferedImage getOffenseSprite() {
		// the back image of the offense
		System.out.println("getting offense");
		return sheet.getSubimage(getNextOffense(), sprite_offense_y, SPRITE_WIDTH, SPRITE_HEIGHT);
	}

	public BufferedImage getDefenseSprite() {
		// the front image of the defense
		System.out.println("getting defense");
		return sheet.getSubimage(getNextDefense(), sprite_defense_y, SPRITE_WIDTH, SPRITE_HEIGHT);
	}

	private int jiggleOffense() {
		if(jiggle==0){
			jiggle++;
			return OffenseXValue+5;
		}
		else if(jiggle==1){
			jiggle=-1;
			return OffenseXValue;
		}
		else if (jiggle==-1){
			jiggle++;
			return OffenseXValue-5;
		}
		return OffenseXValue;
	}

	private int jiggleDefense() {
		if(jiggle==0){
			jiggle++;
			return DefenseXValue+5;
		}
		else if(jiggle==1){
			jiggle=-1;
			return DefenseXValue;
		}
		else if (jiggle==-1){
			jiggle++;
			return DefenseXValue-5;
		}
		return DefenseXValue;
	}
	
	private int swooshStatus=0, swooshX=125, swooshY=295, spriteStatus=0;
	
	private void spriteInTheWind(int offenseFrames, int defenseFrames){
		sheet.getSubimage(SPRITE_BACK_X, getNextOffense(), SPRITE_WIDTH, SPRITE_HEIGHT);
	}
	
	private int getNextOffense(){
		if(spriteStatus<sprite_offense_frames){
			return SPRITE_BACK_X + (SPRITE_WIDTH*spriteStatus);
		}
		else{
			spriteStatus=0;
			return SPRITE_BACK_X;
		}
	}
	
	private int getNextDefense(){
		if(spriteStatus<sprite_defense_frames){
			return SPRITE_FRONT_X + (SPRITE_WIDTH*spriteStatus);
		}
		else{
			spriteStatus=0;
			return SPRITE_FRONT_X;
		}
	}
	
	private void drawAttack() {
		if(swooshStatus<24){
			swooshX=swooshX+5;
			swooshY=swooshY-5;
			swooshStatus++;
		}
		else
			attack=false;

	}

	private void jiggleBoth() {
		// TODO Auto-generated method stub

	}

	public void resetStart() {
		starting = true;
		frameCounter = 0;
		wind = 0;
		spriteStatus = 0;
	}
}