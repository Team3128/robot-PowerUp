package org.team3128.autonomous;

import org.team3128.common.autonomous.primitives.CmdRunInParallel;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.enums.Direction;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Forklift.ForkliftState;
import org.team3128.mechanisms.Intake.IntakeState;
import org.team3128.util.PlateAllocation;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSwitchFromCenter extends CommandGroup {
	public AutoSwitchFromCenter(SRXTankDrive drive, Forklift forklift) {
		final double robot_center_offset = PowerUpAutoValues.ROBOT_WIDTH / 2 - PowerUpAutoValues.CENTER_OFFSET;
		double horizontal_distance;

		if (PlateAllocation.nearSwitch == Direction.RIGHT) {
			horizontal_distance = PowerUpAutoValues.SWITCH_PLATE_CENTER - robot_center_offset;
		} else {
			horizontal_distance = PowerUpAutoValues.SWITCH_PLATE_CENTER + robot_center_offset;
		}

		final double turn_radius = horizontal_distance / 2;
		final double vertical_travel = PowerUpAutoValues.SWITCH_FRONT_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH
				- horizontal_distance;

		final Direction opposite = (PlateAllocation.nearSwitch == Direction.RIGHT) ? Direction.LEFT : Direction.RIGHT;
		
		addSequential(drive.new CmdFancyArcTurn(turn_radius, 90, 5000, PlateAllocation.nearSwitch, 0.8));
		addSequential(drive.new CmdFancyArcTurn(turn_radius, 90, 5000, opposite, 0.8));

		addSequential(new CmdRunInParallel(
				drive.new CmdMoveForward(vertical_travel, 5000, 0.8),
				forklift.new CmdSetForkliftPosition(ForkliftState.SWITCH)
		));
		
		addSequential(forklift.new CmdRunIntake(IntakeState.OUTTAKE, 500));
	}
}
