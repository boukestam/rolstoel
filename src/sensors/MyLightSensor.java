/*
 * Auteur: Timon van den Brink
 * Datum: 3-10-2015
 */

package sensors;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class MyLightSensor extends Sensor{
	
	private LightSensor lightSensor;
	
	public MyLightSensor(SensorPort port){
		super();
		lightSensor = new LightSensor(port);
	}
	
	//1 is black, 0 is white
	@Override
	public int getValue(){
		int percentage=lightSensor.getLightValue();
		LCD.clear(2);
		LCD.drawString("Light: "+percentage, 0, 2);
		return percentage>50?0:1;
	}
	
	@Override
	public void calibrateHigh(){
		lightSensor.calibrateHigh();
	}
	
	@Override
	public void calibrateLow(){
		lightSensor.calibrateLow();
	}

}
