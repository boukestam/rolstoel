package sensors;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

/**
 * The class that communicates with the range sensor.
 * @author Timon van den Brink
 * @version 1.0
 */

public class MyRangeSensor extends Sensor{
	
	/**
	 * The object that is able to cummunicate with the UltrasonicSensor.
	 */
	private UltrasonicSensor rangeSensor;
	
	/**
	 * Initializes the sensor and the variables it uses.
	 * @param port the port where the RangeSensor operates on.
	 */
	public MyRangeSensor(SensorPort port){
		super();
		rangeSensor = new UltrasonicSensor(port); 
	}
	
	/**
	 * Returns the measured distance between the rangeSensor and an physical prop.
	 */
	@Override
	public int getValue() {
		return rangeSensor.getDistance();
	}

}
