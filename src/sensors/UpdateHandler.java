/*
 * Auteur: Bouke Stam
 * Datum: 3-10-2015
 */

package sensors;

import java.util.ArrayList;

public class UpdateHandler extends Thread{

	private static UpdateHandler instance;
	
	private ArrayList<Task> tasks;
	
	private UpdateHandler(){
		tasks=new ArrayList<Task>();
		this.start();
	}
	
	public static void registerUpdatable(Updatable updatable,int interval){
		if(instance==null){
			instance=new UpdateHandler();
		}
		
		instance.addTask(new Task(updatable,interval));
	}
	
	public void addTask(Task task){
		tasks.add(task);
	}
	
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
