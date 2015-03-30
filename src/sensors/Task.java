/*
 * Auteur: Bouke Stam
 * Datum: 3-10-2015
 */

package sensors;

public class Task {

	private int interval;
	private long lastExecuted;
	private Updatable updatable;
	
	public Task(Updatable updatable,int interval){
		this.updatable=updatable;
		this.interval=interval;
	}
	
	public void update(long time){
		if(lastExecuted+interval<=time){
			execute();
			lastExecuted=time;
		}
	}
	
	public void execute(){
		updatable.update();
	}
}