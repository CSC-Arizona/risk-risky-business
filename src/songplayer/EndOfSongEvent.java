package songplayer;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * An instance of this class is needed to create an EndOfSongListener to pass as
 * an argument to the listener. This provides the programmer with the ability to
 * get the name of the audio file, the date and the time when the song is done.
 * 
 * @author mercer
 */
public class EndOfSongEvent {

	private String fileName;
	private LocalDate currentDate;
	private LocalTime currentTime;

	/**
	 * Construct a new EndOfSongEvent with the file name just finished playing
	 * and the time at which the song finished playing
	 * 
	 * @param fileName
	 *            The song that just finished playing
	 * @param currentTime
	 *            The moment at which the song finished playing
	 */
	public EndOfSongEvent(String fileName, LocalDate currentDate, LocalTime currentTime) {
		this.fileName = fileName;
		this.currentDate = currentDate;
		this.currentTime = currentTime;
	}

	/**
	 * Provide access to the name of the audio file that just finished.
	 * 
	 * @return the name of the file sent to the SongPlayer
	 */
	public String fileName() {
		return fileName;
	}

	/**
	 * Provide access to Date of this EndOfSongEvent
	 * 
	 * @return the name of the file sent to the SongPlayer
	 */
	public LocalDate finishedDate() {
		return currentDate;
	}

	/**
	 * Provide access to time of this EndOfSongEvent
	 * 
	 * @return the name of the file sent to the SongPlayer
	 */
	public LocalTime finishedTime() {
		return currentTime;
	}
}