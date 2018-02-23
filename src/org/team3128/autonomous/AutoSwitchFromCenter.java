package org.team3128.autonomous;

import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.enums.Direction;
import org.team3128.mechanisms.Forklift;
<<<<<<< Updated upstream
import org.team3128.mechanisms.Forklift.ForkliftState;
=======
>>>>>>> Stashed changes
import org.team3128.util.PlateAllocation;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSwitchFromCenter extends CommandGroup {
	public AutoSwitchFromCenter(SRXTankDrive drive, Forklift forklift) {
<<<<<<< Updated upstream
		final double switchDistance = PowerUpField.SWITCH_VERTICAL_OFFSET; // vertical distance between startPos and switch
		final double horizOffset = PowerUpField.CENTER_OFFSET; // distance between start position horizontal and switch horizontal
		final double switchWidth = PowerUpField.SWITCH_VERTICAL_OFFSET; // width of the switch
		
=======
		final double robot_center_offset = PowerUpAutoValues.ROBOT_WIDTH / 2 - PowerUpAutoValues.CENTER_OFFSET;
		double horizontal_distance;

>>>>>>> Stashed changes
		if (PlateAllocation.nearSwitch == Direction.RIGHT) {
			horizontal_distance = PowerUpAutoValues.SWITCH_PLATE_CENTER - robot_center_offset;
		} else {
			horizontal_distance = PowerUpAutoValues.SWITCH_PLATE_CENTER + robot_center_offset;
		}

		final double turn_radius = horizontal_distance / 2;
		final double vertical_travel = PowerUpAutoValues.SWITCH_BACK_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH
				- horizontal_distance - PowerUpAutoValues.CUBE_EXTENSION;

<<<<<<< Updated upstream
			addSequential(forklift.new CmdSetForkliftPosition(ForkliftState.SWITCH));
			Log.info("MainGuido(AUTO)", "[3]outtake activated");

		} else {
			addSequential(drive.new CmdMoveForward(switchDistance / 2, 4000, 0.75)); // drive forward
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); // turn left
			addSequential(drive.new CmdMoveForward(horizOffset + switchWidth / 2, 2000, 0.75)); // across field
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); // turn right
			addSequential(drive.new CmdMoveForward(switchDistance / 2, 2000, 0.75)); // move to switch vertical
			Log.info("MainGuido(AUTO)", "[1]reached switch vertical");

			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); // turn right
			addSequential(drive.new CmdMoveForward(horizOffset, 2000, 0.75)); // move to switch horizontal
			Log.info("MainGuido(AUTO)", "[2]reached switch horizontal");

			addSequential(forklift.new CmdSetForkliftPosition(ForkliftState.SWITCH));
			Log.info("MainGuido(AUTO)", "[3]outtake activated");
		}
=======
		// fancy turn 90 deg in plate direction
		// fancy turn 90 deg opposite plate direction

		addParallel(drive.new CmdMoveForward(vertical_travel, 5000, 0.8));
		// add parallel raise forklift to height
>>>>>>> Stashed changes

		// add sequential deposit
	}
}
