package org.team3128.autonomous;

import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.drive.SRXTankDrive.CmdInPlaceTurn;
import org.team3128.common.drive.SRXTankDrive.CmdMoveForward;
import org.team3128.common.util.Log;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Intake;
import org.team3128.mechanisms.Forklift.CmdForkliftPush;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoPlaceBlockSwitch_Right extends CommandGroup {
	public AutoPlaceBlockSwitch_Right(SRXTankDrive drive, Forklift forklift) {
		final double autoLineDistance = 160 * Length.in; // distance between startPosition and autoLine
		final double switchDistance = 168 * Length.in; // distance between startPosition and switch
		final double horizOffset = 10 * Length.in; // distance between start position horizontal and switch horizontal
		final double switchOffset = 100 * Length.in; // robot width

		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();

		if (gameData.charAt(0) == 'L') {
			// thinking of more efficient structure for auto code
		} else if (gameData.charAt(0) == 'R') {
		}
	}
}
