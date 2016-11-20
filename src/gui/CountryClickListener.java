package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Model.Game;
import Model.Country;
//listner for country clicks
public class countryClickListner implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Game temp = Game.getInstance(1,3);
		System.out.println(e.getActionCommand() + " pressed.");
		//step through all countries until the same name as the actionCommand, then return that country
		for(Country country : temp.getGameMap().getCountries())
		{
			if(country.getName().compareTo(e.getActionCommand()) == 0)
				temp.setSelectedCountry(country);
		}
		temp.placeArmies();
	}

}
