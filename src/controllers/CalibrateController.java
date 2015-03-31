/*
 * Auteur: Noah Steijlen
 * Datum: /3-10-2015
 */

package controllers;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import sensors.Sensor;

public class CalibrateController extends Controller{
	
	private Sensor leftLightSensor, rightLightSensor;
	
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
		
		this.switchBackToCaller();
	}

	@Override
	public void onStop() {
		
	}
}