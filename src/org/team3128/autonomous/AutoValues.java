package org.team3128.autonomous;

import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.drive.SRXTankDrive.CmdInPlaceTurn;
import org.team3128.common.drive.SRXTankDrive.CmdMoveForward;
import org.team3128.common.util.Log;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoValues extends CommandGroup {
	final double switchDistance = 168 * Length.in; // vertical distance between startPos and switch
	final double horizOffset = 55.56 * Length.in; // distance between start position horizontal and switch horizontal
	final double switchWidth = 100 * Length.in; // width of the switch
}
