package org.team3128.autonomous.debug;

import org.team3128.main.MainGuido;
import org.team3128.mechanisms.Forklift.ForkliftState;
import org.team3128.mechanisms.Intake.IntakeState;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoScaleDropoffTest extends CommandGroup
{
	public AutoScaleDropoffTest(MainGuido robot)
	{
		addSequential(robot.forklift.new CmdSetForkliftPosition(ForkliftState.SCALE));
		addSequential(robot.forklift.new CmdRunIntake(IntakeState.OUTTAKE));
		addSequential(robot.forklift.new CmdSetForkliftPosition(ForkliftState.GROUND));
	}
}