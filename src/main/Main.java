package main;

import controllers.DestinationController;
import driver.Driver;

/**
 * The class where the program starts.
 * @author Bouke Stam
 * @version 1.0
 */

public class Main {
	
	/**
	 * Method where the program starts.
	 * @param args String of arguments. This parameter is not used.
	 */
	public static void main(String args[]){
		Driver.init();
		new DestinationController();
	}
}
