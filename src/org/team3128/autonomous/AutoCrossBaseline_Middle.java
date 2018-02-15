package org.team3128.autonomous;

import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoCrossBaseline_Middle extends CommandGroup {
	public AutoCrossBaseline_Middle(SRXTankDrive drive, Forklift forklift) {
		AutoValues autoValues = new AutoValues();

		final double switchDistance = autoValues.switchDistance; // vertical distance between startPos and switch
		final double horizOffset = autoValues.horizOffset; // distance between start position horizontal and switch
															// horizontal
		final double switchWidth = autoValues.switchDistance; // width of the switch

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
