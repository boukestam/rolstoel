/*
 * Auteur: Timon van den Brink
 * Datum: 3-10-2015
 */

package sensors;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.SensorPort;

public class MyColorSensor extends Sensor{
	
	private ColorSensor colorSensor;
	private int _zero = 1023;
	private int _hundred = 0;
	
	public MyColorSensor(SensorPort port){
		super();
		colorSensor = new ColorSensor(port,Color.RED);
	}
	
	//1 is black, 0 is white
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
	
	@Override
	public void calibrateHigh(){
		_hundred = colorSensor.getNormalizedLightValue();
	}
	
	@Override
	public void calibrateLow(){
		_zero = colorSensor.getNormalizedLightValue();
	}
}
