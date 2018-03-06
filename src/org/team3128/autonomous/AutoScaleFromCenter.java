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

public class AutoScaleFromCenter extends AutoGuidoBase {
	public AutoScaleFromCenter(SRXTankDrive drive, Forklift forklift) {
		super(drive, forklift);
		
		final double robot_center_offset = PowerUpAutoValues.ROBOT_WIDTH / 2 - PowerUpAutoValues.CENTER_OFFSET;
		final double alliance_wall_edge = 132 * Length.in;
		
		double horizontal_distance;

		if (PlateAllocation.getNearSwitch() == Direction.RIGHT) {
			horizontal_distance = alliance_wall_edge - robot_center_offset - PowerUpAutoValues.ROBOT_WIDTH / 2;
		} else {
			horizontal_distance = alliance_wall_edge + robot_center_offset - PowerUpAutoValues.ROBOT_WIDTH / 2;
		}

		final double turn_0 = 60 * Length.in;
		final double turn_1 = horizontal_distance - turn_0;
		final double turn_2 = alliance_wall_edge - PowerUpAutoValues.ROBOT_WIDTH / 2 - (PowerUpAutoValues.SCALE_WIDTH / 2 - PowerUpAutoValues.ROBOT_LENGTH / 2);
		final double vertical_travel = PowerUpAutoValues.SCALE_DISTANCE - horizontal_distance - turn_2;

		addSequential(drive.new CmdFancyArcTurn(turn_0, 85, 5000, PlateAllocation.getScale(), 1.0, true));
		if (PlateAllocation.getScale().opposite() == Direction.RIGHT) {
			addSequential(drive.new CmdFancyArcTurn(turn_1, 83, 5000, Direction.RIGHT, 1.0, true));
		}
		else {
			addSequential(drive.new CmdFancyArcTurn(turn_1, 97, 5000, Direction.LEFT, 1.0, true));
		}
		
		
		addSequential(drive.new CmdMoveForward(vertical_travel - 5 * Length.ft, 5000, true, 1.0));
		
		addSequential(new CmdRunInParallel(
				drive.new CmdFancyArcTurn(turn_2, 92, 5000, PlateAllocation.getScale().opposite(), 1.0, false),
				forklift.new CmdSetForkliftPosition(ForkliftState.SCALE)
		));

		
		addSequential(forklift.new CmdRunIntake(IntakeState.OUTTAKE, 500));
	}
}
