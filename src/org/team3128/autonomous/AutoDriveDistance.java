/*
 *  Date: 1/8/2018
 * Description: Drives Guido for a distance of 100 inches so that the PIDS 
 * can easily be tuned(by checking distance and acceleration/deceleration).
 *
 */
package org.team3128.autonomous;

import org.team3128.main.MainGuido;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoDriveDistance extends CommandGroup
{
	public AutoDriveDistance(MainGuido robot, double distance)
	{
		addSequential(robot.drive.new CmdMoveForward(distance, 10000, .7));
	}
}
