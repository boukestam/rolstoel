/*
 * Auteur: Noah Steijlen
 * Datum: 3-10-2015
 */

package controllers;

import driver.Driver;
import sensors.Sensor;

public class AvoidController extends Controller{
	
	private Sensor leftLightSensor, rightLightSensor, rangeSensor;
	
	private boolean justStarted=true;
	private boolean seenOnce=false;
	
	private int threshold=10;	//cm
	private int distance=20;	//cm
	
	private int SPEED=10;
	
	private String lastDirection;
	private boolean lostSight=false;
	private boolean foundLine=false;
	
	private boolean blackRight=false;
	
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
		
	}

	@Override
	public void valueChanged(Sensor source, int value){
		if(super.isRunning()){
			if(!justStarted){
				if(source==rangeSensor){
					if(value<=distance){
						seenOnce=true;
					}
					if(!foundLine){
						if(!seenOnce){
							lastDirection="left";
							Driver.turnSmoothLeft(0,SPEED);
						}else{
							if(value>=distance+threshold){
								if(!lostSight){
									switch(lastDirection){
									case "left":
										Driver.turnSmoothRight(0,SPEED);
										lastDirection="right";
										break;
									case "right":
										Driver.turnSmoothLeft(0,SPEED);
										lastDirection="left";
										break;
									case "straight":
										Driver.turnSmoothLeft(15,SPEED);
										lastDirection="left";
										break;
									}
								}
								lostSight=true;
							}else{
								lostSight=false;
								Driver.turnSmoothRight(20,SPEED);
								lastDirection="straight";
							}
						}
					}
				}else if(source==leftLightSensor){
					if(value==1){
						if(!blackRight){
							//only left sensor sees black
							this.switchBackToCaller();
						}
					}
				}else if(source==rightLightSensor){
					if(value==1){
						//right sensor sees black
						blackRight=true;
						Driver.turnSmoothRight(2,2);
						foundLine=true;
					}else{
						blackRight=false;
					}
				}
			}
		}
	}

	@Override
	public void onStart() {
		Driver.turnAngle(90,100);
		Driver.resetRangeSensorTachoCount();
		Driver.turnRangeSensor(-90);
		Driver.driveForward(20);
		justStarted=false;
		seenOnce=false;
		foundLine=false;
		lostSight=false;
		lastDirection="left";
		Driver.turnSmoothLeft(0,5);
	}

	@Override
	public void onStop() {
		Driver.stop();
		Driver.turnRangeSensor(90);
		justStarted=true;
	}
}