package org.team3128.autonomous;

import org.team3128.autonomous.util.PowerUpAutoValues;
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
		
		double back_turn_radius = 2.0/3.0 * PowerUpAutoValues.SWITCH_WIDTH;
		
		addSequential(new CmdRunInParallel(
				drive.new CmdFancyArcTurn(.32 * PowerUpAutoValues.SWITCH_WIDTH, -90, 5000, PlateAllocation.getNearSwitch().opposite(), 0.8, true),
				forklift.new CmdSetForkliftPosition(ForkliftState.GROUND)
			));
		
		addSequential(drive.new CmdInPlaceTurn(90, 1.0, 5000, PlateAllocation.getNearSwitch().opposite()));
		
		addSequential(new CmdRunInParallel(
				drive.new CmdMoveForward(5 * Length.ft, 2000, 1.0),
				forklift.new CmdRunIntake(IntakeState.INTAKE, 2000)
		));
		
		addSequential(drive.new CmdMoveForward(-5 * Length.ft, 5000, 1.0));
		addSequential(drive.new CmdInPlaceTurn(32, 1.0, 5000, PlateAllocation.getNearSwitch()));
		
		// I made a change
		
		addSequential(new CmdRunInParallel(
				drive.new CmdMoveForward(30 * Length.ft, 3000, 0.75),
				forklift.new CmdSetForkliftPosition(ForkliftState.SWITCH)
			));
		
		
		addSequential(forklift.new CmdRunIntake(IntakeState.OUTTAKE, 1500));
	}
}
