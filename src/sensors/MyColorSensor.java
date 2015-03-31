package sensors;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.SensorPort;

/**
 * The class that lets the robot communicate with the color sensor.
 * @author Timon van den Brink
 * @version 1.0
 */

public class MyColorSensor extends Sensor{
	
	/** 
	 * The color sensor as an object.
	 */
	private ColorSensor colorSensor;
	/** 
	 * The average value that the sensor should detect for 0%.
	 */
	private int _zero = 1023;
	/**
	 *  The average value that the sensor should detect for a 100%.
	 */
	private int _hundred = 0;

	 /**
	  * The constructor of the color sensor which defines the hardware port of the sensor.
	  * @param port which hardware port the color sensor is connected to.
	  */
	public MyColorSensor(SensorPort port){
		super();
		colorSensor = new ColorSensor(port,Color.RED);
	}
	
	/** 
	 * Returns the value 1 if the sensor detects black and returns 0 if the sensor detects white.
	 */
	@Override
	public int getValue(){
		int measured = colorSensor.getNormalizedLightValue();
		int low, high;
		if (_hundred < _zero) {
			low = _hundred;
			high = _zero;
		} else {
			high = _hundred;
			low = _zero;
		}
		int percentage = (int) ((float) (measured - low) / (float) (high - low) * 100);
		if (percentage < 0) {
			percentage = 0;
		} else if (percentage > 100) {
			percentage = 100;
		}
		LCD.clear(3);
		LCD.drawString("Color: "+percentage, 0, 3);
		return (percentage > 50 ? 0 : 1);
	}
	
	/** 
	 * Sets the average value of when the sensor is supposed to detect a 100%.
	 */
	@Override
	public void calibrateHigh(){
		_hundred = colorSensor.getNormalizedLightValue();
	}
	
	/** 
	 * Sets the average value of when the sensor is supposed to detect 0%.
	 */
	@Override
	public void calibrateLow(){
		_zero = colorSensor.getNormalizedLightValue();
	}
}
