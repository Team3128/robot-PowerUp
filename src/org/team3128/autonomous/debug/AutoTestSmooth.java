/*
 *  Date: 1/8/2018
 * Description: Drives Guido for a distance of 100 inches so that the PIDS 
 * can easily be tuned(by checking distance and acceleration/deceleration).
 *
 */
package org.team3128.autonomous.debug;

import org.team3128.common.util.units.Length;
import org.team3128.main.MainGuido;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoTestSmooth extends CommandGroup
{
	public AutoTestSmooth(MainGuido robot)
	{
		addSequential(robot.drive.new CmdMoveForward(50 * Length.in, 10000, true, .8));
		addSequential(robot.drive.new CmdMoveForward(50 * Length.in, 10000, false, .8));
	}
}
