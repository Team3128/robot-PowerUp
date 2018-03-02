package org.team3128.autonomous;

import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Forklift.ForkliftState;
import org.team3128.mechanisms.Intake.IntakeState;
import org.team3128.util.PlateAllocation;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoScaleFromCenter extends CommandGroup {
	public AutoScaleFromCenter(SRXTankDrive drive, Forklift forklift) {
		final double robot_center_offset = PowerUpAutoValues.ROBOT_WIDTH / 2 - PowerUpAutoValues.CENTER_OFFSET;
		final double alliance_wall_edge = 132 * Length.in;
		
		double horizontal_distance;

		if (PlateAllocation.nearSwitch == Direction.RIGHT) {
			horizontal_distance = alliance_wall_edge - robot_center_offset - PowerUpAutoValues.ROBOT_WIDTH / 2;
		} else {
			horizontal_distance = alliance_wall_edge + robot_center_offset - PowerUpAutoValues.ROBOT_WIDTH / 2;
		}

		final double turn_0 = 60 * Length.in;
		final double turn_1 = horizontal_distance - turn_0;
		final double turn_2 = alliance_wall_edge - PowerUpAutoValues.ROBOT_WIDTH / 2 - (PowerUpAutoValues.SCALE_WIDTH / 2 - PowerUpAutoValues.ROBOT_LENGTH / 2);
		final double vertical_travel = PowerUpAutoValues.SCALE_DISTANCE - horizontal_distance - turn_2;

		addSequential(drive.new CmdFancyArcTurn(turn_0, 90, 5000, PlateAllocation.scale, 0.8));
		addSequential(drive.new CmdFancyArcTurn(turn_1, 90, 5000, PlateAllocation.scale.opposite(), 0.8));
		
		addSequential(drive.new CmdMoveForward(vertical_travel, 5000, 0.8));
		
		addSequential(drive.new CmdFancyArcTurn(turn_2, 90, 5000, PlateAllocation.scale.opposite(), 0.8));

		addSequential(forklift.new CmdSetForkliftPosition(ForkliftState.SCALE));
		addSequential(forklift.new CmdRunIntake(IntakeState.OUTTAKE));
	}
}
