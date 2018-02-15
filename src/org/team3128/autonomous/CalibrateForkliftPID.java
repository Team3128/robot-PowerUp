package org.team3128.autonomous;

import org.team3128.common.util.Log;
import org.team3128.main.MainGuido;
import org.team3128.mechanisms.Forklift.ForkliftState;
import org.team3128.mechanisms.Intake.IntakeState;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CalibrateForkliftPID extends CommandGroup
{
	public CalibrateForkliftPID(MainGuido robot)
	{
		Log.debug("Guido Auto", "Calling Callibrate Forklift PID");
		addSequential(robot.forklift.new CmdForkliftPush(ForkliftState.SWITCH, IntakeState.STOPPED));
		Log.info("Guido Auto", "Calibrate Forklift PID called");
	}
}