/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		riskGUI.java
 * 	Purpose:	GUI for visual implementation of RISK
 */

/*
 * To-Dos for Iteration 3
 * 
 * Work on our wow factor - Jewell
 * Make card images - Jewell
 * Polish the GUI/Make sure everything is displayed - Abigail
 * Set up flags for state changes during game - Abigail
 * Adjust reinforcement phase adjustment - Abigail
 * Decide order of players based on dice roll - Abigail
 * Implement rules for army placement - Sydney
 * Write some sort of attack (can be hard coded for now) - Sydney
 * Change AIs during gameplay - Dylan
 * Write AI strategies - Dylan
 *
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.AncestorListener;

import Model.AI;
import Model.AIStrat;
import Model.Card;
import Model.Country;
import Model.Faction;
import Model.Game;
import Model.Map;
import songplayer.SoundClipPlayer;
import Model.Player;

//just a simple GUI to start, with a drawingPanel for map stuff
public class riskGUI extends JFrame {

	public static void main(String[] args) throws UnknownHostException, IOException {
		new riskGUI().setVisible(true);
	}

	private static BoardPanel drawingPanel;
	private JMenuBar menu;
	private int width = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	private int height = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	private int xWidth = 0;
	private int yHeight = 0;
	// private Map map; dont think this is needed anymore cause it is stored
	// within theGame
	private Game theGame;
	private ImageIcon gameBoard, stark, targaryen, lannister, whiteWalkers, dothraki, wildlings;
	private JButton checkButton;
	private CountryPanel currCountryPanel;
	private JButton moveButton;
	private boolean splash, gameOver = false;
	private ImageIcon splashScreen;
	private JPanel splashInfo;
	// my new favorite font...
	private Font goudyFontBig = new Font("Goudy Old Style", Font.BOLD, 40);
	private Font gotFontHeader;
	private Font gotFontBody;

	private String gameType;
	private Player nextPlayer, currPlayer;
	private int humans;
	private int ai = -1;
	private int numOfUnitsToMove = 0;
	private ArrayList<String> houses;
	private ArrayList<String> playerNames;
	private ArrayList<String> possHouses;
	private boolean decisionMakingPhase = false, moveUnitsFlag = false;
	private Country moveUnitsFromCountry;
	private ArrayList<AIStrat> strat = new ArrayList<AIStrat>();
	private boolean attackFromFlag = false, attackFlag = false;
	private Country attackFrom, attack;
	private int numOfArmies = 0;
	private boolean musicOn = true;
	private int playerNumOfCountries = 0;

	private SoundClipPlayer player = new SoundClipPlayer();

	public riskGUI() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			gotFontHeader = Font.createFont(Font.TRUETYPE_FONT, new File("TrajanusBricks.ttf"));
			gotFontHeader = gotFontHeader.deriveFont(36f);
			gotFontBody = Font.createFont(Font.TRUETYPE_FONT, new File("LibreBaskerville-Regular.otf"));
			gotFontBody = gotFontBody.deriveFont(24f);
		} catch (FontFormatException e) {
			System.out.println("What'd you do???");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("What'd you do???");
			e.printStackTrace();
		}

		ge.registerFont(gotFontHeader);

		ge.registerFont(gotFontBody);

		System.out.println("Width = " + width + " Height = " + height);
		splash = true; // comment me out for default mode
		// splash = false; // comment me out for splash screens
		setUpImages();
		setUpGui();
		setUpMenu();
		setUpHouseArray();
		setUpSplash(); // comment me out for default mode
		// defaultMode(); // comment me out for splash screens

	}// end riskGui constructor

	private void setUpAIMenu() {
		int i = 1;
		JMenu AIDiff = new JMenu("AI Difficulty");
		for (Player ai : theGame.getPlayers()) {
			if (ai instanceof AI) {
				((AI) ai).makeMenuItem(i, new AIDiffChangeListener());
				AIDiff.add(((AI) ai).getMenuItem());
				i++;
			}
		}
		menu.add(AIDiff);
	}// end setUpAiMenu

	private void defaultMode() {
		humans = 1;
		ai = 5;
		drawingPanel = new BoardPanel();
		// splashLoading2();
		theGame = Game.getInstance(1, 5);
		setUpDrawingPanel();
		setUpMenu();
		// setUpClearButton();
		// setUpPassButton();
		setUpAIMenu();
		ArrayList<Player> players = theGame.getPlayers();
		players.get(0).setFaction("Stark");
		players.get(1).setFaction("Dothraki");
		players.get(2).setFaction("White Walkers");
		players.get(3).setFaction("Lannister");
		players.get(4).setFaction("Targaryen");
		players.get(5).setFaction("Wildlings");
		players.get(0).setName("Player1");
		((AI) players.get(1)).setMyStrat(AIStrat.EASY);
		((AI) players.get(2)).setMyStrat(AIStrat.EASY);
		((AI) players.get(3)).setMyStrat(AIStrat.EASY);
		((AI) players.get(4)).setMyStrat(AIStrat.EASY);
		((AI) players.get(5)).setMyStrat(AIStrat.EASY);

		// Updating the arraylist in the game
		theGame.setPlayers(players);
		// Starting the game...
		theGame.startGame();
		// player.startPlay();
		drawingPanel.repaint();
	}// end defualtMode

	private void setUpHouseArray() {
		possHouses = new ArrayList<String>();
		possHouses.add("Stark");
		possHouses.add("Lannister");
		possHouses.add("Targaryen");
		possHouses.add("White Walkers");
		possHouses.add("Dothraki");
		possHouses.add("Wildlings");

	}// end setUpHouseArray

	private void setUpSplash() {
		// Still messing around with this.
		splashScreen = new ImageIcon("images/SplashScreen.jpg");
		drawingPanel = new BoardPanel();
		drawingPanel.setLayout(null);
		drawingPanel.setSize(width - 40, height - 70);
		drawingPanel.setLocation(10, 10);
		drawingPanel.setBackground(Color.LIGHT_GRAY);
		this.add(drawingPanel);
		this.setVisible(true);
		drawingPanel.repaint();
		player.startTheme();
		splashLoading1();
	}// end setUpSplash

	private void splashLoading2() {

		System.out.println("Brace Yourselves, RISK is Coming...");
		splash = false;
		drawingPanel.removeAll();
		this.remove(drawingPanel);
		// creates or grabs an instance of the game, first variable is number of
		// human players, second is total number of players
		theGame = Game.getInstance(humans, ai);
		setUpDrawingPanel();
		setUpMenu();
		// setUpClearButton();
		// setUpPassButton();
		setUpAIMenu();
		ArrayList<Player> players = theGame.getPlayers();
		for (int i = 0; i < humans; i++) {
			players.get(i).setFaction(houses.get(i));
			players.get(i).setName(playerNames.get(i));
		}
		for (String h : houses) {
			possHouses.remove(h);
		}
		int i = 0;
		for (int j = humans; j < (humans + ai); j++) {
			players.get(j).setFaction(possHouses.get(i));
			((AI) players.get(j)).setMyStrat(strat.get(i));
			i++;
		}

		theGame.setPlayers(players); // player.startTheme();
		theGame.startGame();

		// player.stopTheme();
		// player.startTheme();
		// musicOn=true;
	}// end splashLoading2

	private void splashNames() {

		System.out.println("What are the players names?");
		playerNames = new ArrayList<String>();
		for (int i = 0; i < humans; i++) {
			boolean nameFlag = false;

			while (!nameFlag) {
				String name = JOptionPane.showInputDialog("What will be Player " + (i + 1) + "'s Name?");
				if (name == null) {
					JOptionPane.showMessageDialog(null, "Must select a name.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {

					playerNames.add(name);
					nameFlag = true;
				}
			}
		}
		splashLoading2();
	}// end splash names

	private void splashHouses() {
		drawingPanel.remove(splashInfo);

		houses = new ArrayList<String>();
		for (int i = 0; i < humans; i++) {
			Boolean illegalName = true;
			String house = "";
			while (illegalName == true) {
				Boolean check = true;
				house = (String) JOptionPane.showInputDialog(null, "Please choose Player " + (i + 1) + "'s House",
						"Choose a House", JOptionPane.QUESTION_MESSAGE, null,
						new Object[] { "Stark", "Targaryen", "Lannister", "White Walkers", "Wildlings", "Dothraki" },
						"No");
				if (house != null) {
					for (int j = 0; j < houses.size(); j++) {
						if (houses.get(j).compareTo(house) == 0) {
							check = false;
							JOptionPane.showMessageDialog(null, "House has already been chosen. Please pick another.",
									"Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}

				if (house == null && check) {
					JOptionPane.showMessageDialog(null, "Must choose a House.", "Error", JOptionPane.ERROR_MESSAGE);
					check = false;
				} else if (house != null && check)
					illegalName = false;

			}
			houses.add(house);
		}
		for (int i = 0; i < ai; i++) {
			Boolean illegalName = true;
			String ais = "";

			while (illegalName == true) {
				ais = (String) JOptionPane.showInputDialog(null, "Please choose AI " + (i + 1) + "'s Strategy",
						"Choose a Strategy", JOptionPane.QUESTION_MESSAGE, null, new Object[] { "Easy", "Hard" },
						"Easy");
				if (ais == null) {
					JOptionPane.showMessageDialog(null, "Must choose a Strategy.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					illegalName = false;
					switch (ais) {
					case "Easy":
						strat.add(AIStrat.EASY);
						break;
					case "Hard":
						strat.add(AIStrat.HARD);
						break;
					default:
						break;
					}
				}
			}
		}
		splashNames();
	}// end splashHouses

	private void splashNumPlayers() {
		drawingPanel.remove(splashInfo);

		System.out.println("How many players?");
		String human = "", ais = "";
		boolean continueFlag = false, setFlag = false, aiFlag = false;

		while (!continueFlag) {
			human = JOptionPane.showInputDialog("How Many Human Players?");
			try {
				humans = Integer.parseInt(human);
				continueFlag = true;

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Must choose a valid number between 0 and 6.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		while (!setFlag) {
			ais = JOptionPane.showInputDialog("How Many AI Players?");
			try {
				ai = Integer.parseInt(ais);
				aiFlag = true;

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Must choose a valid number between 0 and 6.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			if (aiFlag) {
				if ((ai + humans) > 6 || ai + humans < 3) {
					JOptionPane.showMessageDialog(null, "Invalid. Total number of players must be between 3 and 6.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else
					setFlag = true;

			}
		}
		splashHouses();
	}// end splahNumPlayers

	private void splashChooseGame() {
		drawingPanel.remove(splashInfo);
		System.out.println("New Game or Load Game?");
		splashInfo = new JPanel();
		splashInfo.setLayout(null);
		splashInfo.setSize(700, 400);
		splashInfo.setLocation(width / 2 - 350, height / 2 - 200);
		JLabel load = new JLabel("New Game or Load Game?");
		load.setFont(gotFontHeader.deriveFont(28f));
		// load.setHorizontalAlignment(JLabel.CENTER);
		load.setLocation(100, 5);
		load.setSize(600, 150);
		JButton newG = new JButton("New Game!");
		newG.setFont(gotFontHeader.deriveFont(24f));
		newG.setLocation(50, 200);
		newG.addActionListener(new GameTypeListener());
		newG.setSize(300, 100);
		JButton loadG = new JButton("Load Game!");
		loadG.setFont(gotFontHeader.deriveFont(24f));
		loadG.setLocation(375, 200);
		loadG.addActionListener(new GameTypeListener());
		loadG.setSize(300, 100);
		splashInfo.add(newG);
		splashInfo.add(loadG);
		splashInfo.add(load);
		drawingPanel.add(splashInfo);
		drawingPanel.repaint();
	}// end splashChooseGame

	/*
	 * SplashLoading1 is the first loading page. Sets up the background image
	 * and JPanel for information. Starts Theme Song, which will play until it
	 * ends. This screen is shown for 10 seconds.
	 */
	private void splashLoading1() {
		splashInfo = new JPanel();
		splashInfo.setLayout(null);
		splashInfo.setSize(500, 150);
		splashInfo.setLocation(width / 2 - 250, height / 2 - 75);
		JLabel load = new JLabel("LOADING...");
		load.setFont(gotFontHeader);
		load.setLocation(150, 5);
		load.setSize(300, 150);
		splashInfo.add(load);
		drawingPanel.add(splashInfo);
		drawingPanel.repaint();

		// play the song! Commented out for now in order to test without losing
		// my mind
		// SongPlayer.playFile("Game_Of_Thrones_Official_Show_Open_HBO_.wav");

		// player.startTheme();

		// pause on this screen for 10 seconds. Set to 5 seconds during testing.
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			System.out.println("nahhh");
		}
		// move on to splash screen #2, choosing game play
		splashChooseGame();
	}// end splashLoading1

	private void setUpGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());
		setTitle("GoT Risk");
		setSize(width, height);
		this.setVisible(true);
	}// end setUpGui

	private void setUpMenu() {
		JMenu file = new JMenu("File");
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.addActionListener(new NewGameListener());
		file.add(newGame);
		JMenu settings = new JMenu("Settings");
		String music = "";
		if (musicOn)
			music = "Mute Music";
		else
			music = "Unmute Music";
		JMenuItem musicStatus = new JMenuItem(music);
		musicStatus.addActionListener(new musicListener());
		settings.add(musicStatus);
		JMenu help = new JMenu("Help");
		menu = new JMenuBar();
		menu.add(file);
		JMenuItem about = new JMenuItem("About");
		menu.add(help);
		if (!splash)
			menu.add(settings);

		JMenuItem rules = new JMenuItem("Rules");
		help.add(rules);
		help.add(about);
		this.setJMenuBar(menu);

		rules.setActionCommand("rules");
		about.setActionCommand("about");
		rules.addActionListener(new HelpListener());
		about.addActionListener(new HelpListener());

	}// end setUpMenu

	private void setUpClearButton() {

		JButton clearButton = new JButton("Clear Move Selections");
		clearButton.addActionListener(new clearButtonListener());
		clearButton.setSize(4 * xWidth, 2 * yHeight);
		clearButton.setLocation(width - (int) (4.25 * xWidth), (int) (0.25 * yHeight));
		drawingPanel.add(clearButton);
	}

	private void setUpPassButton() {
		JButton passButton = new JButton("Finished Attacking");
		passButton.addActionListener(new PassButtonListener());
		passButton.setSize(4 * xWidth, 2 * yHeight);
		passButton.setLocation(width - (int) (4.25 * xWidth), (int) (2.75 * yHeight));
		drawingPanel.add(passButton);
	}

	private void setUpDrawingPanel() {
		// if(drawingPanel==null)

		// System.out.println(gameBoard.toString());
		drawingPanel = new BoardPanel();
		drawingPanel.setLayout(null);
		drawingPanel.setSize(width - 40, height - 70);
		drawingPanel.setLocation(10, 10);
		drawingPanel.setBackground(Color.LIGHT_GRAY);
		drawingPanel.repaint();

		// Prepare to draw the buttons!
		Dimension drawD = drawingPanel.getSize();
		xWidth = (int) (drawD.getWidth() / 40);
		yHeight = (int) (drawD.getHeight() / 40);
		drawCountryButtons();

		// Draw country panel
		currCountryPanel = new CountryPanel();
		drawingPanel.add(currCountryPanel);
		this.add(drawingPanel, BorderLayout.CENTER);
		drawingPanel.repaint();

		player.stopTheme();
		player.startPlay();

	}// end setUpDrawingPanel

	private void setUpImages() {

		gameBoard = new ImageIcon("images/GoTMapRisk.jpg");
		stark = new ImageIcon("images/stark.jpg");
		targaryen = new ImageIcon("images/targaryen.jpg");
		lannister = new ImageIcon("images/lannister.jpg");
		whiteWalkers = new ImageIcon("images/whiteWalkers.jpg");
		dothraki = new ImageIcon("images/dothraki.jpg");
		wildlings = new ImageIcon("images/wildlings.jpg");
	}// end setUpImages

	// draws buttons over the name of all of the countries
	private void drawCountryButtons() {
		for (Country country : theGame.getGameMap().getCountries()) {
			// The Make button method has the same logic that was previously
			// here
			country.makeButton(xWidth, yHeight, new CountryClickListener());
			drawingPanel.add(country.getButton());
		} // end for

		// Manually adjusts the size and shape of a few of the weirder
		// shaped country buttons

	}// end drawCountryButtons

	// Updates those buttons if the size of the panel changes

	private void updateCountryButtons() {
		for (Country country : theGame.getGameMap().getCountries()) {
			country.updateButton(xWidth, yHeight);
		}
	}// end updateCountryButtons

	private class BoardPanel extends JPanel implements Observer {
		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.white);
			super.paintComponent(g2);

			Image tmp;
			if (splash)
				tmp = splashScreen.getImage();
			else
				tmp = gameBoard.getImage();
			g2.drawImage(tmp, 0, 0, drawingPanel.getWidth(), drawingPanel.getHeight(), null);

			Dimension drawD = drawingPanel.getSize();
			xWidth = (int) (drawD.getWidth() / 40);
			yHeight = (int) (drawD.getHeight() / 40);

			if (!gameOver) {
				if (!splash) {
					updateCountryButtons();
					currCountryPanel.updatePanel(g);
				}

				drawFactions(g2);
				drawUnits(g2);
				if (theGame != null)
					drawCurrentPlayer(g2);
				// drawGridAndNumbers(g2);
			} else {
				g2.setFont(gotFontBody.deriveFont(30f));
				g2.drawString(theGame.getCurrentPlayer().getName() + " has achieved total victory.",
						drawingPanel.getWidth() / 2, drawingPanel.getHeight() / 2);
				for (Country country : theGame.getGameMap().getCountries()) {
					country.getButton().setEnabled(false);
				}

			}

		}// end paintComponenet

		private void drawUnits(Graphics2D g2) {

			g2.setColor(Color.BLACK);
			g2.setFont(gotFontBody.deriveFont(Font.BOLD, 35f));
			for (Country country : theGame.getGameMap().getCountries()) {
				if (country.getForcesVal() > 0) {
					g2.drawString("" + country.getForcesVal(), ((int) country.getX() * xWidth) + 22,
							((int) country.getY() * yHeight) + 17);
				}

			}

		}// end drawUnits

		private void drawCurrentPlayer(Graphics2D g2) {
			Player currentPlayer = theGame.getCurrentPlayer();
			if (currentPlayer != null) {
				Faction playersFact = theGame.getCurrentPlayer().getFaction();
				switch (playersFact) {
				case STARK:
					g2.drawImage(stark.getImage(), 0, 0, 100, 100, null);
					break;
				case TARGARYEN:
					g2.drawImage(targaryen.getImage(), 0, 0, 100, 100, null);
					break;
				case LANNISTER:
					g2.drawImage(lannister.getImage(), 0, 0, 100, 100, null);
					break;
				case DOTHRAKI:
					g2.drawImage(dothraki.getImage(), 0, 0, 100, 100, null);
					break;
				case WHITEWALKERS:
					g2.drawImage(whiteWalkers.getImage(), 0, 0, 100, 100, null);
					break;
				case WILDLINGS:
					g2.drawImage(wildlings.getImage(), 0, 0, 100, 100, null);
					break;
				default:
					break;
				}
			}
			g2.setColor(Color.WHITE);
			g2.setFont(gotFontBody.deriveFont(20f));
			g2.drawString("Current Player: " + currentPlayer.getName(), 110, 25);
			g2.drawString("Current Phase: " + theGame.getPhase(), 110, 45);
			if (!theGame.isPlacePhase() && theGame.isReinforcePhase())
				g2.drawString("You have: " + theGame.getCurrentPlayer().getAvailableTroops() + " units left to place.",
						110, 65);
			if (theGame.isDeployPhase())
				g2.drawString("You have: " + theGame.getCurrentPlayer().getAvailableTroops() + " units to place.", 110,
						65);

			// TODO display amount of troops "Picked up" when moving troops
			// around at end of turn
		}// end drawCurrentPlayer

		// draws factions if a country is occupied
		private void drawFactions(Graphics2D g2) {
			Map temp = Map.getInstance();
			Country[] allCountries = temp.getCountries();
			for (Country country : allCountries) {
				if (country.getOccupier() != null) {
					Faction ownerFaction = country.returnMyOwnersFaction();
					switch (ownerFaction) {
					case STARK:
						g2.drawImage(stark.getImage(), ((int) country.getX() * xWidth) + 50,
								((int) country.getY() * yHeight) + 5, 30, 30, null);
						break;
					case TARGARYEN:
						g2.drawImage(targaryen.getImage(), ((int) country.getX() * xWidth) + 50,
								((int) country.getY() * yHeight) + 5, 30, 30, null);
						break;
					case LANNISTER:
						g2.drawImage(lannister.getImage(), ((int) country.getX() * xWidth) + 50,
								((int) country.getY() * yHeight) + 5, 30, 30, null);
						break;
					case DOTHRAKI:
						g2.drawImage(dothraki.getImage(), ((int) country.getX() * xWidth) + 50,
								((int) country.getY() * yHeight) + 5, 30, 30, null);
						break;
					case WHITEWALKERS:
						g2.drawImage(whiteWalkers.getImage(), ((int) country.getX() * xWidth) + 50,
								((int) country.getY() * yHeight) + 5, 30, 30, null);
						break;
					case WILDLINGS:
						g2.drawImage(wildlings.getImage(), ((int) country.getX() * xWidth) + 50,
								((int) country.getY() * yHeight) + 5, 30, 30, null);

					}

				}
			}

		}// end drawFaction
			// draws a 40X40 grid over the risk map. Used for determining where
			// to
			// place buttons.

		// draws a 40X40 grid over the risk map. Used for determining where to
		// place buttons.

		private void drawGridAndNumbers(Graphics2D g2) {
			for (int i = xWidth; i < width - 40; i += xWidth) {
				g2.drawLine(i, 0, i, height - 70);
			}

			for (int i = yHeight; i < height - 70; i += yHeight) {
				g2.drawLine(0, i, width - 40, i);
			}

			int xCount = xWidth / 2;
			int yCount = yHeight / 2;

			int startX = xCount;
			int startY = yCount;
			int y = 0;
			// int x = 0;

			for (int i = 1; i < 40; i++) {
				int x = 1;
				y++;
				startY = yCount;

				for (int j = 1; j < 40; j++) {
					g2.drawString(Integer.toString(x), startX, startY);
					startY += yHeight;

					x++;
				}
				startX += xWidth;
			}
		}// end drawGridAndNumbers

		// update for drawing factions over occupied functions
		@Override
		public void update(Observable arg0, Object arg1) {
			repaint();
		}// end update

	}// end boardPanel

	public static BoardPanel getBoardPanel() {
		return drawingPanel;
	}// end getBoardPanel

	private class CountryPanel extends JPanel {
		// private JPanel centerPanel;
		// private JButton makeAMoveButton;
		private boolean isFirstAttackPhase = true;
		private Country curr;
		// Here while I play with various borders!
		Border blueline, raisedetched, loweredetched, raisedbevel, loweredbevel, empty, raisedWithColor;

		/*
		 * public void PaintComponent(Graphics g){ update(g);
		 * super.paintComponent(g);
		 * 
		 * }//end
		 */

		public CountryPanel() {
			// Creating border types to make the GUI prettier
			blueline = BorderFactory.createLineBorder(Color.BLUE);
			raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
			loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			raisedbevel = BorderFactory.createRaisedBevelBorder();
			loweredbevel = BorderFactory.createLoweredBevelBorder();
			empty = BorderFactory.createEmptyBorder();
			raisedWithColor = BorderFactory.createCompoundBorder(raisedetched, blueline);

			this.setLayout(new BorderLayout());
			this.setBorder(loweredetched);
			curr = theGame.getSelectedCountry();
			updatePanel();

		}// end constructor

		// Displays country's name and owner
		public void makeTopLabel() {
			JPanel top = new JPanel();
			top.setLayout(new BorderLayout());
			JLabel country = new JLabel(curr.getName());
			country.setFont(gotFontHeader);
			country.setHorizontalAlignment(JLabel.CENTER);
			top.add(country, BorderLayout.CENTER);
			JLabel owner = new JLabel();
			owner.setFont(gotFontHeader.deriveFont(28f));
			owner.setHorizontalAlignment(JLabel.CENTER);
			if (curr.getOccupier() != null)
				owner.setText(curr.getOccupier().getName() + " " + curr.getOccupier().getFaction().getName());
			else
				owner.setText("None");
			top.add(owner, BorderLayout.SOUTH);

			// Eventually will be deleted!
			top.setBorder(raisedWithColor);

			this.add(top, BorderLayout.NORTH);
			top.revalidate();
		}// end makeTopLabel

		// will display a list of neighbors and the strength of the country's
		// armies
		public void makePlayingCenterPanel() {
			JPanel center = new JPanel();
			center.setLayout(new GridLayout(0, 2));
			JPanel neighbors = new JPanel();
			// Make neighbors panel
			ArrayList<Country> neighs = curr.getNeighbors();
			neighbors.setLayout(new GridLayout(neighs.size(), 0));

			// add all of the neighbors to that panel
			for (int i = 0; i < neighs.size(); i++) {
				JLabel lab = new JLabel();
				// lab.setForeground(Color.white);
				lab.setFont(gotFontBody);
				lab.setText(neighs.get(i).getName());
				lab.setHorizontalAlignment(JLabel.CENTER);
				neighbors.add(lab);
			} // end addneighbors loop
			neighbors.setBorder(raisedWithColor);
			center.add(neighbors);

			// Now, add the strength
			JLabel streng = new JLabel();
			streng.setHorizontalAlignment(JLabel.CENTER);
			streng.setFont(gotFontHeader.deriveFont(48f));
			streng.setText("" + curr.getForcesVal());
			streng.setBorder(raisedWithColor);
			center.add(streng);

			this.add(center, BorderLayout.CENTER);
			center.revalidate();
		}// end makeCenterPanel

		// displays the cards and has a button for trading in cards
		public void makePlayingCardPanel(Graphics g) {
			ArrayList<Card> myCards = theGame.getCurrentPlayer().getCards();

			JPanel cards = new JPanel();
			cards.setLayout(new GridLayout(2, 0));
			JPanel showCards = new JPanel();
			showCards.setLayout(new GridLayout(3, 2));
			ArrayList<Card> currCards = theGame.getCurrentPlayer().getCards();

			// Get the image for this card
			for (int i = 0; i < currCards.size(); i++) {
				JPanel oneCard = new JPanel();
				Image im = currCards.get(i).getMyImage();
				g.drawImage(im, 0, 0, null);
				showCards.add(oneCard);
			} // end for

			cards.add(showCards);
			JButton trade = new JButton("Trade in Cards");
			trade.addActionListener(new TradeClickListener());
			cards.add(trade);
			cards.setBorder(raisedWithColor);
			this.add(cards, BorderLayout.EAST);
			cards.revalidate();
		}// end makeCardPanel

		public void makePlayingMyCountryBottomLabel() {
			/*
			 * JPanel bott = new JPanel(); bott.setLayout(new GridLayout(0, 2));
			 * JButton transfer = new JButton("Transfer Troops");
			 * transfer.addActionListener(new TransferTroopListener()); JButton
			 * war = new JButton("Go to war!"); war.addActionListener(new
			 * AttackListener()); bott.add(transfer); bott.add(war);
			 * this.add(bott, BorderLayout.SOUTH);
			 */
			JButton butt = new JButton("Go to War!");
			butt.addActionListener(new AttackListener());
			this.add(butt, BorderLayout.SOUTH);
			this.revalidate();
			// if (theGame.isDeployPhase())
			// butt.setText("Deploy Troops Here");
			// bott.revalidate();
		}// end makeBottomLabel

		public void makeTransferMyCountryBottomLabel() {
			JButton trans = new JButton("Transfer Troops");
			trans.addActionListener(new TransferTroopListener());
			this.add(trans, BorderLayout.SOUTH);
			this.revalidate();
		}// end makeTransferMyCountryBottomLabel

		public void makePlayingYourCountryBottomLabel() {
			JButton attack = new JButton("Attack");
			attack.addActionListener(new AttackListener());
			this.add(attack, BorderLayout.SOUTH);
			this.revalidate();
		}// end makePlayingYourCountryBottomLabel

		public void makePlacementBottomLabel() {
			JButton place = new JButton("Place Troops Here");
			place.addActionListener(new PlaceAndReinforceListener());
			place.addActionListener(new TransferTroopListener());
			this.add(place, BorderLayout.SOUTH);
			this.revalidate();
		}// end makeplacementbottom

		// Only displays neighbors
		public void makePlacementCenterPanel() {
			JPanel neighbors = new JPanel();
			ArrayList<Country> neighs = curr.getNeighbors();
			neighbors.setLayout(new GridLayout(neighs.size(), 0));

			// add all of the neighbors to that panel
			for (int i = 0; i < neighs.size(); i++) {
				JLabel lab = new JLabel();
				lab.setHorizontalAlignment(JLabel.CENTER);
				lab.setFont(gotFontBody);
				lab.setText(neighs.get(i).getName());
				neighbors.add(lab);
			} // end addneighbors loop
			neighbors.setBorder(raisedWithColor);
			this.add(neighbors, BorderLayout.CENTER);
			neighbors.revalidate();
		}

		public void updatePanel() {
			updatePanel(null);
		}

		public void updatePanel(Graphics g) {
			curr = theGame.getSelectedCountry();
			this.removeAll();
			this.setLocation(12 * xWidth, 1 * yHeight);
			this.setSize(xWidth * 20, yHeight * 14);

			/*
			 * Iterator itr; Card card; for(itr = cards.listIterator();
			 * itr.hasNext(); ){ Card card; for (itr = cards.listIterator();
			 * itr.hasNext();) { card = (Card) itr.next(); // Add the JCheckBox
			 * for the card System.out.println("Card: " + card.getCountry()); }
			 */
			if (curr == null) {
				JLabel directions = new JLabel();
				directions.setHorizontalAlignment(JLabel.CENTER);
				// Font labFont = gotFontBody.deriveFont(Font.BOLD, 32);
				// directions.setFont(labFont);

				if (theGame.isPlacePhase()) {
					directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 34));
					directions.setText("Choose a Country to Claim");
					this.add(directions, BorderLayout.CENTER);

				} // end if
				else if (theGame.isDeployPhase()) {
					directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 20));
					directions.setText("Choose a country to Deploy new units");
					this.add(directions, BorderLayout.CENTER);
				} else if (theGame.isReinforcePhase()) {
					directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 30));
					directions.setText("Choose a Country to Reinforce");
					this.add(directions, BorderLayout.CENTER);
				} // end else if
				else if (attackFromFlag) {
					if (isFirstAttackPhase) {
						setUpPassButton();
						isFirstAttackPhase = false;
					}

					directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 30));
					directions.setText("Choose a Country to Attack");
					this.add(directions, BorderLayout.CENTER);
				} // end else if
				else if (attackFlag) {
					directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 30));
					directions.setText("Choose a Country to Attack From");
					this.add(directions, BorderLayout.CENTER);
				} else if (moveUnitsFlag) {
					directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 30));
					directions.setText("Choose a Country to Reinforce");
					this.add(directions, BorderLayout.CENTER);
				} // end else if
				else {
					if (isFirstAttackPhase) {
						System.out.println("lallaa");
						setUpPassButton();
						setUpClearButton();
						isFirstAttackPhase = false;
					}
					JLabel dir2 = new JLabel();
					directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 34));
					dir2.setFont(gotFontHeader.deriveFont(Font.BOLD, 34));
					directions.setText("Choose a Country to Attack"); // or
																		// Reinforce");
					dir2.setText("Or to Reinforce");
					dir2.setHorizontalAlignment(JLabel.CENTER);
					this.add(directions, BorderLayout.CENTER);
					this.add(dir2, BorderLayout.SOUTH);
					makePlayingCardPanel(g);
				} // end else

				this.revalidate();
				this.repaint();
			} // end if
			else {
				// This never changes - always displayed if a country is
				// displayed!
				makeTopLabel();

				if (theGame.isPlacePhase()) {
					makePlacementCenterPanel();
					makePlacementBottomLabel();
				} // end if
				else if (theGame.isReinforcePhase()) {
					makePlayingCenterPanel();
					makePlacementBottomLabel();

				} else if (theGame.isDeployPhase()) {
					makePlayingCardPanel(g);
					makePlayingCenterPanel();
					// Only give this option if the country is yours
					if (theGame.getCurrentPlayer().equals(curr.getOccupier()))
						makePlacementBottomLabel();
				} // end elseif
					// we should make a specific panel for if a transfer is in
					// progress.
					// we should make a specific panel for if a transfer is in
					// progress.
				else if (theGame.isAttackPhase()) {
					makePlayingCardPanel(g);
					makePlayingCenterPanel();
					makePlayingCardPanel(g);

					if (theGame.getCurrentPlayer().equals(curr.getOccupier())) {
						makePlayingMyCountryBottomLabel();
					} // end if
					else {
						makePlayingYourCountryBottomLabel();
					}
				} else {
					makePlayingCardPanel(g);
					makePlayingCenterPanel();
					// Only give this option if the country is yours
					if (theGame.getCurrentPlayer().equals(curr.getOccupier()))
						makeTransferMyCountryBottomLabel();
				} // end outer else

			} // end updatePanel

		}// end updatePanel

		/*
		 * Handles the logic for trading in cards!
		 */
		private class TradeClickListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("You wish to trade.");
				ArrayList cards = new ArrayList<Card>();
				theGame.redeemCards(nextPlayer, cards);
				repaint();
			}// end actionPerformed
		}// end tradeClickListener
	}// end countryPanel

	private class PassButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// do stuff, but I be tired :)

			if (theGame.isAttackPhase()) {
				theGame.skipAttackPhase();
				System.out.println("Passed attack phase");
			} else if (theGame.isReinforcePhase()) {
				theGame.finishTurn();
				System.out.println("Ended turn");
			}
			drawingPanel.repaint();

		}// end actionperformed
	}// end passbuttonlistener

	/*
	 * help button listener for opening the about
	 */
	private class HelpListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().compareTo("rules") == 0) {
				JOptionPane.showMessageDialog(riskGUI.this, "Fill this out later, maybe with a hyperlink to the rules",
						"Rules", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(riskGUI.this,
						"This version of Risk was created by Dylan Tobia,\nAbigail Dodd, Sydney Komro, and Jewell Finder."
								+ "\nCreated for our CS335 class as our final project.",
						"About", JOptionPane.INFORMATION_MESSAGE);
		}// actionPerformed
	}// end helpListener

	/*
	 * Handles what happens when you select a country
	 */
	private class CountryClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(e.getActionCommand() + " pressed.");
			// step through all countries until the same name as the
			// actionCommand, then return that country

			for (Country country : theGame.getGameMap().getCountries()) {
				if (country.getName().compareTo(e.getActionCommand()) == 0)
					theGame.setSelectedCountry(country);
			}
			drawingPanel.repaint();
		}// end actionperformed
	}// end countryclicklistener

	/*
	 * Listener for transfer troops buttons. Handles moving troops from one
	 * country you own to another country that you own AND that is connected to
	 * the original country by a chain of 1 or more neighbors that you also own
	 */
	private class TransferTroopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// for now just allow players to move armies
			// player move then ai move if ai turn
			// if(theGame.getCurrentPlayer() instanceof AI)
			// ((AI) theGame.getCurrentPlayer()).myTurn();
			if (theGame.isPlayPhase() && theGame.isReinforcePhase()) {
				if (theGame.getSelectedCountry().getOccupier().equals(theGame.getCurrentPlayer())) {

					if (!moveUnitsFlag) {
						if (theGame.getSelectedCountry().getForcesVal() <= 1) {
							JOptionPane.showMessageDialog(null, "You must choose a country with more than one army.",
									"Error", JOptionPane.ERROR_MESSAGE);
						} else {
							numOfUnitsToMove = getUnitsToMove(theGame.getSelectedCountry());
							moveUnitsFlag = true;
							moveUnitsFromCountry = theGame.getSelectedCountry();
						}
					} else {
						boolean result = theGame.moveUnitsToCountry(numOfUnitsToMove, moveUnitsFromCountry,
								theGame.getSelectedCountry(), theGame.getCurrentPlayer());
						// theGame.getSelectedCountry().setForcesVal(numOfUnitsToMove);
						if (result)
							moveUnitsFlag = false;
						else
							JOptionPane.showMessageDialog(null, "Invalid Country Choice. Please choose another",
									"Error", JOptionPane.ERROR_MESSAGE);
					} // end else
				} // end if
				theGame.setSelectedCountry(null);
				drawingPanel.repaint();
			}
		}// end actionPerformed

		// pops up a pane to ask how many units to move, which returns a string
		// it then tries to parse that string into an int, and if it does
		// compares
		// it to the total
		// number of units on a country. If it is larger, or equal to the total
		// number, throws an error, otherwise returns that number
		// to the gui
		public int getUnitsToMove(Country countryToRemoveUnits) {
			boolean moveFlag = false, continueFlag = false;
			int totalUnits = countryToRemoveUnits.getForcesVal(), unitsToReturn = 0;
			;
			String unitsToMove = "";

			while (!moveFlag) {
				unitsToMove = JOptionPane.showInputDialog("How Many armies? You must leave 1.");
				try {
					unitsToReturn = Integer.parseInt(unitsToMove);
					continueFlag = true;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "That was invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if (continueFlag) {
					if (unitsToReturn >= totalUnits) {
						JOptionPane.showMessageDialog(null, "You must leave 1 army.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						// theGame.getSelectedCountry().removeUnits(unitsToReturn);
						moveFlag = true;
					}
				}
			}
			return unitsToReturn;

		}// end unitsToReturn

	}// end transfertrooplistener

	/*
	 * Handles two teams going to war against each other!
	 */
	private class AttackListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (attackFromFlag == false && attackFlag == false) {
				if (theGame.getCurrentPlayer().equals(theGame.getSelectedCountry().getOccupier())) {
					attackFromFlag = true;
					attackFrom = theGame.getSelectedCountry();
				} else {
					attackFlag = true;
					attack = theGame.getSelectedCountry();
				}
			} else {
				if (attackFromFlag) {

					if (theGame.getCurrentPlayer().equals(theGame.getSelectedCountry().getOccupier())) {
						JOptionPane.showMessageDialog(null, "Cannot attack your own country. Please choose another",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else if (!attackFrom.getNeighbors().contains(theGame.getSelectedCountry())) {
						JOptionPane.showMessageDialog(null,
								"Attacking Countries must be neighbors. Please choose another", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						int numArmies = theGame.getArmiesToAttack(attackFrom);
						String attackResult = "";
						System.out.println(
								theGame.getSelectedCountry() + " " + theGame.getSelectedCountry().getForcesVal());
						System.out.println(attackFrom + " " + attackFrom.getForcesVal());
						attackResult = theGame.attack(attackFrom, theGame.getSelectedCountry(), numArmies);
						if (attackResult.compareTo("") != 0) {
							System.out.println(
									theGame.getSelectedCountry() + " " + theGame.getSelectedCountry().getForcesVal());
							System.out.println(attackFrom + " " + attackFrom.getForcesVal());
							JOptionPane.showMessageDialog(null, attackResult + " won the attack!");
							attackFromFlag = false;
						}
					}
				} else if (attackFlag) {
					if (!theGame.getCurrentPlayer().equals(theGame.getSelectedCountry().getOccupier())) {
						JOptionPane.showMessageDialog(null, "Must attack with a Country you own. Please choose another",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else if (!attack.getNeighbors().contains(theGame.getSelectedCountry())) {
						JOptionPane.showMessageDialog(null,
								"Attacking Countries must be neighbors. Please choose another", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						int numArmies = theGame.getArmiesToAttack(theGame.getSelectedCountry());
						String attackResult = "";
						System.out.println(
								theGame.getSelectedCountry() + " " + theGame.getSelectedCountry().getForcesVal());
						System.out.println(attack + " " + attack.getForcesVal());
						attackResult = theGame.attack(theGame.getSelectedCountry(), attack, numArmies);
						if (attackResult.compareTo("") != 0) {
							System.out.println(
									theGame.getSelectedCountry() + " " + theGame.getSelectedCountry().getForcesVal());
							System.out.println(attack + " " + attack.getForcesVal());
							JOptionPane.showMessageDialog(null, attackResult + " won the attack!");
							attackFlag = false;
							theGame.removeLosers();
							gameOver = theGame.isFinished();
						}
					}
				}
			}
			theGame.setSelectedCountry(null);
			drawingPanel.repaint();
		}// end actionPerformed

		public int getArmiesToAttack(Country countryToRemoveUnits) {
			boolean moveFlag = false, continueFlag = false;
			int totalUnits = countryToRemoveUnits.getForcesVal(), unitsToReturn = 0;
			String unitsToMove = "";

			while (!moveFlag) {
				unitsToMove = JOptionPane.showInputDialog("How many armies do you want to attack with?");
				try {
					unitsToReturn = Integer.parseInt(unitsToMove);
					continueFlag = true;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "That was invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if (continueFlag) {
					if (unitsToReturn > totalUnits) {
						JOptionPane.showMessageDialog(null, "Invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						// theGame.getSelectedCountry().removeUnits(unitsToReturn);
						moveFlag = true;
					}
				}
			}
			return unitsToReturn;

		}// end getArmiesToAttack
	}// end AttackListener

	/*
	 * Listener for the place and reinforcement buttons. Lets users place troops
	 * on previously unclaimed countries or reinforce countries they already
	 * own.
	 */
	private class PlaceAndReinforceListener implements ActionListener {
		boolean continueFlag = false, deployFlag = false;
		int numOfOwnedCountries;

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (theGame.isPlacePhase()) {
				// next player place army
				numOfOwnedCountries = theGame.getCurrentPlayer().getCountries().size();
				theGame.placeArmies(theGame.getSelectedCountry(), 1);
				drawingPanel.repaint();
				theGame.roundOfPlacement();
				if (numOfOwnedCountries < theGame.getCurrentPlayer().getCountries().size())
					theGame.nextPlayer();
			} else if (theGame.isReinforcePhase() && !theGame.isPlayPhase()) {
				int remainingTroops = theGame.getCurrentPlayer().getAvailableTroops();
				theGame.placeArmies(theGame.getSelectedCountry(), 1);
				drawingPanel.repaint();
				// player can reinforce countries
				theGame.roundOfReinforcement();
				if (remainingTroops > theGame.getCurrentPlayer().getAvailableTroops())
					theGame.nextPlayer();

			} // end if
			else if (theGame.isDeployPhase()) {
				deployFlag = false;
				while (!deployFlag) {
					int armiesToPlaceInt = 0;
					String armiesToPlaceStr = JOptionPane
							.showInputDialog("How many armies do you want to place? (You can place "
									+ theGame.getCurrentPlayer().getAvailableTroops() + ")");
					try {
						armiesToPlaceInt = Integer.parseInt(armiesToPlaceStr);
						continueFlag = true;
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "That was invalid number.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					if (continueFlag) {
						if (armiesToPlaceInt < 0
								|| armiesToPlaceInt > theGame.getCurrentPlayer().getAvailableTroops()) {
							JOptionPane.showMessageDialog(null, "Invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
						} else {
							theGame.placeArmies(theGame.getSelectedCountry(), armiesToPlaceInt);
							deployFlag = true;
						}
					}
				}
			}
			theGame.setSelectedCountry(null);
			drawingPanel.repaint();

		}// end actionPerformed
	}// end class

	/*
	 * Creates a new Game
	 */
	private class NewGameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			theGame.newGame();
		}// end action performed
	}// end game listener

	/*
	 * Lets the user choose between loading an old game and starting a new game
	 * 
	 * Right now: Hard coded to only allow new games
	 */
	private class GameTypeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			gameType = arg0.getActionCommand();
			System.out.println(gameType);
			splashNumPlayers();
		}// end actionperformed
	}// end GameTypeListener

	/*
	 * listener for the AI Difficulty menu
	 */
	private class AIDiffChangeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String newDifficulty = (String) JOptionPane.showInputDialog(null, "Please choose a Difficulty",
					"Set Difficulty", JOptionPane.QUESTION_MESSAGE, null, new Object[] { "Easy", "Hard" }, "Easy");

			for (Player ai : theGame.getPlayers()) {
				if (ai instanceof AI) {
					if (((AI) ai).getMenuItem().getActionCommand().compareTo(e.getActionCommand()) == 0) {
						switch (newDifficulty) {

						case "Easy":
							((AI) ai).setMyStrat(AIStrat.EASY);
							break;
						case "Hard":
							((AI) ai).setMyStrat(AIStrat.HARD);
							break;
						default:
							break;
						}// end switch
					} // end if
				} // end if
			} // end for
		}// end actionperformed
	}// end aiDiffChangeListener

	private class clearButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			attack = null;
			attackFromFlag = false;
			attackFlag = false;
			attackFrom = null;
			moveUnitsFlag = false;
			numOfUnitsToMove = 0;
			moveUnitsFromCountry = null;
			theGame.setSelectedCountry(null);
			drawingPanel.repaint();
		}// end actionperformed
	}// end clearButtonListener

	private class musicListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			musicOn = !musicOn;
			if (musicOn) {
				player.notifyPause();
			} else {
				player.pause();
			}
			setUpMenu();
			if (!splash)
				setUpAIMenu();
		}// end actionperformed
	}// end musicListener
}
