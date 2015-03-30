/*
 * Auteur: Noah Steijlen
 * Datum: 3-10-2015
 */

package controllers;

import lejos.nxt.LCD;
import driver.Driver;
import sensors.Sensor;

public class AvoidController extends Controller{
	
	private Sensor leftLightSensor, rightLightSensor, rangeSensor;
	
	private boolean justStarted=true;
	
	private int threshold=10;	//cm
	private int distance=20;	//cm
	
	public AvoidController(Sensor leftLightSensor,Sensor rightLightSensor,Sensor rangeSensor){
		leftLightSensor.addListener(this);
		rightLightSensor.addListener(this);
		rangeSensor.addListener(this);
		this.leftLightSensor=leftLightSensor;
		this.rightLightSensor=rightLightSensor;
		this.rangeSensor=rangeSensor;
	}
	
	@Override
	public void control(){
		if(justStarted){
			Driver.turnAngle(90,100);
			Driver.turnRangeSensor(-90);
			Driver.driveForward(200);
			justStarted=false;
		}
	}

	@Override
	public void valueChanged(Sensor source, int value){
		if(super.isRunning()){
			if(!justStarted){
				if(source==rangeSensor){
					if(value>=distance+threshold){
						Driver.turnLeft(50);
					}else if(value<=distance-threshold){
						Driver.turnRight(50);
					}else{
						Driver.driveForward(200);
					}
					LCD.clear(5);
					LCD.drawString("Distance: "+value, 0, 5);
				}else if((source==leftLightSensor&&value==1)||(source==rightLightSensor&&value==1)){
					Driver.turnRangeSensor(90);
					this.switchBackToCaller();
				}
			}
		}
	}
}