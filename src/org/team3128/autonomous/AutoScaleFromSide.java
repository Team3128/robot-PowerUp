package org.team3128.autonomous;

import org.team3128.autonomous.util.PowerUpAutoValues;
import org.team3128.common.autonomous.primitives.CmdDelay;
import org.team3128.common.autonomous.primitives.CmdRunInParallel;
import org.team3128.common.autonomous.primitives.CmdRunInSeries;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Angle;
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

		if (side == PlateAllocation.getScale()) {
			System.out.println("---------SAME SIDE-------");
			final double arc_distance = PowerUpAutoValues.SCALE_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH - 3.0 * Length.ft;
			
			final float angle = 15.6f;
			
			final double large_turn_radius = arc_distance * 180 / (angle * Math.PI);
					
			// (pi/180) * r = d
			
			addSequential(new CmdRunInParallel(
					drive.new CmdFancyArcTurn(large_turn_radius, angle + (float) (0.2 * Angle.DEGREES), 10000, side.opposite(), 1.0),
					
					new CmdRunInSeries(
							new CmdDelay(1),
							forklift.new CmdSetForkliftPosition(ForkliftState.SCALE))
					)
			);
		}
		else {
			final double vertical = 1.6 * Length.ft + PowerUpAutoValues.SWITCH_BACK_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH;
			final double turn_1 = (PowerUpAutoValues.SWITCH_BACK_DISTANCE - PowerUpAutoValues.SWITCH_FRONT_DISTANCE) / 2;
			final double horizontal = PowerUpAutoValues.SCALE_WIDTH - 3 * Length.ft;
						
			
			addSequential(drive.new CmdMoveForward(vertical, 10000, true, 1.0));
			addSequential(drive.new CmdFancyArcTurn(turn_1, 100, 5000, side.opposite(), 1.0, true));
			addSequential(drive.new CmdMoveForward(horizontal, 4000, true, 1.0));
						
			addSequential(new CmdRunInParallel(
					drive.new CmdFancyArcTurn(PowerUpAutoValues.ROBOT_WIDTH * 1.3, 185, 5000, side, 1.0, true),
					forklift.new CmdSetForkliftPosition(ForkliftState.SCALE))
			);
		}
		
		addSequential(new CmdDelay(0.5));
		addSequential(forklift.new CmdRunIntake(IntakeState.OUTTAKE, 1000));
		
		addSequential(drive.new CmdMoveForward(-3 * Length.ft, 3000, 0.5));
	}
}
