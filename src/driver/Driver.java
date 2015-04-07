package driver;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

/**
 * This class is used to speak to all motors of the robot. 
 * With this class you can say the robot has to turn his wheels and range sensor.
 * @author Timon van den Brink
 * @author Scott Mackay
 * @version 1.0
 */

public class Driver {
	
	/**
	 * Objects used to speak to the motors.
	 */
	private static NXTRegulatedMotor leftWheel, rightWheel, rangeMotor;
	
	/**
	 * The distance between the two wheels.
	 */
	private static final float WHEEL_CENTER_DISTANCE = 11.3f; // cm
	
	/**
	 * The diameter of one wheel.
	 */
	private static final float WHEEL_DIAMETER = 4.32f; // cm
	
	/**
	 * The circumference of a wheel.
	 */
	private static final float WHEEL_CIRCUMFERENCE = (float) Math.PI
			* WHEEL_DIAMETER; // cm

	/**
	 * This method initializes the motors to variables so you can use the motors.
	 */
	public static void init() {
		leftWheel = Motor.A;
		rightWheel = Motor.B;
		rangeMotor = Motor.C;
		rangeMotor.resetTachoCount();
	}
	
	/**
	 * Turns the wheels to the left with a given speed of turning.
	 * @param speed the speed of the wheels turning in cm/s.
	 */
	public static void turnLeft(float speed) {
		int degreesPerSecond=(int) (speed/WHEEL_CIRCUMFERENCE*360);
		leftWheel.setSpeed(degreesPerSecond);
		rightWheel.setSpeed(degreesPerSecond);
		turnLeft();
	}
	
	/**
	 * Turns the wheels to the left.
	 */
	private static void turnLeft() {
		leftWheel.backward();
		rightWheel.forward();
	}
	
	/**
	 * Turns the wheels to the right with a given speed of turning.
	 * @param speed the speed of the wheels turning in cm/s.
	 */
	public static void turnRight(float speed) {
		int degreesPerSecond=(int) (speed/WHEEL_CIRCUMFERENCE*360);
		leftWheel.setSpeed(degreesPerSecond);
		rightWheel.setSpeed(degreesPerSecond);
		turnRight();
	}
	
	/**
	 * Turns the wheels to the right.
	 */
	private static void turnRight() {
		leftWheel.forward();
		rightWheel.backward();
	}

	/**
	 * Turns the robot a given amount of degrees and stops it. While turning the method sleeps.
	 * That means the thread that called this method won't continue until the robot has turned the amount of degrees and stopped turning.
	 * @param angle the angle that the robot has to turn.
	 * @param speed the speed of the robot turning in cm/s.
	 */
	public static void turnAngle(int angle, int speed) {
		float circumference=(float)(Math.PI)*WHEEL_CENTER_DISTANCE;
		float wheelRotationsToTurn=circumference*((float)angle/360f)/WHEEL_CIRCUMFERENCE;
		leftWheel.setSpeed(speed);
		rightWheel.setSpeed(speed);
		if (wheelRotationsToTurn > 0) {
			turnRight();
		} else if (wheelRotationsToTurn < 0) {
			turnLeft();
		} else {
			driveForward();
		}
		try {
			Thread.sleep((int)((float) Math.abs(wheelRotationsToTurn*360f) / (float) speed * 1000f));
		} catch (Exception e) {
		}
		leftWheel.stop();
		rightWheel.stop();
	}
	
	/**
	 * Drives the robot in an arc to the left with a given speed of driving and angle of the arc.
	 * @param radius the angle in radians of the robot driving left.
	 * @param speed the speed of the robot driving the arc.
	 */
	public static void driveSmoothLeft(float radius, int speed) {
		//Can't go smaller than 0
		if (radius <= 0) {
			radius = WHEEL_CENTER_DISTANCE / 2;
		}
		driveArc(speed, -1 * radius);
	}
	
	/**
	 * Drives the robot in an arc to the right with a given speed of driving and angle of the arc.
	 * @param radius the angle in radians of the robot driving right.
	 * @param speed the speed of the robot driving the arc.
	 */
	public static void driveSmoothRight(float radius, int speed) {
		//Can't go smaller than 0
		if (radius <= 0) {
			radius = WHEEL_CENTER_DISTANCE / 2;
		}
		driveArc(speed, radius);
	}
	
	/**
	 * Drives the robot in an arc calculated by the radius.
	 * @param speed the speed of the robot driving the arc.
	 * @param radius the angle in radians of the robot driving the arc.
	 */
	private static void driveArc(float speed, float radius) {
		//get the absolute radius so it doesn't have problems with negative radius (to the right)
		float absRadius=Math.abs(radius);
		//left wheel
		float distanceLeftWheel=2f*(float)Math.PI*(absRadius+(0.5f*WHEEL_CENTER_DISTANCE));				//cm
		float speedLeftWheel=speed/WHEEL_CIRCUMFERENCE*360f;											//degrees per second
		float timeLeftWheel=distanceLeftWheel/speed;													//seconds
				
		//right wheel
		float distanceRightWheel=2f*(float)Math.PI*(absRadius-(0.5f*WHEEL_CENTER_DISTANCE));			//cm
		float speedRightWheel=distanceRightWheel/timeLeftWheel/WHEEL_CIRCUMFERENCE*360f;				//degrees per second
		
		if(radius<0){
			//switch left and right wheel speed
			float temp=speedLeftWheel;
			speedLeftWheel=speedRightWheel;
			speedRightWheel=temp;
		}
		
		Motor.A.setSpeed(speedLeftWheel);
		Motor.B.setSpeed(speedRightWheel);
		
		//go forward if the speed is positive, if the speed is negative go backward
		if(speedLeftWheel>0){	
			Motor.A.forward();
		}else if(speedLeftWheel<0){
			Motor.A.backward();
		}
		if(speedRightWheel>0){
			Motor.B.forward();
		}else if(speedRightWheel<0){
			Motor.B.backward();
		}
	}
	
	/**
	 * Drives the robot forward with a given speed.
	 * @param speed the speed of the robot driving forwards.
	 */
	public static void driveForward(float speed) {	//cm per second
		int degreesPerSecond=(int) (speed/WHEEL_CIRCUMFERENCE*360);
		leftWheel.setSpeed(degreesPerSecond);
		rightWheel.setSpeed(degreesPerSecond);
		driveForward();
	}
	
	/**
	 * Drives the robot forward.
	 */
	private static void driveForward() {
		leftWheel.forward();
		rightWheel.forward();
	}
	
	/**
	 * Turns the range sensor in a given amount of degrees.
	 * @param angle the amount of degrees the range sensor turns;
	 */
	public static void turnRangeSensor(int angle) {
		rangeMotor.rotate(angle);
	}
	
	/**
	 * Resets the tachometer back to zero. The tachometer is something that remembers the angle of the motor turned in degrees.
	 */
	public static void resetRangeSensorTachoCount(){
		rangeMotor.resetTachoCount();
	}
	
	/**
	 * Stops both wheels with driving.
	 */
	public static void stop(){
		leftWheel.stop();
		rightWheel.stop();
	}

}