/*
 * Auteur: Timon van den Brink
 * Datum: 3-10-2015
 */

package sensors;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class MyRangeSensor extends Sensor{
	
	private UltrasonicSensor rangeSensor;
	
	public MyRangeSensor(SensorPort port){
		super();
		rangeSensor = new UltrasonicSensor(port); 
	}
	
	@Override
	public int getValue() {
		return rangeSensor.getDistance();
	}

}
