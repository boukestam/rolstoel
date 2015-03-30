package sensors;

import java.util.ArrayList;

/**
 * A handler that repeatedly calls the "update" method of all Updateable objects
 * in a given period of time.
 * @author Bouke Stam
 * @version 1.0
 */

public class UpdateHandler extends Thread{
	
	/**
	 * Object of this class.
	 */
	private static UpdateHandler instance;
	
	/**
	 * Arraylist of all tasks that get updated.
	 */
	private ArrayList<Task> tasks;
	
	/**
	 * Initialize global variables and starts the run() method. (singleton pattern)
	 */
	private UpdateHandler(){
		tasks=new ArrayList<Task>();
		this.start();
	}
	
	/**
	 * Add an updateable that will be called in a given interval. If
	 * instance is not initialized than create the object.
	 * @param updatable the object that gets called
	 * @param interval the wait time before object gets called
	 */
	public static void registerUpdatable(Updatable updatable,int interval){
		if(instance==null){
			instance=new UpdateHandler();
		}
		
		instance.addTask(new Task(updatable,interval));
	}
	
	/**
	 * Add a task to the List tasks
	 * @param task the task that gets added
	 */
	public void addTask(Task task){
		tasks.add(task);
	}
	
	/**
	 * Updates all tasks in a given amount of time.
	 */
	@Override
	public void run(){
		while(true){
			long time=System.currentTimeMillis();
			for(Task task:tasks){
				task.update(time);
			}
			try{
				Thread.sleep(10);
			}catch(Exception e){
				
			}
		}
	}
}
