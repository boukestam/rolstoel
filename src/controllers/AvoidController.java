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
	private boolean seenOnce=false;
	
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
			Driver.driveForward(20);
			justStarted=false;
			seenOnce=false;
			lastDirection="left";
			Driver.turnSmoothLeft(0,5);
		}
	}
	
	private String lastDirection;
	private boolean lostSight=false;

	@Override
	public void valueChanged(Sensor source, int value){
		if(super.isRunning()){
			if(!justStarted){
				if(source==rangeSensor){
					if(value<=distance){
						seenOnce=true;
					}
					if(!seenOnce){
						lastDirection="left";
						Driver.turnSmoothLeft(0,5);
					}else{
						if(value>=distance+threshold){
							if(!lostSight){
								switch(lastDirection){
								case "left":
									Driver.turnSmoothRight(0,5);
									lastDirection="right";
									break;
								case "right":
									Driver.turnSmoothLeft(0,5);
									lastDirection="left";
									break;
								case "straight":
									Driver.turnSmoothLeft(0,5);
									lastDirection="left";
									break;
								}
							}
							lostSight=true;
						}else{
							lostSight=false;
							Driver.turnSmoothRight(20,5);
							lastDirection="straight";
						}
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