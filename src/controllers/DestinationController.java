package controllers;

import driver.Driver;
import lejos.nxt.SensorPort;
import sensors.MyColorSensor;
import sensors.MyLightSensor;
import sensors.MyRangeSensor;
import sensors.Sensor;
import sensors.UpdateHandler;

/**
 * The controller that tries to follow the line to the destination.
 * @author Noah Steijlen
 * @author Scott Mackay
 * @author Bouke Stam
 * @author Timon van den Brink
 * @version 1.0
 */

public class DestinationController extends Controller{
	
	/**
	 * Objects used to find out which sensor is which.
	 */
	private Sensor leftLightSensor, rightLightSensor, rangeSensor;
	
	/**
	 * Object to the other controllers.
	 */
	private Controller avoidController,calibrateController;
	
	/**
	 * Booleans remembering if the right or left sensor detected the black line.
	 */
	private boolean blackLeft=false,blackRight=false;
	
	/**
	 * Says if the left or right light sensor detected the line for the last time.
	 */
	private String lastDetectedSensor="none";
	
	/**
	 * The speed of the robot turning.
	 */
	private final float SPEED=10;
	
	/**
	 * The speed of the robot turning in curves.
	 */
	private final float TURN_SPEED=SPEED;
	
	/**
	 * The starting turn radius when the robot found the line.
	 */
	private final float INIT_TURN_RADIUS=10;
	
	/**
	 * The starting turn radius when the robot is on the line and tries to stay on the line.
	 */
	private final float STRAIGHT_TURN_RADIUS=120;
	
	/**
	 * The start acceleration of the robot turning.
	 */
	private final float TURN_ACCELERATION=20;
	
	/**
	 * The current acceleration of the robot turning.
	 */
	private float turnRadius=INIT_TURN_RADIUS;
	
	/**
	 * Initialized the sensors and listens to the sensors.
	 */
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
	
	/**
	 * IN this method the turn radius slowly decreases so the robot will turn faster and faster.
	 */
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
	
	/**
	 * When the value of one of the sensors that this object is listening for has changed, 
	 * this method gets called with the new value and the sensor. 
	 * @param source the sensor who's value has changed.
	 * @param value the new value of the sensor.
	 */
	@Override
	public void valueChanged(Sensor source, int value) {
		if(super.isRunning()){
			if(source==rangeSensor){
				//if range distance is less then 20 cm
				if(value<20){
					lastDetectedSensor="left";
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
			
			drive();
		}
	}
	
	/**
	 * In this method all logic of keep following the line and 
	 * finding back the line is here controlled.
	 */
	public void drive(){
		// Left and Right sensor detecting the black line
		if(blackLeft&&blackRight){
			if(lastDetectedSensor.equals("left")){
				Driver.turnRight(TURN_SPEED);
			}else{
				Driver.turnLeft(TURN_SPEED);
			}
		}
		// Left sensor detecting the black line
		else if(blackLeft){
			Driver.driveSmoothLeft(STRAIGHT_TURN_RADIUS,(int) SPEED);
			lastDetectedSensor="left";
			turnRadius=INIT_TURN_RADIUS;
		}
		// Right sensor detecting the black line
		else if(blackRight){
			Driver.driveSmoothRight(STRAIGHT_TURN_RADIUS,(int) SPEED);
			lastDetectedSensor="right";
			turnRadius=INIT_TURN_RADIUS;
		}
		// Both sensors not detecting the black line
		else{
			if(lastDetectedSensor.equals("left")){
				Driver.driveSmoothLeft(turnRadius,(int) TURN_SPEED);
			}else{
				Driver.driveSmoothRight(turnRadius,(int) TURN_SPEED);
			}
		}
	}
	
	@Override
	public void onStart() {
		drive();
	}
	
	@Override
	public void onStop() {
		
	}
}
