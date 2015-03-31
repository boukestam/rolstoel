
package controllers;

import sensors.Sensor;
import sensors.SensorListener;

/**
 * This controller lets other controllers switch between each other, 
 * it can turn controllers on or off 
 * and it calls the control method of the currently running controllers.
 * @author Scott Mackay
 * @version 1.0
 */

public abstract class Controller extends Thread implements SensorListener{
	
	/**
	 * the current caller object.
	 */
	private Controller caller;
	/**
	 * determines if the controller's controller method is called.
	 */
	private boolean isRunning;
	
	/**
	 * Pauses the caller and turns on the controller.
	 * @param caller lets the provided caller pause.
	 */
	public void switchToController(Controller caller){
		caller.stopController();
		this.caller=caller;
		this.startController();
	}
	
	/**
	 * Starts the thread for controller.
	 */
	public void startController(){
		this.onStart();
		if(!this.isAlive()){
			this.start();
		}
		this.setRunning(true);
	}
	
	/**
	 * Stops the thread for controller.
	 */
	public void stopController(){
		this.onStop();
		this.setRunning(false);
	}
	
	/**
	 * Pauses the controller and switches back to the current caller.
	 */
	public void switchBackToCaller(){
		this.stopController();
		caller.startController();
		caller=null;
	}
	
	/**
	 * Switches the isRunning boolean to the provided argument.
	 * @param state the boolean that isRunning gets set to.
	 */
	public void setRunning(boolean state){
		this.isRunning=state;
	}
	
	/**
	 * Returns the isRunning variable, which is true if the thread is supposed to run and false otherwise.
	 * @return the isRunning boolean.
	 */
	public boolean isRunning(){
		return this.isRunning;
	}
	
	/**
	 * Calls the control method of the current caller if isRunning is true.
	 */
	public void run(){
		while(true){
			if(this.isRunning()){
				control();
			}else{
				try{
					Thread.sleep(100);
				}catch(Exception e){}
			}
		}
	}
	
	/**
	 * This is where sleeping should be done if needed
	 */
	public abstract void control();
	
	/**
	 * This is where the given sensor (source) gives it new value.
	 */
	public abstract void valueChanged(Sensor source,int value);
	
	/**
	 * Gets called when controller is (re)started.
	 */
	public abstract void onStart();
	
	/**
	 * Gets called when controller is stopped
	 */
	public abstract void onStop();

}