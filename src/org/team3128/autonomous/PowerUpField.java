package org.team3128.autonomous;

import org.team3128.common.util.units.Length;

/**
 * The set of distances and lengths of field elements of the field for the 2018
 * FRC Game POWER UP.
 */
public class PowerUpField {
	/**
	 * The horizontal distance between the robot's start position (just to the right
	 * of the power cube loading zone) and the center of the field.
	 */
	final static double CENTER_OFFSET = 0 * Length.in;
	
	
	/**
	 * The vertical distance between the robot's start position (the alliance
	 * station wall) and the switch
	 */
	final static double SWITCH_VERTICAL_OFFSET = 168 * Length.in;

	/**
	 * The width of the switch.
	 */
	final static double SWITCH_WIDTH = 100 * Length.in;

	
	
	/**
	 * The vertical distance between the robot's start position (the alliance
	 * station wall) and the scale
	 */
	final static double SCALE_VERTICAL_OFFSET = 168 * Length.in;

	/**
	 * The width of the switch.
	 */
	final static double SCALE_WIDTH = 100 * Length.in;

}
