package controllers;

import driver.Driver;
import sensors.Sensor;

/**
 * This controller need to drive around an obstacle blocking the path.
 * @author Noah Steijlen
 * @author Scott Mackay
 * @author Bouke Stam
 * @author Timon van den Brink
 * @version 1.0
 */

public class AvoidController extends Controller{
	
	/**
	 * Objects used to find out which sensor is which.
	 */
	private Sensor leftLightSensor, rightLightSensor, rangeSensor;
	
	/**
	 * Boolean that says if this controller just started. After the onStart() method is executed this boolean will be set to zero.
	 */
	private boolean justStarted=true;
	
	/**
	 * Boolean that checks if the robot has already seen the obstacle before.
	 */
	private boolean seenOnce=false;
	
	/**
	 * The deviation of the distance where the robot tries to stay.
	 */
	private int threshold=10;	//cm
	
	/**
	 * The distance where the robot tries to stay.
	 */
	private int distance=20;	//cm
	
	/**
	 * The speed of the robot driving.
	 */
	private final int SPEED=10;
	
	/**
	 * The last remembered direction in which the robot was driving.
	 */
	private String lastDirection;
	
	/**
	 * Tells if the robot is not detecting the obstacle.
	 */
	private boolean lostSight=false;
	
	/**
	 * Tells if the line was found.
	 */
	private boolean foundLine=false;
	
	/**
	 * Tells if the right light sensor is detecting the line.
	 */
	private boolean blackRight=false;
	
	/**
	 * Initialized the sensors and listens to the sensors.
	 * @param leftLightSensor the object of the left light sensor.
	 * @param rightLightSensor the object of the right light sensor
	 * @param rangeSensor the object of the range sensor.
	 */
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
	
	/**
	 * When the value of one of the sensors that this object is listening for has changed, 
	 * this method gets called with the new value and the sensor. In this method all logic of
	 * moving around the obstacle and finding back the line is here controlled.
	 * @source the sensor who's value has changed.
	 * @value the new value of the sensor.
	 */
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
							Driver.driveSmoothLeft(0,SPEED);
						}else{
							if(value>=distance+threshold){
								if(!lostSight){
									switch(lastDirection){
									case "left":
										Driver.driveSmoothRight(0,SPEED);
										lastDirection="right";
										break;
									case "right":
										Driver.driveSmoothLeft(0,SPEED);
										lastDirection="left";
										break;
									case "straight":
										Driver.driveSmoothLeft(15,SPEED);
										lastDirection="left";
										break;
									}
								}
								lostSight=true;
							}else{
								lostSight=false;
								Driver.driveSmoothRight(20,SPEED);
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
						Driver.driveSmoothRight(2,2);
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
		Driver.driveSmoothLeft(0,5);
	}

	@Override
	public void onStop() {
		Driver.stop();
		Driver.turnRangeSensor(90);
		justStarted=true;
	}
}