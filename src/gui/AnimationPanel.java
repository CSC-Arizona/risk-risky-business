/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		AnimationPanel.java
 * 	Purpose:	Visual panel to display animations for an attack
 */

package gui;

import java.awt.Color;

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

public class AnimationPanel extends JPanel {

	private BufferedImage sheet;
	private BufferedImage swoosh;
	private ArrayList<BufferedImage> redDice = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> whiteDice = new ArrayList<BufferedImage>();
	private ArrayList<Dice> attackDice = new ArrayList<Dice>();
	private ArrayList<Dice> defenseDice = new ArrayList<Dice>();
	private TheGame model;
	private int wind = 0;
	public static final int SPRITE_DISTANCE = 0;
	public static final int SPRITE_WIDTH = 95, SPRITE_HEIGHT = 104;
	public static final int SWOOSH_HEIGHT = 100, SWOOSH_WIDTH = 116;
	public static final int SWOOSH_X = 142, SWOOSH_Y = 742;
	public static final int SPRITE_FRONT_X = 130, SPRITE_BACK_X = 520;
	public static final int JON_Y = 304, JON_FRAMES = 4;
	public static final int DROGO_Y = 411, DROGO_FRAMES = 3;
	public static final int DANNY_Y = 519, DANNY_FRAMES = 3;
	public static final int JOFFREY_Y = 622, JOFFREY_FRAMES = 2;
	public static final int WUNWUN_Y = 107, WUNWUN_FRAMES = 4;
	public static final int WALKER_Y = 213, WALKER_FRAMES = 4;

	public static final int OFFENSE_Y = 350, DEFENSE_Y = 150;
	public static final int JIGGLE_MAX = 1, JIGGLE_MIN = -1, JIGGLE_TIME = 15;
	private int sprite_defense_y, sprite_offense_y, sprite_defense_frames,
			sprite_offense_frames;
	private int jiggle = 0, frameCounter = 0, OffenseXValue = 25,
			DefenseXValue = 350;
	private int swooshStatus = 0, swooshX = 125, swooshY = 295,
			spriteOStatus = 0, spriteDStatus = 0;
	private boolean starting = true, attack = false;

	/*
	 * setUpEverything is in lieu of a constructor. this method loads images to
	 * be used, sets the model, and sets color.
	 */
	public void setUpEverything(TheGame game) {
		loadImages();
		model = game;
		attackDice = model.getAttackDice();
		defenseDice = model.getDefenseDice();
		this.setBackground(Color.GRAY);
		repaint();
	}

	/*
	 * paintComponent Paints animations of all of the possible faction battles,
	 * including showing the difference between the attacker and the defender,
	 * the dice rolled in the turn, and the characters reacting to their success
	 * or defeat
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D gr = (Graphics2D) g;
		super.paintComponent(gr);

		if (model == null) {
			return;
		}

		if (starting) {
			// Images are moving in the "wind"
			gr.drawImage(getOffenseSprite(), OffenseXValue, OFFENSE_Y, null);
			gr.drawImage(getDefenseSprite(), DefenseXValue, DEFENSE_Y, null);
			wind++;
			// only change wind images every 5th call (offset for swoosh)
			if (wind % 5 == 1) {
				spriteOStatus++;
				spriteDStatus++;
			}
			// starting should only last for 12 iterations
			if (frameCounter == 12) {
				starting = false;
				attack = true;
			}
			frameCounter++;

		} else if (attack) {
			// offense attacks defense with "swoosh"
			gr.drawImage(getOffenseSprite(), OffenseXValue, OFFENSE_Y, null);
			gr.drawImage(getDefenseSprite(), DefenseXValue, DEFENSE_Y, null);
			wind++;
			// continue moving in the wind
			if (wind % 5 == 1) {
				spriteOStatus++;
				spriteDStatus++;
			}
			// set up swoosh
			gr.drawImage(swoosh, swooshX, swooshY, null);
			drawAttack();

		} else if (model.getHit() == 0) {
			// set up dice to be shown from the model
			int i = 0;
			for (Dice c : attackDice) {
				gr.drawImage(redDice.get(c.getValue() - 1), OffenseXValue + 100
						+ ((i + 1) * 50) + 10, OFFENSE_Y, 50, 50, null);
				i++;
			}
			for (Dice c : defenseDice) {
				gr.drawImage(whiteDice.get(c.getValue() - 1), DefenseXValue
						- ((i + 1) * 50) - 10, DEFENSE_Y, 50, 50, null);
				i++;
			}
			// continue moving in the wind while calling jiggleOffense
			gr.drawImage(getOffenseSprite(), jiggleOffense(), OFFENSE_Y, null);
			gr.drawImage(getDefenseSprite(), DefenseXValue, DEFENSE_Y, null);
			wind++;
			if (wind % 5 == 1) {
				spriteOStatus++;
				spriteDStatus++;
			}

		} else if (model.getHit() == 1) {
			// set up dice to be shown from the model
			int i = 0;
			for (Dice c : attackDice) {
				gr.drawImage(redDice.get(c.getValue() - 1), OffenseXValue + 100
						+ ((i + 1) * 50) + 10, OFFENSE_Y, 50, 50, null);
				i++;
			}
			for (Dice c : defenseDice) {
				gr.drawImage(whiteDice.get(c.getValue() - 1), DefenseXValue
						- ((i + 1) * 50) - 10, DEFENSE_Y, 50, 50, null);
				i++;
			}
			// continue moving in the wind while calling jiggleOffense and
			// jiggleDefense
			gr.drawImage(getOffenseSprite(), jiggleOffense(), OFFENSE_Y, null);
			gr.drawImage(getDefenseSprite(), jiggleDefense(), DEFENSE_Y, null);
			wind++;
			if (wind % 5 == 1) {
				spriteOStatus++;
				spriteDStatus++;
			}

		} else {
			// set up dice to be shown from the model
			int i = 0;
			for (Dice c : attackDice) {
				gr.drawImage(redDice.get(c.getValue() - 1), OffenseXValue + 100
						+ ((i + 1) * 50) + 10, OFFENSE_Y, 50, 50, null);
				i++;
			}
			for (Dice c : defenseDice) {
				gr.drawImage(whiteDice.get(c.getValue() - 1), DefenseXValue
						- ((i + 1) * 50) - 10, DEFENSE_Y, 50, 50, null);
				i++;
			}
			// continue moving in the wind while calling jiggleDefense
			gr.drawImage(getOffenseSprite(), OffenseXValue, OFFENSE_Y, null);
			gr.drawImage(getDefenseSprite(), jiggleDefense(), DEFENSE_Y, null);
			wind++;
			if (wind % 5 == 1) {
				spriteOStatus++;
				spriteDStatus++;
			}
		}

	}

	/*
	 * updateAnimations(): checks the state of the animations and updates.
	 */
	public void updateAnimations() {
		// check state

		if (starting) {
			this.repaint();
		} else if (attack) {
			drawAttack();
		} else {

		}
	}

	/*
	 * setOffenseFaction: call this from gui, sets the faction to set the
	 * correct offense sprite location and number of frames
	 */
	public void setOffenseFaction(Faction faction) {
		if (faction == Faction.STARK) {
			sprite_offense_y = JON_Y;
			sprite_offense_frames = JON_FRAMES;
		} else if (faction == Faction.TARGARYEN) {
			sprite_offense_y = DANNY_Y;
			sprite_offense_frames = DANNY_FRAMES;
		} else if (faction == Faction.DOTHRAKI) {
			sprite_offense_y = DROGO_Y;
			sprite_offense_frames = DROGO_FRAMES;
		} else if (faction == Faction.LANNISTER) {
			sprite_offense_y = JOFFREY_Y;
			sprite_offense_frames = JOFFREY_FRAMES;
		} else if (faction == Faction.WHITEWALKERS) {
			sprite_offense_y = WALKER_Y;
			sprite_offense_frames = WALKER_FRAMES;
		} else if (faction == Faction.WILDLINGS) {
			sprite_offense_y = WUNWUN_Y;
			sprite_offense_frames = WUNWUN_FRAMES;
		}
	}

	/*
	 * setDefenseFaction: call this from gui, sets the faction to set the
	 * correct defense sprite location and number of frames
	 */
	public void setDefenseFaction(Faction faction) {
		if (faction == Faction.STARK) {
			sprite_defense_y = JON_Y;
			sprite_defense_frames = JON_FRAMES;
		} else if (faction == Faction.TARGARYEN) {
			sprite_defense_y = DANNY_Y;
			sprite_defense_frames = DANNY_FRAMES;
		} else if (faction == Faction.DOTHRAKI) {
			sprite_defense_y = DROGO_Y;
			sprite_defense_frames = DROGO_FRAMES;
		} else if (faction == Faction.LANNISTER) {
			sprite_defense_y = JOFFREY_Y;
			sprite_defense_frames = JOFFREY_FRAMES;
		} else if (faction == Faction.WHITEWALKERS) {
			sprite_defense_y = WALKER_Y;
			sprite_defense_frames = WALKER_FRAMES;
		} else if (faction == Faction.WILDLINGS) {
			sprite_defense_y = WUNWUN_Y;
			sprite_defense_frames = WUNWUN_FRAMES;
		}
	}

	/*
	 * Private Helpers
	 */

	/*
	 * loadImages(): loads all images needed for the animation (all dice and the
	 * spriteSheet)
	 */
	private void loadImages() {

		for (int i = 0; i < 6; i++) {
			try {
				redDice.add(ImageIO.read(new File("images" + File.separator
						+ "dice" + File.separator + "RedDice" + (i + 1)
						+ ".jpg")));
			} catch (IOException e) {
				System.out.println("Could not find Dice Images");
			}
		}
		for (int i = 0; i < 6; i++) {
			try {
				whiteDice.add(ImageIO.read(new File("images" + File.separator
						+ "dice" + File.separator + "WhiteDice" + (i + 1)
						+ ".jpg")));

			} catch (IOException e) {
				System.out.println("Could not find Dice Images");
			}
		}
		try {
			sheet = ImageIO.read(new File("images" + File.separator
					+ "GoTSpriteSheet.png"));
			swoosh = sheet.getSubimage(SWOOSH_X, SWOOSH_Y, SWOOSH_WIDTH,
					SWOOSH_HEIGHT);
		} catch (IOException e) {
			System.out.println("Could not find 'GoTSpriteSheet.png'");
		}
	}

	// returns subimage of sprite wanted
	private BufferedImage getOffenseSprite() {
		// the back image of the offense
		return sheet.getSubimage(getNextOffense(), sprite_offense_y,
				SPRITE_WIDTH, SPRITE_HEIGHT);
	}

	// returns subimage of sprite wanted
	private BufferedImage getDefenseSprite() {
		// the front image of the defense
		return sheet.getSubimage(getNextDefense(), sprite_defense_y,
				SPRITE_WIDTH, SPRITE_HEIGHT);
	}

	/*
	 * jiggleOffense(): changes OffenseXValue based on a jiggle int flag
	 */
	private int jiggleOffense() {
		if (jiggle == 0) {
			jiggle++;
			return OffenseXValue + 5;
		} else if (jiggle == 1) {
			jiggle = -1;
			return OffenseXValue;
		} else if (jiggle == -1) {
			jiggle++;
			return OffenseXValue - 5;
		}
		return OffenseXValue;
	}

	/*
	 * jiggleDefense(): changes DefenseXValue based on a jiggle int flag
	 */
	private int jiggleDefense() {
		if (jiggle == 0) {
			jiggle++;
			return DefenseXValue + 5;
		} else if (jiggle == 1) {
			jiggle = -1;
			return DefenseXValue;
		} else if (jiggle == -1) {
			jiggle++;
			return DefenseXValue - 5;
		}
		return DefenseXValue;
	}

	/*
	 * getNextOffense(): return x value of the area of the sprite sheet wanted
	 * based on wind
	 */
	private int getNextOffense() {
		if (spriteOStatus < sprite_offense_frames) {
			return SPRITE_BACK_X + (SPRITE_WIDTH * spriteOStatus);
		} else {
			spriteOStatus = 0;
			return SPRITE_BACK_X;
		}
	}

	/*
	 * getNextDefense(): return x value of the area of the sprite sheet wanted
	 * based on wind
	 */
	private int getNextDefense() {
		if (spriteDStatus < sprite_defense_frames) {
			return SPRITE_FRONT_X + (SPRITE_WIDTH * spriteDStatus);
		} else {
			spriteDStatus = 0;
			return SPRITE_FRONT_X;
		}
	}

	/*
	 * drawAttack(): update x and y values for where swoosh will be painted
	 */
	private void drawAttack() {
		if (swooshStatus < 24) {
			swooshX = swooshX + 5;
			swooshY = swooshY - 5;
			swooshStatus++;
		} else
			attack = false;

	}

	/*
	 * resetStart(): sets animations up to be run from starting
	 */
	public void resetStart() {
		attackDice = model.getAttackDice();
		defenseDice = model.getDefenseDice();
		starting = true;
		frameCounter = 0;
		wind = 0;
		spriteOStatus = 0;
		spriteDStatus = 0;
		swooshStatus = 0;
		swooshX = 125;
		swooshY = 295;
	}
}