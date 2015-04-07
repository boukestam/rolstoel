package controllers;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import sensors.Sensor;

/**
 * This controller calibrates the light sensors.
 * You need to put the sensors first on the white line and then on the black line.
 * @author Noah Steijlen
 * @author Scott Mackay
 * @author Bouke Stam
 * @author Timon van den Brink
 * @version 1.0
 */

public class CalibrateController extends Controller{
	
	/**
	 * Objects used to find out which sensor is which.
	 */
	private Sensor leftLightSensor, rightLightSensor;
	
	/**
	 * Initialized the sensors and listens to the sensors.
	 * @param leftLightSensor object used for the left light sensor.
	 * @param rightLightSensor object used for the right light sensor.
	 */
	public CalibrateController(Sensor leftLightSensor,Sensor rightLightSensor){
		leftLightSensor.addListener(this);
		rightLightSensor.addListener(this);
		this.leftLightSensor=leftLightSensor;
		this.rightLightSensor=rightLightSensor;
	}

	@Override
	public void control() {
		
	}
	
	@Override
	public void valueChanged(Sensor source, int value) {
		
	}
	
	@Override
	public void onStart() {
		LCD.clear(1);
		LCD.drawString("Zet op WIT", 0, 1);
		Button.waitForAnyPress();
		leftLightSensor.calibrateHigh();
		rightLightSensor.calibrateHigh();
		LCD.clear(1);
		LCD.drawString("Zet op ZWART", 0, 1);
		Button.waitForAnyPress();
		leftLightSensor.calibrateLow();
		rightLightSensor.calibrateLow();
		LCD.clear(1);
		
		this.switchBackToCaller();
	}

	@Override
	public void onStop() {
		
	}
}