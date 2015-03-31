/*
 * Auteur: Scott Mackay
 * Datum: 3-10-2015
 */

package controllers;

import sensors.Sensor;
import sensors.SensorListener;

public abstract class Controller extends Thread implements SensorListener{
	
	private Controller caller;
	private boolean isRunning;

	public void switchToController(Controller caller){
		caller.stopController();
		this.caller=caller;
		this.startController();
	}
	
	public void startController(){
		this.onStart();
		if(!this.isAlive()){
			this.start();
		}
		this.setRunning(true);
	}
	
	public void stopController(){
		this.onStop();
		this.setRunning(false);
	}
	
	public void switchBackToCaller(){
		this.stopController();
		caller.startController();
		caller=null;
	}
	
	public void setRunning(boolean state){
		this.isRunning=state;
	}
	
	public boolean isRunning(){
		return this.isRunning;
	}
	
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
	
	//This is where sleeping should be done if needed
	public abstract void control();
	
	public abstract void valueChanged(Sensor source,int value);
	
	//Gets called when controller is (re)started
	public abstract void onStart();
	
	//Gets called when controller is stopped
	public abstract void onStop();

}