package org.team3128.autonomous;

import org.team3128.autonomous.util.PowerUpAutoValues;
import org.team3128.common.autonomous.primitives.CmdDelay;
import org.team3128.common.autonomous.primitives.CmdRunInParallel;
import org.team3128.common.autonomous.primitives.CmdRunInSeries;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Forklift.ForkliftState;
import org.team3128.mechanisms.Intake.IntakeState;
import org.team3128.util.PlateAllocation;

public class AutoScaleFromRightTwoCoulombs extends AutoGuidoBase
{
	public AutoScaleFromRightTwoCoulombs(SRXTankDrive drive, Forklift forklift, Direction side, double delay)
	{
		super(drive, forklift, delay);

		if (side == PlateAllocation.getScale()) {
			System.out.println("---------SAME SIDE-------");
			final double arc_distance = PowerUpAutoValues.SCALE_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH - 2.2 * Length.ft;
			
			final float angle = 45f;
			
			final double large_turn_radius = arc_distance * 180 / (angle * Math.PI);
			
			final float small_turn_radius = (float) (arc_distance * 160 / (angle * Math.PI));
			
			final float small_forward = (float) (1 * Length.ft);
			
			final double small_arc_distance = (PowerUpAutoValues.SWITCH_SCALE_DISTANCE - PowerUpAutoValues.SWITCH_BACK_DISTANCE) / (2 * Length.ft);
					
			// (pi/180) * r = d
			
			addSequential(new CmdRunInParallel(
					drive.new CmdFancyArcTurn(large_turn_radius, angle, 10000, side.opposite(), 1.0),
					
					new CmdRunInSeries(
							new CmdDelay(2),
							forklift.new CmdSetForkliftPosition(ForkliftState.SCALE)),
					
					forklift.new CmdRunIntake(IntakeState.OUTTAKE, 1000)
					
					)
					
			);
			
			addSequential(new CmdRunInParallel(
					
					forklift.new CmdRunIntake(IntakeState.STOPPED, 1000),
					
					drive.new CmdMoveForward(-small_arc_distance, 2000, true),
					
					forklift.new CmdSetForkliftPosition(ForkliftState.GROUND),
					
					drive.new CmdFancyArcTurn(small_arc_distance, small_turn_radius, 4000, side.opposite())
					
					)
					
			);
			
			addSequential(new CmdRunInParallel(
							
					forklift.new CmdRunIntake(IntakeState.INTAKE, 2000),
							
					drive.new CmdMoveForward(small_forward, 1250, true),
					
					new CmdRunInSeries(
					
						new CmdDelay(1),
						forklift.new CmdRunIntake(IntakeState.STOPPED, 1000)
							
					)
		
					)
			);
			
			addSequential(new CmdRunInParallel(
					
					drive.new CmdFancyArcTurn(-small_arc_distance, small_turn_radius, 4000, side.opposite()),
					
					forklift.new CmdSetForkliftPosition(ForkliftState.SCALE),
					
					drive.new CmdMoveForward(small_arc_distance, 2000, true),
					
					forklift.new CmdRunIntake(IntakeState.OUTTAKE, 1000),
					
					drive.new CmdMoveForward(-small_arc_distance, 2000, true)
					
					)	
				);
		}
		else {
			/*
			final double vertical = PowerUpAutoValues.SWITCH_FRONT_DISTANCE - PowerUpAutoValues.ROBOT_LENGTH/2 + 0.7*Length.ft;
			final double turn_1 = (PowerUpAutoValues.SWITCH_BACK_DISTANCE - PowerUpAutoValues.SWITCH_FRONT_DISTANCE) / 2;

			final double horiz_distance = PowerUpAutoValues.SCALE_WIDTH - 3 * Length.ft;
						
			
			addSequential(drive.new CmdMoveForward(vertical, 10000, true, 1.0));
			addSequential(drive.new CmdFancyArcTurn(turn_1, 95, 5000, side.opposite(), 1.0, true));
			addSequential(drive.new CmdMoveForward(horiz_distance, 4000, true, 1.0));
			
			addSequential(drive.new CmdFancyArcTurn(PowerUpAutoValues.ROBOT_WIDTH + 2 * Length.in, 95, 2000, side, 1.0, true));
			
			addSequential(new CmdRunInParallel(
					drive.new CmdMoveForward(3 * Length.ft, 2000, false, 1.0),
					forklift.new CmdSetForkliftPosition(ForkliftState.SCALE))
			);
			*/
		}
		
		/*
		addSequential(forklift.new CmdRunIntake(IntakeState.OUTTAKE, 1000));
		
		addSequential(drive.new CmdMoveForward(-1 * Length.ft, 500, 1.0));
		*/
	}
}
