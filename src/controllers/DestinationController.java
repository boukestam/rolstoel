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
	// -1 == LeftSensor and 0 == RightSensor
	private String lastDetectedSensor="none";
	
	private float SPEED=10;
	private float TURN_SPEED=SPEED;
	
	private final float INIT_TURN_RADIUS=10;
	private final float STRAIGHT_TURN_RADIUS=120;
	private final float TURN_ACCELERATION=20;
	
	private float turnRadius=INIT_TURN_RADIUS;
	
	public DestinationController(){
		this.leftLightSensor=new MyColorSensor(SensorPort.S3);
		this.rightLightSensor=new MyLightSensor(SensorPort.S1);
		this.rangeSensor=new MyRangeSensor(SensorPort.S2);
		
		UpdateHandler.registerUpdatable(leftLightSensor, 10);
		UpdateHandler.registerUpdatable(rightLightSensor, 10);
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
		if((blackLeft&&blackRight)||(!blackLeft&&blackRight)){
			turnRadius-=TURN_ACCELERATION*(TURN_SPEED/2);
			if(turnRadius<0){
				turnRadius=0;
			}
		}
		try{
			Thread.sleep(100);
		}catch(Exception e){}
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
					Driver.turnSmoothRight(turnRadius,(int) TURN_SPEED);
				}else{
					Driver.turnSmoothLeft(turnRadius,(int) TURN_SPEED);
				}
			}
			// Left sensor detecting the black line
			else if(blackLeft){
				Driver.turnSmoothLeft(STRAIGHT_TURN_RADIUS,(int) SPEED);
				lastDetectedSensor="left";
				turnRadius=INIT_TURN_RADIUS;
			}
			// Right sensor detecting the black line
			else if(blackRight){
				Driver.turnSmoothRight(STRAIGHT_TURN_RADIUS,(int) SPEED);
				lastDetectedSensor="right";
				turnRadius=INIT_TURN_RADIUS;
			}
			// Both sensors not detecting the black line
			else{
				if(lastDetectedSensor.equals("left")){
					Driver.turnSmoothLeft(turnRadius,(int) TURN_SPEED);
				}else{
					Driver.turnSmoothRight(turnRadius,(int) TURN_SPEED);
				}
			}
		}
	}
}
