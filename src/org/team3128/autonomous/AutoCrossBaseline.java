package org.team3128.autonomous;

import org.team3128.autonomous.util.PowerUpAutoValues;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.mechanisms.Forklift;

public class AutoCrossBaseline extends AutoGuidoBase
{
	public AutoCrossBaseline(SRXTankDrive drive, Forklift forklift, double delay)
	{
		super(drive, forklift, delay);
		
		addSequential(drive.new CmdMoveForward(PowerUpAutoValues.SWITCH_FRONT_DISTANCE, 10000, 1.0));
	}
}
