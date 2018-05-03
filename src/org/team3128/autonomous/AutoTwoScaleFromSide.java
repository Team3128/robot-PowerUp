package org.team3128.autonomous;

import org.team3128.common.autonomous.primitives.CmdRunInParallel;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Forklift.ForkliftState;
import org.team3128.mechanisms.Intake.IntakeState;
import org.team3128.util.PlateAllocation;

public class AutoTwoScaleFromSide extends AutoScaleFromSide
{

	public AutoTwoScaleFromSide(SRXTankDrive drive, Forklift forklift, Direction side, double delay)
	{
		super(drive, forklift, side, delay);
		
		if (side == PlateAllocation.getScale()) {
			addSequential(new CmdRunInParallel(
					drive.new CmdInPlaceTurn(110, 1.0, 3000, PlateAllocation.getScale().opposite()),
					forklift.new CmdSetForkliftPosition(ForkliftState.GROUND)
			));
			
			addSequential(new CmdRunInParallel(
					drive.new CmdMoveForward(8 * Length.ft, 1500, 0.6),
					forklift.new CmdRunIntake(IntakeState.INTAKE, 1500)
			));
			
			addSequential(drive.new CmdMoveForward(-4 * Length.ft, 2000, 1.0));
			addSequential(new CmdRunInParallel(
					drive.new CmdInPlaceTurn(90, 1.0, 3000, PlateAllocation.getScale()),
					forklift.new CmdSetForkliftPosition(ForkliftState.HI_SCALE)
			));
			
			addSequential(drive.new CmdMoveForward(3 * Length.ft, 2000, 1.0));
			addSequential(forklift.new CmdRunIntake(IntakeState.OUTTAKE, 2000));
		}
		
	}

}
