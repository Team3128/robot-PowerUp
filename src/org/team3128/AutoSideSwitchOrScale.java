package org.team3128;

import org.team3128.autonomous.AutoGuidoBase;
import org.team3128.autonomous.AutoScaleFromSide;
import org.team3128.autonomous.AutoSwitchFromSide;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;
import org.team3128.util.PlateAllocation;

public class AutoSideSwitchOrScale extends AutoGuidoBase
{
	public AutoSideSwitchOrScale(SRXTankDrive drive, Forklift forklift, Direction side, double delay)
	{
		super(drive, forklift, delay);
		
		if (side == PlateAllocation.getScale()) {
			addSequential(new AutoScaleFromSide(drive, forklift, side, delay));
		}
		else if (side == PlateAllocation.getNearSwitch()) {
			addSequential(new AutoSwitchFromSide(drive, forklift, side, delay));
		}
		else {
			addSequential(drive.new CmdMoveForward(100 * Length.in, 5000, true));
			addSequential(drive.new CmdMoveForward(-50 * Length.in, 5000, true));
		}
	}
}
