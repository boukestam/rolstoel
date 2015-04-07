/*
 * Auteur: Bouke Stam
 * Datum: 3-10-2015
 */

package sensors;

/**
 * The interface that gets called by UpdateHandler.
 * @author Bouke Stam
 * @version 1.0
 */

public interface Updatable {
	
	/**
	 * Method that must be implemented by sub class. This update() method gets called by the Task class.
	 */
	public void update();
}
