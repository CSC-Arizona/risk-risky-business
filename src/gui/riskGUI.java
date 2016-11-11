/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		riskGUI.java
 * 	Purpose:	GUI for visual implementation of RISK
 */

package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



//just a simple GUI to start, with a drawingPanel for map stuff
public class riskGUI extends JFrame{

	public static void main(String[] args) throws UnknownHostException, IOException
	{
		new riskGUI().setVisible(true);
	}
	
	private BoardPanel drawingPanel;
	private JMenuBar menu;
	private int width;
	private int height;
	private ImageIcon gameBoard;
	private JButton checkButton;
	public riskGUI(){
		width = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
		height = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
		System.out.println("Width = " + width + " Height = " + height);
		setUpGui();
		setUpDrawingPanel();
		setUpMenu();
	}
	
	private void setUpGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(null);
		setTitle("GOT Risk");
		setSize(width, height);
	}
	
	private void setUpMenu() {
		JMenu help = new JMenu("Help");
		menu = new JMenuBar();
		JMenuItem about = new JMenuItem("About");
		menu.add(help);
		help.add(about);
		this.setJMenuBar(menu);
		
		about.addActionListener(new helpListener());
		
	}
	private void setUpDrawingPanel() {
		int xWidth = 47;
		int yHeight = 24;
		gameBoard = new ImageIcon("GoTMapRisk.jpg");
		drawingPanel = new BoardPanel();
		drawingPanel.setLayout(null);
		drawingPanel.setSize(width-40, height-70);
		drawingPanel.setLocation(10,10);
		drawingPanel.setBackground(Color.LIGHT_GRAY);
		drawingPanel.repaint();
		checkButton = new JButton();
		checkButton.setSize(10,10);
		checkButton.setLocation((int)(3.5*xWidth), (int)(18.5*yHeight));
		drawingPanel.add(checkButton);
		this.add(drawingPanel);
		
		
	}
	
	private class BoardPanel extends JPanel{
		@Override
		public void paintComponent(Graphics g){

			
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.white);
			
			;
			super.paintComponent(g2);
			Image tmp = gameBoard.getImage();
			g2.drawImage(tmp, 0, 0, drawingPanel.getWidth(), drawingPanel.getHeight(),null);
			
			//drawGridAndNumbers(g2);

		}

		private void drawGridAndNumbers(Graphics2D g2)
		{
			int xWidth = 47;
			int yHeight = 24;
			for(int i = (width-40)/40; i < width-40; i += ((width-40)/40))
			{
				g2.drawLine(i, 0, i, height-70);
			}
			
			for(int i = (height-70)/40; i < height-70; i+= (height-70)/40)
			{
				g2.drawLine(0, i, width-40, i);
			}
			
			int xCount = xWidth/2;
			int yCount = yHeight/2;
					
			int startX = xCount;
			int startY = yCount;;
			for(int i = 1; i < 40; i++)
			{	int x = 1;
				startY = yCount;
			
				for(int j = 1; j < 40; j++)
				{
					g2.drawString(Integer.toString(x), startX, startY);
					startY += yHeight;
					
							
					x++;
				}
				startX += xWidth;
			}			
		}
		
	}
	
	private class helpListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(riskGUI.this, "This version of Risk was created by Dylan Tobia,\nAbigail Dodd, Sydney Komro, and Jewell Finder."
					+ "\nCreated for our CS335 class as our final project.", "About",JOptionPane.INFORMATION_MESSAGE);
			
		}
		
	}
}
