/*
 * Auteur: Noah Steijlen
 * Datum: 3-10-2015
 */

package controllers;

import driver.Driver;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import sensors.MyColorSensor;
import sensors.MyLightSensor;
import sensors.MyRangeSensor;
import sensors.Sensor;
import sensors.UpdateHandler;

public class DestinationController extends Controller{

	private Sensor leftLightSensor, rightLightSensor, rangeSensor;
	
	private Controller avoidController,calibrateController;
	
	private boolean blackLeft=false,blackRight=false;
	// -1 == LeftSensor   and    0 == RightSensor
	private String lastDetectedSensor="none";
	
	private int SPEED=15;
	private int TURN_SPEED=10;
	
	private final int INIT_TURN_RADIUS=20;
	private final int STRAIGHT_TURN_RADIUS=80;
	private int turnRadius=INIT_TURN_RADIUS;
	
	public DestinationController(){
		this.leftLightSensor=new MyColorSensor(SensorPort.S3);
		this.rightLightSensor=new MyLightSensor(SensorPort.S1);
		this.rangeSensor=new MyRangeSensor(SensorPort.S2);
		
		UpdateHandler.registerUpdatable(leftLightSensor, 50);
		UpdateHandler.registerUpdatable(rightLightSensor, 50);
		UpdateHandler.registerUpdatable(rangeSensor, 100);
		
		leftLightSensor.addListener(this);
		rightLightSensor.addListener(this);
		rangeSensor.addListener(this);
		
		avoidController=new AvoidController(leftLightSensor, rightLightSensor, rangeSensor);
		calibrateController=new CalibrateController(leftLightSensor, rightLightSensor);
		
		calibrateController.switchToController(this);
	}

	@Override
	public void control() {
		
	}

	@Override
	public void valueChanged(Sensor source, int value) {
		if(super.isRunning()){
			if(source==rangeSensor){
				//if range distance is less then 20 cm
				if(value<20){
					avoidController.switchToController(this);
				}
			}
			
			if(source==leftLightSensor){
				if(value==1){
					blackLeft=true;
				}else{
					blackLeft=false;
				}
			}else if(source==rightLightSensor){
				if(value==1){
					blackRight=true;
				}else{
					blackRight=false;
				}
			}
			
			// Left and Right sensor detecting the black line
			if(blackLeft&&blackRight){
				if(lastDetectedSensor.equals("left")){
					Driver.turnSmoothRight(turnRadius,TURN_SPEED);
				}else{
					Driver.turnSmoothLeft(turnRadius,TURN_SPEED);
				}
				turnRadius-=5;
				if(turnRadius<0){
					turnRadius=0;
				}
			}
			// Left sensor detecting the black line
			else if(blackLeft){
				Driver.turnSmoothLeft(STRAIGHT_TURN_RADIUS,SPEED);
				lastDetectedSensor="left";
				turnRadius=INIT_TURN_RADIUS;
			}
			// Right sensor detecting the black line
			else if(blackRight){
				Driver.turnSmoothRight(STRAIGHT_TURN_RADIUS,SPEED);
				lastDetectedSensor="right";
				turnRadius=INIT_TURN_RADIUS;
			}
			// Both sensors not detecting the black line
			else{
				if(lastDetectedSensor.equals("left")){
					Driver.turnSmoothLeft(turnRadius,TURN_SPEED);
				}else{
					Driver.turnSmoothRight(turnRadius,TURN_SPEED);
				}
				turnRadius-=5;
				if(turnRadius<0){
					turnRadius=0;
				}
			}
		}
	}
}
