package sensors;

/**
 * This class combines variables in one object that handles the interval between updates of an updatable object.
 * @author Bouke Stam
 * @version 1.0
 */

public class Task {

	/**
	 * Time between updates.
	 */
	private int interval;
	
	/**
	 * Last time the interval has passed.
	 */
	private long lastExecuted;
	
	/**
	 * the object that must be updated.
	 */
	private Updatable updatable;
	
	/**
	 * The constructor saving the updatable object and interval between updates.
	 * @param updatable the updatable object that gets saved as global variable.
	 * @param interval the interval between a next possible update.
	 */
	public Task(Updatable updatable,int interval){
		this.updatable=updatable;
		this.interval=interval;
	}
	
	/**
	 * This method gets called to trigger an update. It also checks if the interval has been passed. If the interval is not passed this method does nothing.
	 * @param time the current time.
	 */
	public void update(long time){
		if(lastExecuted+interval<=time){
			execute();
			lastExecuted=time;
		}
	}
	
	/**
	 * Triggers thhe update() method of an updatable object.
	 */
	public void execute(){
		updatable.update();
	}
}