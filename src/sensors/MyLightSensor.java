package sensors;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

/**
 * This class lets the robot communicate with the light sensor.
 * @author Timon van den Brink
 * @version 1.0
 */

public class MyLightSensor extends Sensor{
	
	/**
	 * The light sensor as an object.
	 */
	private LightSensor lightSensor;
	
	/**
	 * The constructor of the light sensor which defines the hardware port of the sensor.
	 * @param port the port of the light sensor.
	 */
	public MyLightSensor(SensorPort port){
		super();
		lightSensor = new LightSensor(port);
	}
	
	/**
	 * Returns the value 1 if the sensor detects black and returns 0 if the sensor detects white.
	 */
	@Override
	public int getValue(){
		int percentage=lightSensor.getLightValue();
		LCD.clear(2);
		LCD.drawString("Light: "+percentage, 0, 2);
		return percentage>50?0:1;
	}
	
	/**
	 * Calibrates the measured value as a high value.
	 */
	@Override
	public void calibrateHigh(){
		lightSensor.calibrateHigh();
	}
	
	/**
	 * Calibrates the measured value as a low value.
	 */
	@Override
	public void calibrateLow(){
		lightSensor.calibrateLow();
	}

}
