/*
 *  Date: 1/8/2018
 * Description: Drives Guido for a distance of 100 inches so that the PIDS 
 * can easily be tuned(by checking distance and acceleration/deceleration).
 *
 */
package org.team3128.autonomous;

import org.team3128.common.util.Log;
import org.team3128.common.util.units.Length;
import org.team3128.main.MainGuido;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CalibrateRunPID extends CommandGroup
{
	public CalibrateRunPID(MainGuido robot)
	{
		addSequential(robot.drive.new CmdMoveForward(100 * Length.in, 10000, .7));
		Log.info("Guido Auto", "CalibrateRunPID called");

	}
}
