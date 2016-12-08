/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		Map.java
 * 	Purpose:	Singleton Map class holds all country, neighbor, and continent information for the risk game.
 */

package Model;

import gui.riskGUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable {

	private Country countries[] = new Country[50];
	private Continent blue;
	private Continent green;
	private Continent orange;
	private Continent pink;
	private Continent red;
	private Continent black;
	private Continent yellow;
	private static ArrayList<Continent> allContinents;
	private static Map gameMap = null;

	/*
	 * constructor hard codes all of the continents in this game (since there
	 * are so few), countries, and neighbor relationships
	 */
	private Map(int i) {
		blue = new Continent(4, "Blue");
		green = new Continent(2, "Green");
		orange = new Continent(3, "Orange");
		pink = new Continent(3, "Pink");
		red = new Continent(2, "Red");
		black = new Continent(5, "Black");
		yellow = new Continent(7, "Yellow");

		allContinents = new ArrayList<>();
		allContinents.add(blue);
		allContinents.add(green);
		allContinents.add(orange);
		allContinents.add(pink);
		allContinents.add(red);
		allContinents.add(black);
		allContinents.add(yellow);

		fillCountries(i);

	}// end constructor

	/*
	 * getInstance Returns an instance of this singleton class
	 */
	public static Map getInstance(int i) {
		if (gameMap == null)
			gameMap = new Map(i);

		return gameMap;
	}// end getInstance

	/*
	 * readObject For serializing
	 */
	private void readObject(ObjectInputStream ois) throws IOException,
			ClassNotFoundException {
		ois.defaultReadObject();
		gameMap = this;
	}

	/*
	 * readResolve For serializing
	 */
	private Object readResolve() {
		return gameMap;
	}

	/*
	 * newMap makes a new instance of the map
	 */
	public Map newMap() {
		gameMap = null;
		return getInstance(0);
	}

	/*
	 * getContinentBonuses Collects all of the continent bonuses that one
	 * particular player has earned on this map
	 */
	public int getContinentBonuses(Player player) {
		int totalBonus = 0;

		totalBonus += blue.payOwnerBonus(player);
		totalBonus += green.payOwnerBonus(player);
		totalBonus += pink.payOwnerBonus(player);
		totalBonus += orange.payOwnerBonus(player);
		totalBonus += red.payOwnerBonus(player);
		totalBonus += black.payOwnerBonus(player);
		totalBonus += yellow.payOwnerBonus(player);

		return totalBonus;
	}// end getContinentBonuses

	/*
	 * getContinentOwnerAsStrings return a string containing all of the
	 * continents, their continent bunses and their owners (if any). For the
	 * gameLog
	 */
	public String[] getContinentOwnersAsStrings() {
		String[] allStrings = new String[7];

		allStrings[0] = blue.getBonus() + " units: " + blue.toString();
		allStrings[1] = green.getBonus() + " units: " + green.toString();
		allStrings[2] = orange.getBonus() + " units: " + orange.toString();
		allStrings[3] = pink.getBonus() + " units: " + pink.toString();
		allStrings[4] = red.getBonus() + " units: " + red.toString();
		allStrings[5] = black.getBonus() + " units: " + black.toString();
		allStrings[6] = yellow.getBonus() + " units: " + yellow.toString();

		return allStrings;
	}

	/*
	 * fillCountries construct all 50 countries inside of the game
	 */
	private void fillCountries(int j) {
		// this method is going to suck
		countries[0] = new Country("The Wall", 6.5, 3, blue);
		countries[1] = new Country("Skagos", 10, 2.5, blue);
		countries[2] = new Country("Wolfswood", 3, 6.75, blue);
		countries[3] = new Country("Winterfell", 6, 7.5, blue);
		countries[4] = new Country("The Rills", 1.5, 9, blue);
		countries[5] = new Country("The Neck", 5.5, 11.25, blue);
		countries[6] = new Country("The Flint Cliffs", 2.5, 13.75, blue);
		countries[7] = new Country("The Grey Cliffs", 7.5, 5.5, blue);
		countries[8] = new Country("The Vale", 6.5, 16, green);
		countries[9] = new Country("Riverlands", 3, 18.5, green);
		countries[10] = new Country("Iron Islands", 1.5, 17, green);
		countries[11] = new Country("Westerlands", 3, 21.25, green);
		countries[12] = new Country("Crownlands", 6.25, 20.5, green);
		countries[13] = new Country("The Reach", 5.25, 23.5, orange);
		countries[14] = new Country("Shield Lands", 2.5, 25, orange);
		countries[15] = new Country("Whispering Sound", 1.75, 28.5, orange);
		countries[16] = new Country("Storm Lands", 7.75, 25, orange);
		countries[17] = new Country("Red Mountains", 4.75, 28.5, orange);
		countries[18] = new Country("Dorne", 6, 30.5, orange);
		countries[19] = new Country("Braavosi Coastland", 13.5, 16.5, pink);
		countries[20] = new Country("Andalos", 13.25, 20.5, pink);
		countries[21] = new Country("Hills of Norvos", 15, 19.25, pink);
		countries[22] = new Country("Rhoyne Lands", 17.25, 20.25, pink);
		countries[23] = new Country("Forrest of Qohor", 19.25, 20, pink);
		countries[24] = new Country("The Golden Fields", 15.25, 25.75, pink);
		countries[25] = new Country("The Disputed Lands", 13.5, 29, pink);
		countries[26] = new Country("Rhoynian Veld", 18.5, 24, red);
		countries[27] = new Country("Sar Mell", 18, 29, red);
		countries[28] = new Country("Western Waste", 20, 27, red);
		countries[29] = new Country("Sea of Sighs", 21.5, 29.75, red);
		countries[30] = new Country("Elyria", 22.75, 33, red);
		countries[31] = new Country("Valyria", 22, 37.5, red);
		countries[32] = new Country("Sarnor", 22, 17.5, yellow);
		countries[33] = new Country("Parched Fields", 23.25, 23, yellow);
		countries[34] = new Country("Abandoned Lands", 25.5, 18.75, yellow);
		countries[35] = new Country("Western Grass Sea", 26.5, 24.75, yellow);
		countries[36] = new Country("Kingdoms of the Jfeqevron", 28.75, 19.5,
				yellow);
		countries[37] = new Country("Eastern Grass Sea", 31, 23.5, yellow);
		countries[38] = new Country("The Footprint", 31, 16, yellow);
		countries[39] = new Country("Vaes Dothrak", 32.25, 19.5, yellow);
		countries[40] = new Country("Realms of Jhogrvin", 36.7, 16.5, yellow);
		countries[41] = new Country("Ibben", 32.80, 8, yellow);
		countries[42] = new Country("Painted Mountains", 24.25, 27.75, black);
		countries[43] = new Country("Slaver's Bay", 28, 30.25, black);
		countries[44] = new Country("Lhazar", 30, 28.75, black);
		countries[45] = new Country("Samyrian Hills", 34.5, 24.75, black);
		countries[46] = new Country("Bayasabhad", 34, 29.25, black);
		countries[47] = new Country("Ghiscar", 27.75, 34.25, black);
		countries[48] = new Country("The Red Waste", 30.1, 31.75, black);
		countries[49] = new Country("Qarth", 34.35, 32.75, black);

		addAllNeighbors();
		if (j == 0) {
			updateAllButtonSizes();
			for (int i = 0; i < countries.length; i++) {
				countries[i].addObserver(riskGUI.getBoardPanel());
			}
		}

	}// end fillCountries

	/*
	 * updateAllButtonSizes updates all of the manually set sizes for each
	 * country button
	 */
	public void updateAllButtonSizes() {
		// public void changeButtonSize(double width, double height)
		countries[0].changeButtonSize(2, 1.75);
		countries[1].changeButtonSize(0, 0);
		countries[2].changeButtonSize(2.5, 1.5);
		countries[3].changeButtonSize(3, 2.5);
		countries[4].changeButtonSize(3, 3);
		countries[5].changeButtonSize(1.5, 2.75);
		countries[6].changeButtonSize(3, 0);
		countries[7].changeButtonSize(0, 1.5);
		countries[8].changeButtonSize(4, 3);
		countries[9].changeButtonSize(2.5, 0);
		countries[10].changeButtonSize(2, 1.25);
		countries[11].changeButtonSize(3, 2);
		countries[12].changeButtonSize(3, 2);
		countries[13].changeButtonSize(0, 3);
		countries[14].changeButtonSize(2.5, 2);
		countries[15].changeButtonSize(2.5, 2);
		countries[16].changeButtonSize(2.25, 3);
		countries[17].changeButtonSize(2.25, 1.75);
		countries[18].changeButtonSize(4, 2);
		countries[19].changeButtonSize(2, 2.5);
		countries[20].changeButtonSize(1.75, 4);
		countries[21].changeButtonSize(2, 2);
		countries[22].changeButtonSize(1.5, 3.75);
		countries[23].changeButtonSize(3, 3);
		countries[24].changeButtonSize(2.5, 3);
		countries[25].changeButtonSize(3.5, 2);
		countries[26].changeButtonSize(3, 2);
		countries[27].changeButtonSize(2.5, 2);
		countries[28].changeButtonSize(4, 2);
		countries[29].changeButtonSize(1.5, 1.5);
		countries[30].changeButtonSize(1.5, 2.5);
		countries[31].changeButtonSize(0, 2.5);
		countries[32].changeButtonSize(3, 3);
		countries[33].changeButtonSize(0, 3);
		countries[34].changeButtonSize(0, 4);
		countries[35].changeButtonSize(0, 2);
		countries[36].changeButtonSize(3, 3);
		countries[37].changeButtonSize(4, 1.75);
		countries[38].changeButtonSize(5, 2.5);
		countries[39].changeButtonSize(3.5, 2.5);
		countries[40].changeButtonSize(2.5, 3);
		countries[41].changeButtonSize(3, 1.5);
		countries[42].changeButtonSize(0, 1.5);
		countries[43].changeButtonSize(2, 2);
		countries[44].changeButtonSize(3.75, 1.5);
		countries[45].changeButtonSize(0, 3);
		countries[46].changeButtonSize(3.5, 2.25);
		countries[47].changeButtonSize(2.75, 2.5);
		countries[48].changeButtonSize(4, 2);
		countries[49].changeButtonSize(3, 3.5);

	}

	/*
	 * addAllNeighbors Manually adds every neighbor for every country, creating
	 * an adjacency list of all of the countries. This method calls a helper
	 * method in order to save me on typing. It passes the indices of the
	 * neighbors in the countries array
	 */
	private void addAllNeighbors() {
		int index = 0;
		// Adding to the wall
		int[] wall = { 1, 7, 2 };
		addMyNeighbors(index++, wall);
		// Skagos
		int[] skags = { 0, 7 };
		addMyNeighbors(index++, skags);
		// Wolf
		int[] wolf = { 0, 7, 3, 5, 4 };
		addMyNeighbors(index++, wolf);
		// winterfell
		int[] winter = { 7, 2, 4, 5 };
		addMyNeighbors(index++, winter);
		// rills
		int[] rill = { 2, 5, 6 };
		addMyNeighbors(index++, rill);
		// neck
		int[] neck = { 8, 6, 4, 2, 3 };
		addMyNeighbors(index++, neck);
		// flint
		int[] flint = { 5, 8, 9, 10, 4 };
		addMyNeighbors(index++, flint);
		// grey
		int[] grey = { 2, 0, 1, 3 };
		addMyNeighbors(index++, grey);
		// vale
		int[] vale = { 5, 6, 9, 12 };
		addMyNeighbors(index++, vale);
		// riverlands
		int[] river = { 6, 10, 8, 12, 11, 40 };
		addMyNeighbors(index++, river);
		// iron islands
		int[] iron = { 6, 9 };
		addMyNeighbors(index++, iron);
		// westerlands
		int[] wester = { 9, 12, 13, 14 };
		addMyNeighbors(index++, wester);
		// crownlands
		int[] crown = { 8, 9, 11, 13, 16, 20 };
		addMyNeighbors(index++, crown);
		// reach
		int[] reach = { 11, 12, 16, 17, 15, 14 };
		addMyNeighbors(index++, reach);
		// shield lands
		int[] shield = { 11, 13, 17, 15 };
		addMyNeighbors(index++, shield);
		// whispering sound
		int[] whisper = { 14, 13, 17 };
		addMyNeighbors(index++, whisper);
		// storm lands
		int[] storm = { 12, 13, 17, 20 };
		addMyNeighbors(index++, storm);
		// red mountains
		int[] red = { 14, 13, 16, 18, 15 };
		addMyNeighbors(index++, red);
		// dorne
		int[] dorne = { 17, 25 };
		addMyNeighbors(index++, dorne);
		// braavosi coast
		int[] braav = { 21, 20 };
		addMyNeighbors(index++, braav);
		// andalos
		int[] andalos = { 12, 16, 19, 21, 24 };
		addMyNeighbors(index++, andalos);
		// hills of norvos
		int[] hills = { 19, 20, 24, 22 };
		addMyNeighbors(index++, hills);
		// rhoyne lands
		int[] rhoyne = { 21, 24, 26, 23 };
		addMyNeighbors(index++, rhoyne);
		// forrest of qohor
		int[] qohor = { 22, 26, 28, 33, 32 };
		addMyNeighbors(index++, qohor);
		// golden fields
		int[] gold = { 20, 21, 22, 26, 27, 25 };
		addMyNeighbors(index++, gold);
		// disputed lands
		int[] dispute = { 18, 24, 27 };
		addMyNeighbors(index++, dispute);
		// rhoynian veld
		int[] rhoynian = { 24, 22, 23, 33, 28, 27 };
		addMyNeighbors(index++, rhoynian);
		// sar mell
		int[] mell = { 25, 24, 26, 28, 29 };
		addMyNeighbors(index++, mell);
		// western waste
		int[] westWaste = { 27, 26, 33, 42, 30, 29 };
		addMyNeighbors(index++, westWaste);
		// sea of sighs
		int[] sighs = { 27, 28, 30, 31 };
		addMyNeighbors(index++, sighs);
		// elyria
		int[] elyria = { 28, 42, 31, 29 };
		addMyNeighbors(index++, elyria);
		// valyria
		int[] valyria = { 29, 30 };
		addMyNeighbors(index++, valyria);
		// sarnor
		int[] sarnor = { 23, 33, 34 };
		addMyNeighbors(index++, sarnor);
		// parched fields
		int[] parched = { 23, 32, 34, 35, 42, 28, 26 };
		addMyNeighbors(index++, parched);
		// abandoned
		int[] abandoned = { 32, 33, 35, 36 };
		addMyNeighbors(index++, abandoned);
		// western grass sea
		int[] westGrass = { 33, 36, 37, 44, 42, 34 };
		addMyNeighbors(index++, westGrass);
		// kingdoms of jfeqevron
		int[] jfeqv = { 38, 39, 37, 35, 34 };
		addMyNeighbors(index++, jfeqv);
		// eastern grass sea
		int[] eastGrass = { 36, 39, 45, 44, 35 };
		addMyNeighbors(index++, eastGrass);
		// footprint
		int[] footprint = { 40, 39, 36, 41 };
		addMyNeighbors(index++, footprint);
		// vaes dothrak
		int[] dothrak = { 36, 38, 40, 45, 37 };
		addMyNeighbors(index++, dothrak);
		// realms of jhogrvin
		int[] jhogr = { 39, 38, 9 };
		addMyNeighbors(index++, jhogr);
		// ibben
		int[] ibben = { 38 };
		addMyNeighbors(index++, ibben);
		// painted mountains
		int[] painted = { 28, 33, 35, 44, 43, 30 };
		addMyNeighbors(index++, painted);
		// slaver's bay
		int[] slavers = { 42, 44, 48, 47 };
		addMyNeighbors(index++, slavers);
		// lhazar
		int[] lhazar = { 42, 35, 37, 45, 46, 49, 48, 43 };
		addMyNeighbors(index++, lhazar);
		// samyrian hills
		int[] samyrian = { 37, 39, 46, 44 };
		addMyNeighbors(index++, samyrian);
		// bayasabhad
		int[] bayasb = { 44, 45, 49, 48 };
		addMyNeighbors(index++, bayasb);
		// ghiscar
		int[] ghiscar = { 43, 48 };
		addMyNeighbors(index++, ghiscar);
		// red waste
		int[] redWaste = { 47, 43, 44, 46, 49 };
		addMyNeighbors(index++, redWaste);
		// qarth
		int[] qarth = { 48, 46 };
		addMyNeighbors(index++, qarth);
	}// end addAllNeighbors

	/*
	 * addMyNeighbors Takes an array of ints, and adds all of the countries at
	 * those indices to be neighbors of the country at the first index
	 */
	private void addMyNeighbors(int countryI, int[] neighborsI) {
		for (int i = 0; i < neighborsI.length; i++) {
			countries[countryI].addNeighbor(countries[neighborsI[i]]);
		}
	}// end addMyNeighbors

	/*
	 * Miscellaneous getters and setters
	 */
	public Country[] getCountries() {
		return countries;
	}// end getCountries

	public Map newTourneyMap() {
		gameMap = null;

		return getInstance(1);
	}

	public static ArrayList<Continent> getAllContinents() {
		return allContinents;
	}
}// end Map class
