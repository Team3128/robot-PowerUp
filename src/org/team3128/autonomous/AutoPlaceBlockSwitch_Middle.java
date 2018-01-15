package org.team3128.autonomous;

import org.team3128.common.autonomous.movement.CmdTurnGyro;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.drive.SRXTankDrive.CmdInPlaceTurn;
import org.team3128.common.drive.SRXTankDrive.CmdMoveForward;
import org.team3128.common.util.Log;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Intake;
import org.team3128.mechanisms.Forklift.CmdForkliftPush;

public class AutoPlaceBlockSwitch_Middle extends CommandGroup {
	public AutoPlaceBlockSwitch_Middle(SRXTankDrive drive, Forklift forklift) {
		
		AutoValues autoValues = new AutoValues();
		
		final double switchDistance = autoValues.switchDistance; // vertical distance between startPos and switch
		final double horizOffset = autoValues.horizOffset; // distance between start position horizontal and switch horizontal
		final double switchWidth = autoValues.switchDistance; // width of the switch

		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();

		if (gameData.charAt(0) == 'R') {
			addSequential(drive.new CmdMoveForward(switchDistance / 2, 4000, 0.75)); // drive forward
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); // turn right
			addSequential(drive.new CmdMoveForward(horizOffset + switchWidth/2, 2000, 0.75)); // across field
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); // turn left
			addSequential(drive.new CmdMoveForward(switchDistance / 2, 2000, 0.75)); // move to switch vertical
			Log.info("MainGuido(AUTO)", "[1]reached switch vertical");

			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); // turn left
			addSequential(drive.new CmdMoveForward(horizOffset, 2000, 0.75)); // move to switch horizontal
			Log.info("MainGuido(AUTO)", "[2]reached switch horizontal");

			addSequential(forklift.new CmdForkliftPush(Forklift.State.SWITCH, Intake.intakeState.OUTTAKE));
			Log.info("MainGuido(AUTO)", "[3]outtake activated");

		} else if (gameData.charAt(0) == 'L') {
			addSequential(drive.new CmdMoveForward(switchDistance / 2, 4000, 0.75)); // drive forward
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); // turn left
			addSequential(drive.new CmdMoveForward(horizOffset + switchWidth/2, 2000, 0.75)); // across field
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); // turn right
			addSequential(drive.new CmdMoveForward(switchDistance / 2, 2000, 0.75)); // move to switch vertical
			Log.info("MainGuido(AUTO)", "[1]reached switch vertical");

			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); // turn right
			addSequential(drive.new CmdMoveForward(horizOffset, 2000, 0.75)); // move to switch horizontal
			Log.info("MainGuido(AUTO)", "[2]reached switch horizontal");

			addSequential(forklift.new CmdForkliftPush(Forklift.State.SWITCH, Intake.intakeState.OUTTAKE));
			Log.info("MainGuido(AUTO)", "[3]outtake activated");
		}

	}
}
