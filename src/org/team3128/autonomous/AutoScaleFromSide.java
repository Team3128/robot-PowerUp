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

public class AutoScaleFromSide extends AutoGuidoBase
{
	public AutoScaleFromSide(SRXTankDrive drive, Forklift forklift, Direction side, double delay)
	{
		super(drive, forklift, delay);

		if (side == PlateAllocation.getNearSwitch()) {
			final double horizontal_distance = PowerUpAutoValues.ALLIANCE_WALL_EDGE -(PowerUpAutoValues.ROBOT_WIDTH / 2) - (PowerUpAutoValues.SCALE_WIDTH / 2);
			final double vertical_distance = PowerUpAutoValues.SCALE_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH - horizontal_distance + 1 * Length.ft;
			
			final float angle = 10f;
			
			final double large_turn_radius = vertical_distance * 180 / (angle * Math.PI);
					
			// (pi/180) * r = d
			
			addSequential(drive.new CmdFancyArcTurn(large_turn_radius, angle, 10000, side));
			addSequential(new CmdRunInParallel(
					drive.new CmdInPlaceTurn(85, 1.0, 1500, side.opposite()),
					forklift.new CmdSetForkliftPosition(ForkliftState.SCALE))
			);
		}
		else {
			final double vertical = 1.6 * Length.ft + PowerUpAutoValues.SWITCH_BACK_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH;
			final double turn_1 = (PowerUpAutoValues.SWITCH_BACK_DISTANCE - PowerUpAutoValues.SWITCH_FRONT_DISTANCE) / 2;

			final double horizontal = PowerUpAutoValues.SCALE_WIDTH - 2 * Length.ft;
			
			final double turn_2 = (PowerUpAutoValues.SCALE_DISTANCE - vertical - turn_1) / 2 - 1.5 * Length.ft;
			
			addSequential(drive.new CmdMoveForward(vertical, 10000, true, 1.0));
			addSequential(drive.new CmdFancyArcTurn(turn_1, 89, 5000, side.opposite(), 1.0, true));
			
			addSequential(drive.new CmdMoveForward(horizontal, 10000, true, 1.0));
			
			addSequential(new CmdRunInParallel(
					drive.new CmdFancyArcTurn(turn_2, 170, 10000, side, 1.0, false),
					forklift.new CmdSetForkliftPosition(ForkliftState.SCALE)
					));
		}
		
		addSequential(drive.new CmdMoveForward(2 * Length.ft, 500, 1.0));
		addSequential(forklift.new CmdRunIntake(IntakeState.OUTTAKE, 1000));
	}
}
