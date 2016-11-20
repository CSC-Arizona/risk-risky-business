/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		riskGUI.java
 * 	Purpose:	GUI for visual implementation of RISK
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.AncestorListener;

import Model.Country;
import Model.Game;
import Model.Map;

//just a simple GUI to start, with a drawingPanel for map stuff
public class riskGUI extends JFrame {

	public static void main(String[] args) throws UnknownHostException, IOException
	{
		new riskGUI().setVisible(true);
	}

	private BoardPanel drawingPanel;
	private JMenuBar menu;
	private int width = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	private int height = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	private int xWidth = 0;
	private int yHeight = 0;
	//private Map map; dont think this is needed anymore cause it is stored within theGame
	private Game theGame;
	private ImageIcon gameBoard;
	private JButton checkButton;

	public riskGUI()
	{
		System.out.println("Width = " + width + " Height = " + height);
	
		//creates or grabs an instance of the game, first variable is number of human players, second is total number of players
		theGame = Game.getInstance(1,3);
		setUpGui();
		setUpDrawingPanel();
		setUpGameStatsPanel();
		setUpMenu();
		
		
		
	}

	private void setUpGui()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());
		setTitle("GoT Risk");
		setSize(width, height);
	}

	private void setUpMenu()
	{
		JMenu file = new JMenu("File");
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.addActionListener(new newGameListener());
		file.add(newGame);
		JMenu help = new JMenu("Help");
		menu = new JMenuBar();
		menu.add(file);
		JMenuItem about = new JMenuItem("About");
		menu.add(help);
		
		JMenuItem rules = new JMenuItem("Rules");
		help.add(rules);
		help.add(about);
		this.setJMenuBar(menu);

		rules.setActionCommand("rules");
		about.setActionCommand("about");
		rules.addActionListener(new helpListener());
		about.addActionListener(new helpListener());

	}

	private void setUpDrawingPanel()
	{
		gameBoard = new ImageIcon("GoTMapRisk.jpg");
		drawingPanel = new BoardPanel();
		drawingPanel.setLayout(null);
		drawingPanel.setSize(width - 40, height - 70);
		drawingPanel.setLocation(10, 10);
		drawingPanel.setBackground(Color.LIGHT_GRAY);
		drawingPanel.repaint();
		
		//Prepare to draw the buttons!
		Dimension drawD = drawingPanel.getSize();
		xWidth = (int) (drawD.getWidth()/40);
		yHeight = (int) (drawD.getHeight()/40);
		drawCountryButtons();
		
		this.add(drawingPanel, BorderLayout.CENTER);
		
	}
	
	private void setUpGameStatsPanel(){
		//Currently there to print nothing useful, but see what the game board
		//will look like
		JPanel gameStatsPanel = new JPanel();
		gameStatsPanel.setLayout(new GridLayout(1,6));
		gameStatsPanel.setPreferredSize(new Dimension(100,10));
		gameStatsPanel.setBackground(Color.pink);
		//this.add(gameStatsPanel, BorderLayout.EAST);
	}//end setUpGameStatsPanel
	

	//draws buttons over the name of all of the countries
	private void drawCountryButtons()
	{
		for (Country country : theGame.getGameMap().getCountries())
		{
			//The Make button method has the same logic that was previously here
			country.makeButton(xWidth, yHeight, new CountryClickListener());
			drawingPanel.add(country.getButton());
		}//end for
		
		//Manually adjusts the size and shape of a few of the weirder 
		//shaped country buttons
		
	}//end drawCountryButtons
	
	
	//Updates those buttons if the size of the panel changes
	private void updateCountryButtons(){
		for (Country country : theGame.getGameMap().getCountries()){
			country.updateButton(xWidth, yHeight);
		}
	}

	private class BoardPanel extends JPanel {
		@Override
		public void paintComponent(Graphics g)
		{

			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.white);
			super.paintComponent(g2);
			
			Image tmp = gameBoard.getImage();
			g2.drawImage(tmp, 0, 0, drawingPanel.getWidth(), drawingPanel.getHeight(), null);

			Dimension drawD = drawingPanel.getSize();
			xWidth = (int) (drawD.getWidth()/40);
			yHeight = (int) (drawD.getHeight()/40);
			
		
			updateCountryButtons();
	//		drawGridAndNumbers(g2);

		}

		//draws a 40X40 grid over the risk map. Used for determining where to place buttons.
		private void drawGridAndNumbers(Graphics2D g2)
		{
			for (int i = xWidth; i < width - 40; i += xWidth)
			{
				g2.drawLine(i, 0, i, height - 70);
			}

			for (int i = yHeight; i < height - 70; i += yHeight)
			{
				g2.drawLine(0, i, width - 40, i);
			}

			int xCount = xWidth / 2;
			int yCount = yHeight / 2;

			int startX = xCount;
			int startY = yCount;
			int y = 0;
			for (int i = 1; i < 40; i++)
			{
				//int x = 1;
				y++;
				startY = yCount;

				for (int j = 1; j < 40; j++)
				{
					g2.drawString(Integer.toString(y), startX, startY);
					startY += yHeight;

				//	x++;
				}
				startX += xWidth;
			}
		}

	}

	//help button listener for opening the about
	private class helpListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getActionCommand().compareTo("rules") == 0)
			{
				JOptionPane.showMessageDialog(riskGUI.this,  "Fill this out later, maybe with a hyperlink to the rules", "Rules", JOptionPane.INFORMATION_MESSAGE);
			}
			else
				JOptionPane.showMessageDialog(riskGUI.this,
					"This version of Risk was created by Dylan Tobia,\nAbigail Dodd, Sydney Komro, and Jewell Finder."
							+ "\nCreated for our CS335 class as our final project.",
					"About", JOptionPane.INFORMATION_MESSAGE);

		}

	}
	
	private class newGameListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			theGame.startGame(0);
			
		}
		
	}
}
