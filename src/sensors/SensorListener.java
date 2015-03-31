package sensors;

/**
 * This interface specifies for everything that uses this will be a listener for a sensor.
 * @author Timon van den Brink
 * @version 1.0
 */

public interface SensorListener {
	
	/**
	 * Method that must be implemented by all listeners of this interface. It gives the new value of the listener and the sensor who gave the message.
	 * @param source the sensor which the new value came from.
	 * @param value the new given value by the sensor.
	 */
	public void valueChanged(Sensor source, int value);
}
