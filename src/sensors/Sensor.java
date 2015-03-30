/*
 * Auteur: Timon van den Brink
 * Datum: 3-10-2015
 */

package sensors;

import java.util.ArrayList;

public abstract class Sensor implements Updatable{

	private int previousValue;
	private ArrayList<SensorListener> listeners;
	
	public Sensor(){
		listeners = new ArrayList<SensorListener>();
	}
	
	public void addListener(SensorListener listener){
		listeners.add(listener);
	}
	
	public abstract int getValue();
	
	@Override
	public void update(){
		int value = getValue();
		if(previousValue != value){
			for(SensorListener listener:listeners){
				listener.valueChanged(this, value);
			}
			previousValue = value;
		}
	}
	
	public void calibrateHigh(){
		
	}
	public void calibrateLow(){
		
	}
}
