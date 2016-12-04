/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		riskGUI.java
 * 	Purpose:	GUI for visual implementation of RISK
 */


package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.AncestorListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import Model.*;
import songplayer.SoundClipPlayer;

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
	// private Game theGame;
	private TheGame theGame;
	private ImageIcon gameBoard, stark, targaryen, lannister, whiteWalkers, dothraki, wildlings;
	private JButton checkButton;
	private CountryPanel currCountryPanel;
	private JButton moveButton;
	private boolean splash, gameOver = false;
	private ImageIcon splashScreen;
	private JPanel splashInfo = new JPanel();
	private Font gotFontHeader;
	private Font gotFontBody;
	private Boolean useMaxDice = true;
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
	private StatPanel currentStatsPanel;
	private Border blueline, raisedetched, loweredetched, raisedbevel, loweredbevel, empty, raisedWithColor;
	private SoundClipPlayer player = new SoundClipPlayer();
	public static final String MU_FLAG_FILE = "moveUnitsFlag.ser";
	public static final String MU_COUNTRY_FILE = "moveUnitsCountry.ser";
	public static final String AF_FLAG_FILE = "attackFromFlag.ser";
	public static final String A_FLAG_FILE = "attackFlag.ser";
	public static final String AF_FILE = "attackFrom.ser";
	public static final String A_FILE = "attack.ser";

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

		// Making Borders
		// Creating border types to make the GUI prettier
		blueline = BorderFactory.createLineBorder(Color.BLUE);
		raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		raisedbevel = BorderFactory.createRaisedBevelBorder();
		loweredbevel = BorderFactory.createLoweredBevelBorder();
		empty = BorderFactory.createEmptyBorder();
		raisedWithColor = BorderFactory.createCompoundBorder(raisedetched, blueline);

		System.out.println("Width = " + width + " Height = " + height);

		splash = true; // comment me out for default mode
		// splash = false; // comment me out for splash screens
		setUpImages();
		setUpGui();
		setUpMenu();
		setUpHouseArray();
		setUpSplash(); // comment me out for default mode
		// comment me out for splash screens

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

	public void loadGame() {
		// TODO
		// splashNumPlayers(); //here for now so that we don't break things.
		boolean error = setUpLoad();
		if (error) {
			JOptionPane.showMessageDialog(null, "No game data has been saved. Start a new game.", "Error",
					JOptionPane.ERROR_MESSAGE);
			splashNumPlayers();
		} else {
			System.out.println("Brace Yourselves, RISK is Coming...");
			splash = false;
			drawingPanel.removeAll();
			this.remove(drawingPanel);
			setUpDrawingPanel();
			setUpMenu();
			setUpAIMenu();
			drawingPanel.revalidate();
			drawingPanel.repaint();
		}

	}

	private boolean setUpLoad() {
		boolean error = false;
		JFileChooser choose = new JFileChooser();
		choose.setCurrentDirectory(new File("./SavedGames"));
		int get = choose.showOpenDialog(null);
		if (get == JFileChooser.APPROVE_OPTION) {
			try {
				ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(choose.getSelectedFile()));
				theGame = (TheGame) inFile.readObject();
				inFile.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				error = true;
			}
		}

		/*
		 * try { ObjectInputStream inFile = new ObjectInputStream(new
		 * FileInputStream("game.ser")); theGame = (Game) inFile.readObject();
		 * inFile.close();
		 * 
		 * ObjectInputStream inFile2 = new ObjectInputStream(new
		 * FileInputStream("moveUnitsFlag.ser")); moveUnitsFlag = (boolean)
		 * inFile2.readObject(); inFile2.close();
		 * 
		 * ObjectInputStream inFile3 = new ObjectInputStream(new
		 * FileInputStream("moveUnitsCountry.ser")); moveUnitsFromCountry =
		 * (Country) inFile3.readObject(); inFile3.close();
		 * 
		 * ObjectInputStream inFile4 = new ObjectInputStream(new
		 * FileInputStream("attackFromFlag.ser")); attackFromFlag = (boolean)
		 * inFile4.readObject(); inFile4.close();
		 * 
		 * ObjectInputStream inFile5 = new ObjectInputStream(new
		 * FileInputStream("attackFlag.ser")); attackFlag = (boolean)
		 * inFile5.readObject(); inFile5.close();
		 * 
		 * ObjectInputStream inFile6 = new ObjectInputStream(new
		 * FileInputStream("attackFrom.ser")); attackFrom = (Country)
		 * inFile6.readObject(); inFile6.close();
		 * 
		 * ObjectInputStream inFile7 = new ObjectInputStream(new
		 * FileInputStream("attack.ser")); attack = (Country)
		 * inFile7.readObject(); inFile7.close();
		 * 
		 * } catch (IOException e) { error = true; e.printStackTrace(); } catch
		 * (ClassNotFoundException e) { error = true; e.printStackTrace(); }
		 */
		return error;

	}

	public void saveGame() {
		FileOutputStream gameToDisk = null;
		FileOutputStream muFlagToDisk = null;
		FileOutputStream muCountryToDisk = null;
		FileOutputStream afFlagToDisk = null;
		FileOutputStream aFlagToDisk = null;
		FileOutputStream afToDisk = null;
		FileOutputStream aToDisk = null;

		try {
			// save Game
			gameToDisk = new FileOutputStream(Game.FILE_NAME);
			ObjectOutputStream outFile = new ObjectOutputStream(gameToDisk);
			outFile.writeObject(theGame);
			outFile.close();

			// save Move Units Flag
			muFlagToDisk = new FileOutputStream(MU_FLAG_FILE);
			ObjectOutputStream outFile2 = new ObjectOutputStream(muFlagToDisk);
			outFile2.writeObject(moveUnitsFlag);
			outFile2.close();

			// save Move Units Country
			muCountryToDisk = new FileOutputStream(MU_COUNTRY_FILE);
			ObjectOutputStream outFile3 = new ObjectOutputStream(muCountryToDisk);
			outFile3.writeObject(moveUnitsFromCountry);
			outFile3.close();

			// save attack from flag
			afFlagToDisk = new FileOutputStream(AF_FLAG_FILE);
			ObjectOutputStream outFile4 = new ObjectOutputStream(afFlagToDisk);
			outFile4.writeObject(attackFromFlag);
			outFile4.close();

			// save attack flag
			aFlagToDisk = new FileOutputStream(A_FLAG_FILE);
			ObjectOutputStream outFile5 = new ObjectOutputStream(aFlagToDisk);
			outFile5.writeObject(attackFlag);
			outFile5.close();

			// save attack from
			afToDisk = new FileOutputStream(AF_FILE);
			ObjectOutputStream outFile6 = new ObjectOutputStream(afToDisk);
			outFile6.writeObject(attackFrom);
			outFile6.close();

			// save attack
			aToDisk = new FileOutputStream(A_FILE);
			ObjectOutputStream outFile7 = new ObjectOutputStream(aToDisk);
			outFile7.writeObject(attack);
			outFile7.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO
	}

	private void turnOnStatPanel() {
		currentStatsPanel = new StatPanel();
		this.remove(drawingPanel);
		this.add(currentStatsPanel);
		this.revalidate();
		this.repaint();
	}// end turnOnStatPanel

	private void turnOffStatPanel() {
		this.remove(currentStatsPanel);
		this.add(drawingPanel);
		this.revalidate();
		this.repaint();
	}// end turnOffStatPanel

	private void defaultMode() {
		humans = 1;
		ai = 5;
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

		drawingPanel = new BoardPanel();
		// splashLoading2();
		theGame = TheGame.getInstance(1, 5, false);
		setUpDrawingPanel();
		setUpMenu();
		setUpClearButton();
		setUpPassButton();
		setUpAIMenu();
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
		theGame = TheGame.getInstance(humans, ai, false);
		setUpDrawingPanel();
		setUpMenu();
		setUpClearButton();
		setUpPassButton();
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

		theGame.setPlayers(players);
		theGame.startGame();

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
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());
		setTitle("GoT Risk");
		setSize(width, height);
		this.setVisible(true);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null, "Save Data?", "End",
						JOptionPane.YES_NO_CANCEL_OPTION);
				// If the user wants to save before quit, then save!
				if (confirm == JOptionPane.CANCEL_OPTION) {
					System.out.println("CANCEL CLOSE");
				} else if (confirm == JOptionPane.OK_OPTION) {
					JFileChooser choose = new JFileChooser();
					choose.setCurrentDirectory(new File("./SavedGames"));
					int get = choose.showSaveDialog(null);
					if (get == JFileChooser.APPROVE_OPTION) {
						try {
							FileOutputStream gameToDisk = new FileOutputStream(choose.getSelectedFile() + ".ser");
							ObjectOutputStream outFile = new ObjectOutputStream(gameToDisk);
							outFile.writeObject(theGame);
							outFile.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					// saveGame();
					System.out.println("SAVE GAME");
					System.exit(0);
				} else if (confirm == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
			}
		});

	}// end setUpGui

	private void setUpMenu() {
		JMenu file = new JMenu("File");
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.addActionListener(new NewGameListener());
		file.add(newGame);
		JMenuItem saveGame = new JMenuItem("Save Game");
		saveGame.addActionListener(new saveGameListener());
		file.add(saveGame);
		JMenu settings = new JMenu("Settings");
		String music = "";
		if (musicOn)
			music = "Mute Music";
		else
			music = "Unmute Music";
		JMenuItem musicStatus = new JMenuItem(music);
		musicStatus.addActionListener(new musicListener());
		settings.add(musicStatus);
		JMenu maxDice = new JMenu("Defualt Dice");
		JMenuItem setMax = new JMenuItem("Always Max");
		setMax.addActionListener(new HelpListener());
		setMax.setActionCommand("max");
		maxDice.add(setMax);
		JMenuItem prompt = new JMenuItem("Prompt user");
		prompt.setActionCommand("prompt");
		maxDice.add(prompt);
		settings.add(maxDice);
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
		JButton passButton = new JButton("Skip this Step");
		passButton.addActionListener(new PassButtonListener());
		passButton.setSize(4 * xWidth, 2 * yHeight);
		passButton.setLocation(width - (int) (4.25 * xWidth), (int) (2.75 * yHeight));
		drawingPanel.add(passButton);
	}

	private void setUpStatButton() {
		JButton statButton = new JButton("Check out our stats");
		statButton.addActionListener(new StatPanelTurnOnListener());
		statButton.setSize(4 * xWidth, 2 * yHeight);
		statButton.setLocation(width - (int) (4.25 * xWidth), (int) (5.25 * yHeight));
		drawingPanel.add(statButton);
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
		setUpStatButton();
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

	/*********************************
	 * Other JPanels Below
	 ********************************/
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

			drawFactions(g2);
			if (!gameOver) {
				if (!splash) {
					updateCountryButtons();
					currCountryPanel.updatePanel(g);
				}

				if (theGame != null) {
					drawCurrentPlayer(g2);
					drawUnits(g2);
					// gameOver = theGame.isFinished();
					gameOver = theGame.isGameOver();
				}
				// drawGridAndNumbers(g2);
			} else {
				g2.setColor(Color.BLACK);
				g2.setFont(gotFontBody.deriveFont(Font.BOLD, 30f));
				g2.drawString(theGame.getCurrentPlayer().getName() + " has achieved total victory.",
						(drawingPanel.getWidth() / 2) - 100, drawingPanel.getHeight() / 2);

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
			Map temp = Map.getInstance(0);
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

	private class StatPanel extends JPanel {
		private Player currPlayer;
		private CurrentPlayerStatsPanel currPanel;
		private AllPlayerStatsPanel allPanel;
		private ArrayList<Player> allPlayers;

		public StatPanel() {
			super();
			currPanel = new CurrentPlayerStatsPanel();
			allPanel = new AllPlayerStatsPanel();
			// It's a border layout
			this.setLayout(new BorderLayout());

			JLabel phase = new JLabel("Phase");
			phase.setBorder(raisedWithColor);
			phase.setFont(gotFontHeader);
			this.add(phase, BorderLayout.NORTH);

			JPanel holder = new JPanel();
			holder.setLayout(new GridLayout(0, 2));
			holder.add(currPanel);
			holder.add(allPanel);
			this.add(holder, BorderLayout.CENTER);

			// The go back button
			JButton returnButton = new JButton("Return to your game");
			returnButton.addActionListener(new StatPanelTurnOffListener());
			this.add(returnButton, BorderLayout.SOUTH);
			// this.setBorder(raisedWithColor);
			this.revalidate();
			this.repaint();
		}// end constructor

		/*
		 * Left half of the gui
		 */

		/*
		 * Anytime you make this view, a new currentplayerstats panel needs to
		 * be made to reflect the current player
		 */
		private class CurrentPlayerStatsPanel extends JPanel {

			private CurrentPlayerStatsPanel() {
				super();
				currPlayer = theGame.getCurrentPlayer();
				this.setLayout(new GridLayout(2, 0));
				this.add(addNameAndCardsPanel());
				this.add(addPlayerCountriesPanel());
				this.revalidate();
				this.repaint();
			}// end constructor
		}// end currentplayerstats

		private JPanel addNameAndCardsPanel() {
			JPanel myStuff = new JPanel();
			// myStuff.setPreferredSize(new Dimension(width / 2, height / 2));
			myStuff.setLayout(new GridLayout(2, 0));

			// Drawing the name and factionr
			JPanel name = new JPanel();
			name.setLayout(new GridLayout(2, 0));
			name.setBorder(raisedWithColor);
			JLabel nameLabel = new JLabel(currPlayer.getName());
			nameLabel.setFont(gotFontHeader);
			JLabel factLabel = new JLabel(currPlayer.getFaction().getName());
			factLabel.setFont(gotFontHeader);
			name.add(nameLabel);
			name.add(factLabel);
			myStuff.add(name);

			// Drawing the cards
			JPanel cards = new JPanel();
			cards.setLayout(new GridLayout(0, 5));
			cards.setBorder(raisedWithColor);
			ArrayList<Card> currCards = currPlayer.getCards();

			for (int i = 0; i < currCards.size(); i++) {
				ImageIcon im = currCards.get(i).getMyImageIcon();
				/*
				 * JLabel card = new JLabel(im); card.setPreferredSize(new
				 * Dimension( myStuff.getWidth() / 5 - 10, myStuff.getHeight() /
				 * 2 - 10));
				 */
				CardPanel card = new CardPanel(im.getImage(), xWidth, yHeight);
				cards.add(card);
			}
			myStuff.add(cards);
			myStuff.revalidate();
			return myStuff;
		}// end nameAndCard

		private JScrollPane addPlayerCountriesPanel() {
			// build one panel for all countries
			ArrayList<Country> currCountries = currPlayer.getCountries();

			int numCols = 2;
			int numRows = currCountries.size() / numCols;
			// If there are leftovers, add one more column
			if (currCountries.size() % numCols != 0)
				numRows++;

			JPanel countPanel = new JPanel();
			countPanel.setLayout(new GridLayout(numRows, numCols));
			countPanel.setPreferredSize(new Dimension(width / 2, height / 2));

			// Make labels for all countries and add them
			for (int i = 0; i < currCountries.size(); i++) {
				JLabel countryLabel = new JLabel(currCountries.get(i).getName());
				countryLabel.setFont(gotFontBody);
				countPanel.add(countryLabel);
			}
			countPanel.revalidate();
			JScrollPane scroll = new JScrollPane(countPanel);
			scroll.setBorder(raisedWithColor);
			return scroll;
		}// end addPlayerCountriesPanel

		/*
		 * Begin Right Half of GUI
		 */
		private class AllPlayerStatsPanel extends JPanel {
			private AllPlayerStatsPanel() {
				super();
				allPlayers = theGame.getPlayers();
				this.setLayout(new GridLayout(2, 1));
				this.add(allPlayersList());
				this.add(contsAndLogPanel());
				this.revalidate();
				this.repaint();
			}// end constructor

			private JPanel allPlayersList() {
				JPanel playList = new JPanel();
				playList.setLayout(new GridLayout(allPlayers.size(), 1));

				for (int i = 0; i < allPlayers.size(); i++) {
					JLabel aPlayer = new JLabel();
					aPlayer.setFont(gotFontBody);

					// set up the name
					String player = allPlayers.get(i).getName();
					player += " " + allPlayers.get(i).getFaction().getName();
					player += ": " + allPlayers.get(i).getCountries().size() + " countries.";
					aPlayer.setText(player);

					// add it
					playList.add(aPlayer);
				} // end for
				playList.setBorder(raisedWithColor);
				playList.revalidate();
				playList.repaint();
				return playList;
			}// end allPlayersList

			private JPanel contsAndLogPanel() {
				JPanel lastPanel = new JPanel();
				lastPanel.setLayout(new GridLayout(1, 2));

				JPanel contPanel = new JPanel();
				contPanel.setBorder(raisedWithColor);
				contPanel.setLayout(new GridLayout(6, 1));
				String[] allContinents = theGame.getMap().getContinentOwnersAsStrings();

				for (int i = 0; i < allContinents.length; i++) {
					JLabel lab = new JLabel(allContinents[i]);
					lab.setFont(gotFontBody);
					contPanel.add(lab);
				}
				lastPanel.add(contPanel);

				JTextArea log = new JTextArea();
				log.setEditable(false);
				log.setText(theGame.getGameLog());
				JScrollPane scroll = new JScrollPane(log);
				lastPanel.add(scroll);

				return lastPanel;
			}

		}// end allplayerstats
	}

	private class CountryPanel extends JPanel {
		// private JPanel centerPanel;
		// private JButton makeAMoveButton;
		private boolean isFirstAttackPhase = true;
		private Country curr;

		// Here while I play with various borders!

		/*
		 * public void PaintComponent(Graphics g){ update(g);
		 * super.paintComponent(g);
		 * 
		 * }//end
		 */

		public CountryPanel() {
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
			int cols = myCards.size();
			if (cols < 1)
				cols = 1;
			showCards.setLayout(new GridLayout(0, cols));
			// ArrayList<Card> currCards =
			// theGame.getCurrentPlayer().getCards();
			JCheckBox checkBox = new JCheckBox();
			// Get the image for this card
			for (int i = 0; i < myCards.size(); i++) {
				Image im = myCards.get(i).getMyImage();
				ImageIcon ic = new ImageIcon(
						im.getScaledInstance((int) (xWidth * 1), (int) (yHeight * 1.5), Image.SCALE_DEFAULT));

				checkBox = new JCheckBox(ic);
				checkBox.setActionCommand("" + i);
				checkBox.setSelected(false);
				/*
				 * Image im = currCards.get(i).getMyImage(); JPanel oneCard =
				 * new CardPanel(im, xWidth, yHeight); // g.drawImage(im,
				 * 0,0,null); Dimension myD = new Dimension((int) (0.75 *
				 * xWidth), (int) (1.5 * yHeight));
				 * oneCard.setPreferredSize(myD); oneCard.repaint();
				 */
				showCards.add(checkBox);
			} // end for

			cards.add(showCards);
			JButton trade = new JButton("Trade in Cards");
			trade.addActionListener(new TradeClickListener());
			cards.add(trade);
			cards.setBorder(raisedWithColor);
			this.add(cards, BorderLayout.CENTER);
			showCards.repaint();
			showCards.revalidate();
			cards.revalidate();
			this.revalidate();
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

				if (theGame.isRedeemCardPhase()) {
					if (!theGame.isPlayPhase())
						directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 18));
					directions.setText("Redeem your cards");
					this.add(directions, BorderLayout.NORTH);
					makePlayingCardPanel(g);
				} // end if
				else if (theGame.isPlacePhase()) {
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
				else if (theGame.isAttackPhase()) {
					if (theGame.getMoveFrom() != null) {
						directions.setText("Choose a Country to Attack");
					} // end if
					else if (theGame.getMoveTo() != null) {
						directions.setText("Choose a Country to Attack From");
					} // end else if
					else {
						directions.setText("Choose a Country to Go to War!");
					} // end else

					directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 30));

					this.add(directions, BorderLayout.CENTER);
				} // end else if
				// else if (attackFlag) {
				// directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 30));
				// directions.setText("Choose a Country to Attack From");
				// this.add(directions, BorderLayout.CENTER);
				// } else if (moveUnitsFlag) {
				// directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 30));
				// directions.setText("Choose a Country to Reinforce");
				// this.add(directions, BorderLayout.CENTER);
				// } // end else if
				else {

					JLabel dir2 = new JLabel();
					directions.setFont(gotFontHeader.deriveFont(Font.BOLD, 34));
					dir2.setFont(gotFontHeader.deriveFont(Font.BOLD, 34));
					directions.setText("Choose a Country to Attack"); // or

					this.add(directions, BorderLayout.CENTER);
					// makePlayingCardPanel(g);
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
				else if (!theGame.isPlayPhase() && theGame.isReinforcePhase()) {
					makePlayingCenterPanel();
					makePlacementBottomLabel();

				} else if (theGame.isDeployPhase()) {
					// makePlayingCardPanel(g);
					makePlayingCenterPanel();
					// Only give this option if the country is yours
					if (theGame.getCurrentPlayer().equals(curr.getOccupier()))
						makePlacementBottomLabel();
				} // end elseif
					// we should make a specific panel for if a transfer is in
					// progress.
				else if (theGame.isAttackPhase()) {
					// makePlayingCardPanel(g);
					makePlayingCenterPanel();

					if (theGame.getCurrentPlayer().equals(curr.getOccupier())) {
						makePlayingMyCountryBottomLabel();
					} // end if
					else {
						makePlayingYourCountryBottomLabel();
					}
				} else {
					// makePlayingCardPanel(g);
					makePlayingCenterPanel();
					// Only give this option if the country is yours
					if (theGame.getCurrentPlayer().equals(curr.getOccupier()))
						makeTransferMyCountryBottomLabel();
				} // end outer else

			} // end updatePanel

		}// end updatePanel

		/******************************
		 * Listeners are Below this line!!!
		 *****************************/

		private class CardBoxListener implements ItemListener {

			@Override
			public void itemStateChanged(ItemEvent itE) {

				// if it's selected
				if (itE.getStateChange() == ItemEvent.SELECTED) {

				} // end if
					// if it's deseletced
				else {

				} // end else

			}// end itemstatechanged

		}// end cardboxlistener

		/*
		 * Handles the logic for trading in cards!
		 */
		private class TradeClickListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("You wish to trade.");
				ArrayList<Card> cards = new ArrayList<Card>();

				// TODO JOptionPane to select the cards the the player wants to
				// redeem.
				// ((HumanPlayer) theGame.getCurrentPlayer()).setCardsToRedeem(
				// cards, theGame.getNumRedemptions());
				((HumanPlayer) theGame.getCurrentPlayer()).setCardsToRedeem(cards);
				// int additionalTroups = ((HumanPlayer) theGame
				// .getCurrentPlayer()).redeemCards();
				// if (additionalTroups > 0) {
				// theGame.incrementNumRedemptions();
				// theGame.getCurrentPlayer().discardCards(cards);
				// theGame.getDeck().addToDiscardPile(cards);
				// } else
				JOptionPane.showMessageDialog(riskGUI.this, "Illegal amount of cards set to redeem.",
						"Can't redeem cards.", JOptionPane.INFORMATION_MESSAGE);
				repaint();
			}// end actionPerformed
		}// end tradeClickListener
	}// end countryPanel

	private class PassButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (theGame.isAttackPhase()) {
				if (theGame.skipAttackPhase()) {
					JOptionPane.showMessageDialog(riskGUI.this, "You earned a new card!", "Card Earned",
							JOptionPane.INFORMATION_MESSAGE);
				} // end if
				System.out.println("Passed attack phase");
			} else if (theGame.isReinforcePhase() && theGame.isPlayPhase()) {
				// theGame.finishTurn();
				theGame.passReinforcementPhase();
				System.out.println("Ended turn");
			} else if (theGame.isRedeemCardPhase()) {
				if (!theGame.skipCardRedemption())
					JOptionPane.showMessageDialog(riskGUI.this, "Because you have 5 cards, you must redeem 3.",
							"Card Redemption Warning", JOptionPane.INFORMATION_MESSAGE);
			} // end else if
			else {
				JOptionPane.showMessageDialog(riskGUI.this, "Sorry, you're not allowed to skip this phase.",
						"Pass Warning", JOptionPane.INFORMATION_MESSAGE);
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

				URL rules = null;
				try {
					rules = new URL("http://www.cs.arizona.edu/~mercer/Projects/335/Final/RiskRules.pdf");
				} catch (MalformedURLException e1) {

					e1.printStackTrace();
				}
				JEditorPane ep = new JEditorPane("text/html",
						"<a href=\"" + rules.toString() + "\">Rules given to us by Rick Mercer</a>" //
								+ "</body></html>");
				ep.setEditable(false);
				ep.addHyperlinkListener(new LinkClickListener(rules));
				JOptionPane.showMessageDialog(null, ep);

			} else if(e.getActionCommand().compareTo("about") == 0){
				JOptionPane.showMessageDialog(riskGUI.this,
						"This version of Risk was created by Dylan Tobia,\nAbigail Dodd, Sydney Komro, and Jewell Finder."
								+ "\nCreated for our CS335 class as our final project.",
						"About", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(e.getActionCommand().compareTo("max") == 0)
			{
				useMaxDice = true;
			}
			else if(e.getActionCommand().compareTo("prompt") == 0)
				useMaxDice = false;
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
			// No country is selected if it's the card phase
			// because that's not allowed :)
			if (!theGame.isRedeemCardPhase()) {
				for (Country country : theGame.getGameMap().getCountries()) {
					if (country.getName().compareTo(e.getActionCommand()) == 0)
						theGame.setSelectedCountry(country);
				} // end for
			} // end if

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

			if (theGame.isPlayPhase() && theGame.isReinforcePhase()) {
				// Set the move from first
				if (theGame.getMoveFrom() == null) {
					if (theGame.playerIsOwner()) {
						theGame.setMoveFrom();
					} // end if
					else {
						JOptionPane.showMessageDialog(null, "You can only transfer troops from your own countries.",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} // end if
					// Then set the move To
				else if (theGame.getMoveTo() == null) {
					if (theGame.playerIsOwner()) {
						theGame.setMoveTo();
						int numArmies = getArmiesToUse(theGame.getMoveFrom());// Get
																				// Armies
																				// to
																				// Move

						boolean success = theGame.transferTroops(numArmies);

						if (!success) {
							JOptionPane.showMessageDialog(null,
									"There must be a path of your own countries between your two choices.", "Error",
									JOptionPane.ERROR_MESSAGE);
						} // end if

						// Always clear the game, even if failure
						theGame.clearSelections();
					} // end if
					else {
						JOptionPane.showMessageDialog(null, "You can only transfer troops from your own countries.",
								"Error", JOptionPane.ERROR_MESSAGE);
					} // end else
				} // end else if
			} // end if

			drawingPanel.repaint();
		}// end actionPerformed
			// Old Version below

		// for now just allow players to move armies
		// player move then ai move if ai turn
		// if(theGame.getCurrentPlayer() instanceof AI)
		// ((AI) theGame.getCurrentPlayer()).myTurn();
		// System.out.println("Place clicked");
		// if (theGame.isPlayPhase() && theGame.isReinforcePhase()) {
		// if (theGame.getSelectedCountry().getOccupier()
		// .equals(theGame.getCurrentPlayer())) {
		//
		// if (!moveUnitsFlag) {
		// if (theGame.getSelectedCountry().getForcesVal() <= 1) {
		// JOptionPane
		// .showMessageDialog(
		// null,
		// "You must choose a country with more than one army.",
		// "Error", JOptionPane.ERROR_MESSAGE);
		// } else {
		// numOfUnitsToMove = getUnitsToMove(theGame
		// .getSelectedCountry());
		// moveUnitsFlag = true;
		// moveUnitsFromCountry = theGame.getSelectedCountry();
		// }
		// } else {
		// boolean result = theGame.moveUnitsToCountry(
		// numOfUnitsToMove, moveUnitsFromCountry,
		// theGame.getSelectedCountry(),
		// theGame.getCurrentPlayer());
		// // theGame.getSelectedCountry().setForcesVal(numOfUnitsToMove);
		// if (result)
		// moveUnitsFlag = false;
		// else
		// JOptionPane
		// .showMessageDialog(
		// null,
		// "Invalid Country Choice. Please choose another",
		// "Error", JOptionPane.ERROR_MESSAGE);
		// } // end else
		// } // end if
		// theGame.setSelectedCountry(null);
		// drawingPanel.repaint();
		// }
		// }// end actionPerformed

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
			// One of the countries needs to be set
			if (theGame.getMoveFrom() == null && theGame.getMoveTo() == null) {
				if (theGame.playerIsOwner()) {
					if (theGame.getSelectedCountry().getForcesVal() > 1)
						theGame.setMoveFrom();
					else {
						JOptionPane.showMessageDialog(null,
								"Cannot attack from a country with only one army. Please choose another", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} // end if

				else {
					theGame.setMoveTo();
				} // end else
			} // end if

			// the other one needs to be set
			else if (theGame.getMoveFrom() == null) {
				if (theGame.playerIsOwner()) {
					// Only allow attack from a country with more than one army
					if (theGame.getSelectedCountry().getForcesVal() > 1) {
						theGame.setMoveFrom();
						if (theGame.getMoveTo().isMyNeighbor(theGame.getMoveFrom())) {
							System.out.println(theGame.getMoveTo().isMyNeighbor(theGame.getMoveFrom()));
							int numArmies = getArmiesToUse(theGame.getMoveFrom());
							theGame.attack(numArmies);
						} // end if
						else {
							JOptionPane.showMessageDialog(null, "You can only attack your neighbors.", "Error",
									JOptionPane.ERROR_MESSAGE);
							theGame.clearSelections();
						}
					} // end if
					else {
						JOptionPane.showMessageDialog(null,
								"Cannot attack from a country with only one army. Please choose another", "Error",
								JOptionPane.ERROR_MESSAGE);
					} // end else
				} // end if

				else {
					JOptionPane.showMessageDialog(null,
							"Cannot attack from someone else's country. Please choose another", "Error",
							JOptionPane.ERROR_MESSAGE);
				} // end else
			} // end else if

			// the other one needs to be set
			else if (theGame.getMoveTo() == null) {
				if (!theGame.playerIsOwner()) {
					theGame.setMoveTo();

					if (theGame.getMoveTo().isMyNeighbor(theGame.getMoveFrom())) {
						int numArmies = getArmiesToUse(theGame.getMoveFrom());
						theGame.attack(numArmies);
					} // end if
					else {
						JOptionPane.showMessageDialog(null, "You can only attack your neighbors.", "Error",
								JOptionPane.ERROR_MESSAGE);
						theGame.clearSelections();
					} // end else
				} // end if
				else {
					JOptionPane.showMessageDialog(null, "Cannot attack your own country. Please choose another",
							"Error", JOptionPane.ERROR_MESSAGE);
				} // end else
			} // end else if
			theGame.setSelectedCountry(null);
			drawingPanel.repaint();
		}// end actionPerformed
	}// end AttackListener
	// private boolean firstAttackPast = false, continueFlag = false;
	// int diceAllowed = 0, actualNumDice = 0;
	//
	// @Override
	// public void actionPerformed(ActionEvent arg0) {
	//
	// if (attackFromFlag == false && attackFlag == false) {
	// if
	// (theGame.getCurrentPlayer().equals(theGame.getSelectedCountry().getOccupier()))
	// {
	// attackFromFlag = true;
	// attackFrom = theGame.getSelectedCountry();
	// } else {
	// attackFlag = true;
	// attack = theGame.getSelectedCountry();
	// }
	// } else {
	// if (attackFromFlag) {
	//
	// if
	// (theGame.getCurrentPlayer().equals(theGame.getSelectedCountry().getOccupier()))
	// {
	// JOptionPane.showMessageDialog(null, "Cannot attack your own country.
	// Please choose another",
	// "Error", JOptionPane.ERROR_MESSAGE);
	// } else if
	// (!attackFrom.getNeighbors().contains(theGame.getSelectedCountry())) {
	// JOptionPane.showMessageDialog(null,
	// "Attacking Countries must be neighbors. Please choose another", "Error",
	// JOptionPane.ERROR_MESSAGE);
	// } else {
	// if (attackFrom.getForcesVal() > 1) {
	//
	// int forces = attackFrom.getForcesVal();
	// if (forces >= 4) {
	// diceAllowed = 3;
	// } else if (forces == 3) {
	// diceAllowed = 2;
	// } else
	// diceAllowed = 1;
	//
	// while (!continueFlag) {
	// String sNumDice = JOptionPane.showInputDialog(
	// "How many dice would you like to throw? You can throw up to " +
	// diceAllowed);
	// try {
	// actualNumDice = Integer.parseInt(sNumDice);
	// if (actualNumDice <= diceAllowed || actualNumDice > 0) {
	// continueFlag = true;
	// } else {
	// } // error
	//
	// } catch (NumberFormatException e) {
	// // invalid number
	// }
	//
	// }
	// }
	// // TODO start animation here, I think
	// Boolean attackResult = false;
	// System.out.println(
	// theGame.getSelectedCountry() + " " +
	// theGame.getSelectedCountry().getForcesVal());
	// System.out.println(attackFrom + " " + attackFrom.getForcesVal());
	// attackResult = theGame.attack(attackFrom, theGame.getSelectedCountry(),
	// actualNumDice);
	// if (attackResult) {
	// System.out.println(
	// theGame.getSelectedCountry() + " " +
	// theGame.getSelectedCountry().getForcesVal());
	// System.out.println(attackFrom + " " + attackFrom.getForcesVal());
	// JOptionPane.showMessageDialog(null, attackFrom.toString() + " won the
	// attack!");
	// int units = theGame.getUnits(attackFrom);
	// theGame.moveUnitsToCountry(units, attackFrom, attack,
	// theGame.getCurrentPlayer());
	//
	// attackFromFlag = false;
	// theGame.removeLosers();
	// gameOver = theGame.isFinished();
	// }
	//
	// }
	// } else if (attackFlag) {
	// if
	// (!theGame.getCurrentPlayer().equals(theGame.getSelectedCountry().getOccupier()))
	// {
	// JOptionPane.showMessageDialog(null, "Must attack with a Country you own.
	// Please choose another",
	// "Error", JOptionPane.ERROR_MESSAGE);
	// } else if (!attack.getNeighbors().contains(theGame.getSelectedCountry()))
	// {
	// JOptionPane.showMessageDialog(null,
	// "Attacking Countries must be neighbors. Please choose another", "Error",
	// JOptionPane.ERROR_MESSAGE);
	// } else {
	// if (attackFrom.getForcesVal() > 1) {
	//
	// int forces = attackFrom.getForcesVal();
	// if (forces >= 4) {
	// diceAllowed = 3;
	// } else if (forces == 3) {
	// diceAllowed = 2;
	// } else
	// diceAllowed = 1;
	//
	// while (!continueFlag) {
	// String sNumDice = JOptionPane.showInputDialog(
	// "How many dice would you like to throw? You can throw up to " +
	// diceAllowed);
	// try {
	// actualNumDice = Integer.parseInt(sNumDice);
	// if (actualNumDice <= diceAllowed || actualNumDice > 0) {
	// continueFlag = true;
	// } else {
	// } // error
	//
	// } catch (NumberFormatException e) {
	// // invalid number
	// }
	//
	// }
	// }
	// boolean attackResult = false;
	// System.out.println(
	// theGame.getSelectedCountry() + " " +
	// theGame.getSelectedCountry().getForcesVal());
	// System.out.println(attack + " " + attack.getForcesVal());
	// attackResult = theGame.attack(theGame.getSelectedCountry(), attack,
	// actualNumDice);
	// if (attackResult) {
	// System.out.println(
	// theGame.getSelectedCountry() + " " +
	// theGame.getSelectedCountry().getForcesVal());
	// System.out.println(attack + " " + attack.getForcesVal());
	// JOptionPane.showMessageDialog(null, attackResult + " won the attack!");
	// attackFlag = false;

	public int getArmiesToUse(Country countryToRemoveUnits) {
		boolean moveFlag = false, continueFlag = false;
		int totalUnits = countryToRemoveUnits.getForcesVal(), unitsToReturn = 0;
		String unitsToMove = "";

		while (!moveFlag) {
			unitsToMove = JOptionPane.showInputDialog("How many armies do you want to use? You must leave 1.");
			try {
				unitsToReturn = Integer.parseInt(unitsToMove);
				continueFlag = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "That was invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			if (continueFlag) {
				if (unitsToReturn >= totalUnits) {
					JOptionPane.showMessageDialog(null, "Invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					// theGame.getSelectedCountry().removeUnits(unitsToReturn);
					moveFlag = true;
				}
			}
		}
		return unitsToReturn;

	}// end getArmiesToAttack
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
			theGame.play();
			// //System.out.println("Place clicked");
			// if (theGame.isPlacePhase()) {
			// // next player place army
			// numOfOwnedCountries = theGame.getCurrentPlayer().getCountries()
			// .size();
			// //theGame.placeArmies(1);
			// drawingPanel.repaint();
			// /* theGame.roundOfPlacement();
			// if (numOfOwnedCountries < theGame.getCurrentPlayer()
			// .getCountries().size())
			// theGame.nextPlayer();*/
			// theGame.play();
			// } else if (theGame.isReinforcePhase() && !theGame.isPlayPhase())
			// {
			// int remainingTroops = theGame.getCurrentPlayer()
			// .getAvailableTroops();
			// theGame.placeArmies(1);
			// drawingPanel.repaint();
			// // player can reinforce countries
			// // theGame.roundOfReinforcement();
			// // if (remainingTroops > theGame.getCurrentPlayer()
			// // .getAvailableTroops())
			// // theGame.nextPlayer();
			//
			// } // end if
			// else if (theGame.isDeployPhase()) {
			// deployFlag = false;
			// while (!deployFlag) {
			// int armiesToPlaceInt = 0;
			// String armiesToPlaceStr = JOptionPane
			// .showInputDialog("How many armies do you want to place? (You can
			// place "
			// + theGame.getCurrentPlayer()
			// .getAvailableTroops() + ")");
			// try {
			// armiesToPlaceInt = Integer.parseInt(armiesToPlaceStr);
			// continueFlag = true;
			// } catch (NumberFormatException e) {
			// JOptionPane.showMessageDialog(null,
			// "That was invalid number.", "Error",
			// JOptionPane.ERROR_MESSAGE);
			// }
			// if (continueFlag) {
			// if (armiesToPlaceInt < 0
			// || armiesToPlaceInt > theGame
			// .getCurrentPlayer()
			// .getAvailableTroops()) {
			// JOptionPane.showMessageDialog(null,
			// "Invalid number.", "Error",
			// JOptionPane.ERROR_MESSAGE);
			// } else {
			// theGame.placeArmies(armiesToPlaceInt);
			// deployFlag = true;
			// }//end else
			// }//end if
			// }
			// }
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
			theGame.clear();
			splashNumPlayers();
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
			if (gameType.compareTo("New Game!") == 0)
				splashNumPlayers();
			else
				loadGame();
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
			/*
			 * attack = null; attackFromFlag = false; attackFlag = false;
			 * attackFrom = null; moveUnitsFlag = false; numOfUnitsToMove = 0;
			 * moveUnitsFromCountry = null; theGame.setSelectedCountry(null);
			 */
			theGame.clearSelections();
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

	private class LinkClickListener implements HyperlinkListener {
		private URL myUrl;

		public LinkClickListener(URL rules) {
			myUrl = rules;

		}

		@Override
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
				Desktop myDesktop = Desktop.getDesktop();
				try {
					myDesktop.browse(myUrl.toURI());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		}
	}

	private class StatPanelTurnOnListener implements ActionListener {
		private boolean turnedOn = false;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			turnOnStatPanel();
		}// end actionPerformed
	}// end StatPanelListener

	private class StatPanelTurnOffListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			turnOffStatPanel();
		}// end actionPerformed
	}// end turned off listener

	private class saveGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser choose = new JFileChooser();
			choose.setCurrentDirectory(new File("./SavedGames"));
			int get = choose.showSaveDialog(null);
			if (get == JFileChooser.APPROVE_OPTION) {
				try {
					FileOutputStream gameToDisk = new FileOutputStream(choose.getSelectedFile() + ".ser");
					ObjectOutputStream outFile = new ObjectOutputStream(gameToDisk);
					outFile.writeObject(theGame);
					outFile.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}// end actionPerformed
	}// end saveGame listener

}// end GUI
