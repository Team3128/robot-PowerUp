package org.team3128.autonomous;

import org.team3128.common.autonomous.primitives.CmdRunInParallel;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Forklift.ForkliftState;
import org.team3128.mechanisms.Intake.IntakeState;
import org.team3128.util.PlateAllocation;

public class AutoTwoSwitchFromCenter extends AutoSwitchFromCenter {
	public AutoTwoSwitchFromCenter(SRXTankDrive drive, Forklift forklift, double delay) {
		super(drive, forklift, delay);
				
		addSequential(new CmdRunInParallel(
				drive.new CmdFancyArcTurn(turn_radius + 1 * Length.ft, -90, 5000, PlateAllocation.getNearSwitch().opposite(), 1.0, true),
				forklift.new CmdSetForkliftPosition(ForkliftState.GROUND)
			));
		addSequential(drive.new CmdFancyArcTurn(turn_radius - 1 * Length.ft, -90, 5000, PlateAllocation.getNearSwitch(), 1.0, true));
		
		addSequential(new CmdRunInParallel(
				drive.new CmdFancyArcTurn(12 * Length.ft, 15, 5000, PlateAllocation.getNearSwitch(), 1.0, true),
				forklift.new CmdRunIntake(IntakeState.INTAKE, 3000)
			));
	}
}
