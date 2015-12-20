package de.tu_darmstadt.gdi1.samegame.tests.adapter;

import java.text.DateFormat;
import java.util.Date;

import Controller.HighscoreEntry;


/**
 * This is the test adapter for the second extended stage of completion.
 * Implement all method stubs in order for the tests to work.
 * <br><br>
 * <i>Note:</i> This test adapter inherits from the first extended test adapter
 * 
 * @see SameGameTestAdapterMinimal
 * @see SameGameTestAdapterExtended1
 * 
 * @author Jonas Marczona
 * @author Manuel Pütz
 */
public class SameGameTestAdapterExtended2 extends SameGameTestAdapterExtended1 {
	
	/**
	 * Use this constructor to initialize everything you need.
	 */
	public SameGameTestAdapterExtended2() {
		super();
	}

	/**
	 * Undo the last action of the current level.
	 * The state of the game shall be the same as before the last action. Action is here defined
	 * as "removing stones". 
	 * Do nothing if there is no action to undo in the current level.
	 */
	public void undo() {
		controller.keyUndoPressed();
		//TODO fill stub.
	}

	/**
	 * Redo the last action. The state of the game shall be the same as before the last "undo". Do nothing if there is
	 * no action to redo.
	 */
	public void redo() {
		controller.keyRedoPressed();
		//TODO fill stub.
	}

	
	
	/**
	 * Add a highscore entry to the highscore with the given playername, time, timestamp and points.
	 * 
	 * @param playername
	 *            the name which shall appear in the highscore
	 * @param rem_time
	 *            remaining time in seconds until target time is reached (which can be negative)
	 * @param creation_date
	 *            timestamp of when the game was finished
	 * @param points
	 *            the score which was achieved
	 * 
	 * @see #getDateAtHighscorePosition(int)
	 */
	public void addHighscoreEntry(String playername, double rem_time, Date creation_date, double points ) {
		HighscoreEntry entry = new HighscoreEntry(playername, points, rem_time, creation_date);
		
		controller.getLevelHandling().getHighscores().addHighscoreEntry(entry);
	}
	
	/** 
	 * Return the number of highscore entries in your highscore store.
	 *  
	 * @return the number of highscore entries
	 */
	public int getHighscoreCount() {
		return controller.getLevelHandling().getHighscores().size();
	}
	
	/** 
	 * Clear the highscore store and delete all entries.
	 */
	public void resetHighscore() {
		controller.getLevelHandling().clearHighscores();
	}
	
	/** 
	 * Get the playername of a highscore entry at a given position.
	 * <strong>Note:</strong> The position counting starts at zero. The first entry should contain the <i>best</i> result.
	 * <p>
	 * See the specification in the task assignment for a definition of 'best' in the highscore.<br>
	 * </p>
	 * @param position the position of the highscore entry in the highscore 
	 * store 
	 * @return the playername of the highscore entry at the specified position
	 * or null if the position is invalid 
	 */
	public String getPlayernameAtHighscorePosition(int position) {
		if (controller.getLevelHandling().getHighscores().getEntry(position) == null)
		 return null;
		else 
		return controller.getLevelHandling().getHighscores().getEntry(position).getName();
	}
	
	/** 
	 * Get the remaining time of a highscore entry at a given position.
	 * <strong>Note:</strong> The position counting starts at zero. The first entry should contain the <i>best</i> result.
	 * <p>
	 * See the specification in the task assignment for a definition of 'best' in the highscore.<br>
	 * </p>
	 * @param position the position of the highscore entry in the highscore 
	 * store 
	 * @return the time of the highscore entry at the specified position
	 * or -1 if the position is invalid 
	 */
	public double getTimeAtHighscorePosition(int position) {
		if (controller.getLevelHandling().getHighscores().getEntry(position) == null)
			 return -1;
			else 
			return controller.getLevelHandling().getHighscores().getEntry(position).getRemainingTime();
			}

	/**
	 * Get the points of a highscore entry at a given position.
	 * <strong>Note:</strong> The position counting starts at zero. The first entry should contain the <i>best</i> result.
	 * <p>
	 * See the specification in the task assignment for a definition of 'best' in the highscore.<br>
	 * </p>
	 * @param position the position of the highscore entry in the highscore 
	 * store 
	 * @return the points of the highscore entry at the specified position
	 * or -1 if the position is invalid 
	 */
	public double getPointsAtHighscorePosition(int position) {
		if (controller.getLevelHandling().getHighscores().getEntry(position) == null)
			 return -1;
			else 
			return controller.getLevelHandling().getHighscores().getEntry(position).getPoints();
			}

	/**
	 * Get the date of a highscore entry at a given position. <strong>Note:</strong> The position counting starts at
	 * zero. The first entry should contain the <i>best</i> result.
	 * <p>
	 * See the specification in the task assignment for a definition of 'best' in the highscore.<br>
	 * </p>
	 * 
	 * Hint: in a level file the date is saved as string - to retrieve the date-object out of this string you should
	 * need only a few lines of code. Take a look to the default java classes, something near {@link DateFormat}.
	 * 
	 * @param position
	 *            the position of the highscore entry in the highscore store
	 * @return the date of the highscore entry at the specified position or null if the position is invalid
	 */
	public Date getDateAtHighscorePosition(int position) {
		if (controller.getLevelHandling().getHighscores().getEntry(position) == null)
			 return null;
			else 
			return controller.getLevelHandling().getHighscores().getEntry(position).getDate();
			}

}
