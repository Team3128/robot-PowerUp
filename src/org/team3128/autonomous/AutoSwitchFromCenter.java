package org.team3128.autonomous;

import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.enums.Direction;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Forklift.ForkliftState;
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
		final double vertical_travel = PowerUpAutoValues.SWITCH_BACK_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH
				- horizontal_distance - PowerUpAutoValues.CUBE_EXTENSION;

		addSequential(drive.new CmdFancyArcTurn(turn_radius, 90, 5000, PlateAllocation.nearSwitch, 0.8));
		// fancy turn 90 deg opposite plate direction

		addParallel(drive.new CmdMoveForward(vertical_travel, 5000, 0.8));
		// add parallel raise forklift to height

		// add sequential deposit
	}
}
