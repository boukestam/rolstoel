package sensors;

import java.util.ArrayList;

/**
 * Abstract super class that will be extended by all sensors.
 * @author Timon van den Brink
 * @version 1.0
 */

public abstract class Sensor implements Updatable{
	
	/**
	 * The last saved value.
	 */
	private int previousValue;
	
	/**
	 * All listeners of this sensor that will get updates of the sensor state.
	 */
	private ArrayList<SensorListener> listeners;
	
	/**
	 * Constructor initializing variables.
	 */
	public Sensor(){
		listeners = new ArrayList<SensorListener>();
	}
	
	/**
	 * Add listener to the "listeners" ArrayList.
	 * @param listener the sensor listener object that wants to listen to this sensor.
	 */
	public void addListener(SensorListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Gives the value that the sensor measures. This method must be overwritten by the subclasses.
	 * @return the value that the children sensors measure
	 */
	public abstract int getValue();
	
	/**
	 * Overwritten method of the Updateable class called from the UpdateHandler. 
	 * Updates the value of the sensor and tell all SensorListener's that the value has changed.
	 */
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
	
	/**
	 * Calibrates the sensor's highest value. This method can be overwritten but is not a must.
	 */
	public void calibrateHigh(){
		
	}
	
	/**
	 * Calibrates the sensor's lowest value. This method can be overwritten but is not a must.
	 */
	public void calibrateLow(){
		
	}
}
