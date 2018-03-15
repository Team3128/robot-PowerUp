package org.team3128.autonomous;

import org.team3128.autonomous.util.PowerUpAutoValues;
import org.team3128.common.autonomous.primitives.CmdRunInParallel;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Forklift.ForkliftState;
import org.team3128.mechanisms.Intake.IntakeState;
import org.team3128.util.PlateAllocation;

public class AutoSwitchFromSide extends AutoGuidoBase
{
	public AutoSwitchFromSide(SRXTankDrive drive, Forklift forklift, Direction side, double delay)
	{
		super(drive, forklift, delay);
		
		if (side != PlateAllocation.getNearSwitch())
		{
			final double vertical = 1.6 * Length.ft + PowerUpAutoValues.SWITCH_BACK_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH;
			final double turn_1 = (PowerUpAutoValues.SWITCH_BACK_DISTANCE - PowerUpAutoValues.SWITCH_FRONT_DISTANCE) / 2;
			final double horizontal = PowerUpAutoValues.SCALE_WIDTH - 3 * Length.ft;
			final double turn_2 = turn_1 + 0.6 * Length.ft ;
			
			addSequential(drive.new CmdMoveForward(vertical, 10000, true, 1.0));
			addSequential(drive.new CmdFancyArcTurn(turn_1, 85, 5000, side.opposite(), 1.0, true));
			addSequential(drive.new CmdMoveForward(horizontal, 10000, true, 1.0));
			
			addSequential(new CmdRunInParallel(
					drive.new CmdFancyArcTurn(turn_2, 180, 10000, side.opposite(), 1.0, false),
					forklift.new CmdSetForkliftPosition(ForkliftState.SWITCH)
					));
		}
		else {
			final double vertical = PowerUpAutoValues.SWITCH_FRONT_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH/2 + 0.7*Length.ft;
			 double turn = PowerUpAutoValues.ALLIANCE_WALL_EDGE - (PowerUpAutoValues.SWITCH_WIDTH / 2) -
					(PowerUpAutoValues.ROBOT_WIDTH / 2) - (PowerUpAutoValues.ROBOT_LENGTH / 2) - PowerUpAutoValues.CUBE_EXTENSION;
			

				turn -= 6 * Length.in;
		
			
			addSequential(drive.new CmdMoveForward(vertical, 10000, true, 1.0));
						
			addSequential(new CmdRunInParallel(
					drive.new CmdFancyArcTurn(turn, 90, 5000, side.opposite(), 1.0, false),
					forklift.new CmdSetForkliftPosition(ForkliftState.SWITCH)
					));
		}
		
		addSequential(drive.new CmdMoveForward(18 * Length.in, 1000, 1.0));
		addSequential(forklift.new CmdRunIntake(IntakeState.OUTTAKE, 1000));
	}
}
