package org.team3128.autonomous;

import org.team3128.common.util.units.Length;

/**
 * The set of distances and lengths of field element of the field for as well as
 * robot-specific distances and lengths for the 2018 FRC Game POWER UP.
 */
public class PowerUpAutoValues {
	
	// Robot-specific values
	/**
	 * The width (right edge to left edge) of the robot's bumpers.
	 */
	final static double ROBOT_WIDTH = 0 * Length.in;

	/**
	 * The length (front edge to back edge) of the robot's bumpers.
	 */
	final static double ROBOT_LENGTH = 0 * Length.in;

	/**
	 * How many inches the loaded power cube extends in front of the bumpers.
	 */
	final static double CUBE_EXTENSION = 0 * Length.in;

	
	// Field-specific values
	/**
	 * The horizontal distance between the center of the field and the right edge of
	 * the exchange tape.
	 */
	final static double CENTER_OFFSET = 12 * Length.in;

	/**
	 * The horizontal distance between the center of the switch plate and the center
	 * of the field.
	 */
	final static double SWITCH_PLATE_CENTER = 54 * Length.in;

	/**
	 * The vertical distance between the the alliance station wall and the front
	 * panel of the switch fencing.
	 */
	final static double SWITCH_FRONT_DISTANCE = 140 * Length.in;

	/**
	 * The vertical distance between the the alliance station wall and the back
	 * panel of the switch fencing.
	 */
	final static double SWITCH_BACK_DISTANCE = 196 * Length.in;

	/**
	 * The width of the switch.
	 */
	final static double SWITCH_WIDTH = 153.5 * Length.in;

	/**
	 * The vertical distance between the the alliance station wall and the center of
	 * the scale plate.
	 */
	final static double SCALE_DISTANCE = 324 * Length.in;

	/**
	 * The width of the scale.
	 */
	final static double SCALE_WIDTH = 180 * Length.in;

	/**
	 * The distance between the center of the scale plate and the back panel of the
	 * switch fencing.
	 */
	final static double SWITCH_SCALE_DISTANCE = 128 * Length.in;

}
