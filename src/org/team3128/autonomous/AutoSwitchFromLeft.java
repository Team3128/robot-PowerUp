package org.team3128.autonomous;

import org.team3128.autonomous.util.PowerUpAutoValues;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.Log;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;
import org.team3128.util.PlateAllocation;

public class AutoSwitchFromLeft extends AutoGuidoBase
{
	public AutoSwitchFromLeft(SRXTankDrive drive, Forklift forklift)
	{
		super(drive, forklift);
		final double scaleCup = 42*Length.in;
		final double robot_left_offset = 2*scaleCup;
		double horizontal_distance;

		if (PlateAllocation.getNearSwitch() == Direction.RIGHT)
		{
			horizontal_distance = PowerUpAutoValues.SWITCH_WIDTH-robot_left_offset;
		}
		else
		{
			horizontal_distance = 0;
		}

		final double turn_radius = horizontal_distance / 2;
		final double vertical_travel = PowerUpAutoValues.SWITCH_FRONT_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH - horizontal_distance;
		Log.debug("AutoLeftSwitch", "Vertical Distance: " + vertical_travel);
		
		Log.debug("AutoLeftSwitch", "Calculations complete");
		addSequential(drive.new CmdFancyArcTurn(turn_radius, 85, 5000, PlateAllocation.getNearSwitch(), 1.0, true));
		Log.debug("AutoLeftSwitch", "First Turn");

		addSequential(drive.new CmdFancyArcTurn(turn_radius, 85, 5000, PlateAllocation.getNearSwitch().opposite(), 1.0,
				true));
		Log.debug("AutoLeftSwitch", "Second Turn");

		addSequential(drive.new CmdMoveForward(vertical_travel, 5000, 1.0));
		/*addSequential(new CmdRunInParallel(drive.new CmdMoveForward(vertical_travel, 5000, 1.0),
				forklift.new CmdSetForkliftPosition(ForkliftState.SWITCH)));*/
		Log.debug("AutoLeftSwitch", "Move Forklift");


		//addSequential(forklift.new CmdRunIntake(IntakeState.OUTTAKE, 500));

	}
}
