/*
 *  Date: 1/8/2018
 * Description: Drives Guido for a distance of 100 inches so that the PIDS 
 * can easily be tuned(by checking distance and acceleration/deceleration).
 *
 */
package org.team3128.autonomous.debug;

import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.main.MainGuido;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoArcTurn extends CommandGroup
{
	public AutoArcTurn(MainGuido robot, double angle, Direction dir)
	{
		addSequential(robot.drive.new CmdFancyArcTurn(36 * Length.in, (float) angle, 10000, dir, 1.0, true));
	}
}
