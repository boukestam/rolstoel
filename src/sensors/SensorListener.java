/*
 * Auteur: Timon van den Brink
 * Datum: 3-10-2015
 */

package sensors;

public interface SensorListener {
	public void valueChanged(Sensor source, int value);
}
