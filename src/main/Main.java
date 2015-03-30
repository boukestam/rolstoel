/*
 * Auteur: Bouke Stam
 * Datum: 3-10-2015
 */
// Hoi
package main;

import controllers.DestinationController;
import driver.Driver;

public class Main {

	public static void main(String args[]){
		Driver.init();
		new DestinationController();
	}
}
