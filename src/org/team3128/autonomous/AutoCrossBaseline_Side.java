package org.team3128.autonomous;

import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.drive.SRXTankDrive.CmdMoveForward;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoCrossBaseline_Side extends CommandGroup {
	public AutoCrossBaseline_Side(SRXTankDrive drive, Forklift forklift) {
		PowerUpAutoValues autoValues = new PowerUpAutoValues();
		final double switchDistance = autoValues.SWITCH_DISTANCE; // vertical distance between startPos and switch

		addSequential(drive.new CmdMoveForward(switchDistance + 30 * Length.in, 4000, 0.75)); // move past switch
	}
}
