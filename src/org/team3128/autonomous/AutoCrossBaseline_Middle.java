package org.team3128.autonomous;

import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoCrossBaseline_Middle extends CommandGroup {
	public AutoCrossBaseline_Middle(SRXTankDrive drive, Forklift forklift) {
<<<<<<< Updated upstream

		final double switchDistance = PowerUpField.SWITCH_VERTICAL_OFFSET; // vertical distance between startPos and switch
		final double horizOffset = PowerUpField.CENTER_OFFSET; // distance between start position horizontal and switch
															// horizontal
		final double switchWidth = PowerUpField.SWITCH_VERTICAL_OFFSET; // width of the switch
=======
		PowerUpAutoValues autoValues = new PowerUpAutoValues();

		final double switchDistance = autoValues.SWITCH_DISTANCE; // vertical distance between startPos and switch
		final double horizOffset = autoValues.SIWTCH_HORIZONAL_OFFSET; // distance between start position horizontal and switch
															// horizontal
		final double switchWidth = autoValues.SWITCH_DISTANCE; // width of the switch
>>>>>>> Stashed changes

		String gameData = "LLL";
		//gameData = DriverStation.getInstance().getGameSpecificMessage();

		addSequential(drive.new CmdMoveForward(switchDistance / 2, 4000, 0.75)); // move forwards
		if (gameData.charAt(1) == 'L') {
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); // turn left
		} else {
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); // turn right
		}
		addSequential(drive.new CmdMoveForward(horizOffset + switchWidth / 2, 4000, 0.75)); // move across field
		if (gameData.charAt(1) == 'L') {
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); // turn left
		} else {
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); // turn right
		}
		addSequential(drive.new CmdMoveForward(switchDistance/2 + 30 * Length.in, 4000, 0.75)); // move towards scale

	}
}
