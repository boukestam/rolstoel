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
	
	private int speed=300;
	private int turnSpeed=200;
	
	public DestinationController(){
		this.leftLightSensor=new MyColorSensor(SensorPort.S3);
		this.rightLightSensor=new MyLightSensor(SensorPort.S1);
		this.rangeSensor=new MyRangeSensor(SensorPort.S2);
		
		UpdateHandler.registerUpdatable(leftLightSensor, 100);
		UpdateHandler.registerUpdatable(rightLightSensor, 100);
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
					Driver.turnRight(turnSpeed);
				}else{
					Driver.turnLeft(turnSpeed);
				}
			}
			// Left sensor detecting the black line
			else if(blackLeft){
				Driver.driveForward(speed);
				lastDetectedSensor="left";
			}
			// Right sensor detecting the black line
			else if(blackRight){
				Driver.driveForward(speed);
				lastDetectedSensor="right";
			}
			// Both sensors not detecting the black line
			else{
				if(lastDetectedSensor.equals("left")){
					Driver.turnLeft(turnSpeed);
				}else{
					Driver.turnRight(turnSpeed);
				}
			}
		}
	}
}
