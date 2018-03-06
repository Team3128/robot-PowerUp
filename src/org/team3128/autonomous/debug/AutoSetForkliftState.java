package org.team3128.autonomous.debug;

import org.team3128.main.MainGuido;
import org.team3128.mechanisms.Forklift.ForkliftState;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSetForkliftState extends CommandGroup
{
	public AutoSetForkliftState(MainGuido robot, ForkliftState state)
	{
		addSequential(robot.forklift.new CmdSetForkliftPosition(state));
	}
}