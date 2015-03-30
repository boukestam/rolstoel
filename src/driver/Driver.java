package driver;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class Driver {

	private static NXTRegulatedMotor leftWheel, rightWheel, rangeMotor;
	private static final float WHEEL_CENTER_DISTANCE = 11.3f; // cm
	private static final float WHEEL_DIAMETER = 4.32f; // cm
	private static final float WHEEL_CIRCUMFERENCE = (float) Math.PI
			* WHEEL_DIAMETER; // cm

	public static void init() {
		leftWheel = Motor.A;
		rightWheel = Motor.B;
		rangeMotor = Motor.C;
		rangeMotor.resetTachoCount();
	}

	public static void turnLeft(int speed) {
		leftWheel.setSpeed(speed);
		rightWheel.setSpeed(speed);
		turnLeft();
	}

	public static void turnLeft() {
		leftWheel.backward();
		rightWheel.forward();
	}

	public static void turnRight(int speed) {
		leftWheel.setSpeed(speed);
		rightWheel.setSpeed(speed);
		turnRight();
	}

	public static void turnRight() {
		leftWheel.forward();
		rightWheel.backward();
	}

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
	
	public static void driveArc(float speed,float radius) {
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

	public static void driveForward(int speed) {
		leftWheel.setSpeed(speed);
		rightWheel.setSpeed(speed);
		driveForward();
	}

	public static void driveForward() {
		leftWheel.forward();
		rightWheel.forward();
	}

	public static void turnRangeSensor(int angle) {
		rangeMotor.rotate(angle);
	}

}