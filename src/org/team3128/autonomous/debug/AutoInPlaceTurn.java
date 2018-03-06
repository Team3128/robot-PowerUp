/*
 *  Date: 1/8/2018
 * Description: Drives Guido for a distance of 100 inches so that the PIDS 
 * can easily be tuned(by checking distance and acceleration/deceleration).
 *
 */
package org.team3128.autonomous.debug;

import org.team3128.common.util.enums.Direction;
import org.team3128.main.MainGuido;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoInPlaceTurn extends CommandGroup
{
	public AutoInPlaceTurn(MainGuido robot, double angle, Direction dir)
	{
		addSequential(robot.drive.new CmdInPlaceTurn((float) angle, 0.6, 10000, dir));
	}
}
