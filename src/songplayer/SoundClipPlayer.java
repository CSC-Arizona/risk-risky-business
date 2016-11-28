package songplayer;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

// To play sound using Clip, the process need to be alive.
// Hence, we use a Swing application.
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

	public void startTheme() {
		try {
			// Open an audio input stream.
			File soundFile = new File("Game_Of_Thrones_Official_Show_Open_HBO_.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			// Get a sound clip resource.
			clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			// clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	public void pause(){
		clipTime= clip.getMicrosecondPosition();
		clip.stop();
	}
	public void notifyPause(){
		clip.setMicrosecondPosition(clipTime);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stopTheme(){
		clip.stop();
	}
	public void exitFrame(){
		
	}
	public void startPlay() {
		try {
			// Open an audio input stream.
			URL url = this.getClass().getClassLoader().getResource("Rains of Castamere instrumental version(1 Hour).");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			// Get a sound clip resource.
			Clip clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			// clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	public void stopPlay(){
		clip.stop();
	}
}