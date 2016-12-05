/*
 * 	Authors: 	Dylan Tobia, Abigail Dodd, Sydney Komro, Jewell Finder
 * 	File:		SoundClipPlayer.java
 * 	Purpose:	SoundClipPlayer class controls all sounds played throughout the game including pausing. 
 */

package songplayer;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

public class SoundClipPlayer extends JFrame {

	private Clip clip;
	private long clipTime;

	// Constructor
	public SoundClipPlayer() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Sound Playing");
		this.setSize(300, 200);
		this.setVisible(false);
	}

	public void startPlay() {
		try {
			File soundFile = new File("The Rains of Castamere (Instrumental.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void startTheme() {
		try {
			File soundFile = new File("Game_Of_Thrones_Official_Show_Open_HBO_.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		clipTime = clip.getMicrosecondPosition();
		clip.stop();
	}

	public void notifyPause() {
		clip.setMicrosecondPosition(clipTime);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stopTheme() {
		if(clip !=null)
			clip.stop();
	}

}