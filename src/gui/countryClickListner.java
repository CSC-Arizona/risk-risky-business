package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//listner for country clicks
public class countryClickListner implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println(e.getActionCommand() + " pressed.");
	}

}
