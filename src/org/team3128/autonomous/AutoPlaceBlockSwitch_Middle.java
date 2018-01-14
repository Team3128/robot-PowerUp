package org.team3128.autonomous;

import org.team3128.common.autonomous.movement.CmdTurnGyro;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.Log;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Intake;

public class AutoPlaceBlockSwitch_Middle extends CommandGroup {
	public AutoPlaceBlockSwitch_Middle(SRXTankDrive drive, Forklift forklift) {
		final double autoLineDistance = 160 * Length.in; //distance between startPos and autoLine
		final double switchDistance = 168 * Length.in; //distance between startPos and switch
		final double horizOffset = 30 * Length.in; //distance between start position horizontal and switch horizontal
		final double switchOffset = 100 * Length.in; //robot width
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		if (gameData.charAt(0) == 'L') {
			addSequential(drive.new CmdMoveForward(switchDistance / 2, 4000, 0.75)); //drive forward to autoline
			Log.info("MainGuido(AUTO)", "[1]reached autoLine");
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); //turn left
			addSequential(drive.new CmdMoveForward(horizOffset, 2000, 0.75)); //compensate for horizontal offset
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); //turn right
			addSequential(drive.new CmdMoveForward(switchDistance / 2, 4000, 0.75)); //drive forward to autoline
			addSequential(drive.new CmdMoveForward(switchDistance - autoLineDistance, 2000, 0.75)); //move to switch
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); //move to right position(turn right)
			Log.info("MainGuido(AUTO)", "[2]reached target position");
			//forklift.setState(Forklift.State.SWITCH, Intake.State.OUTTAKE); //push block on to switch
			addSequential(forklift.new CmdForkliftPush(Forklift.State.SWITCH, Intake.intakeState.OUTTAKE));
			Log.info("MainGuido(AUTO)", "[3]outtake activated");
			
		} else if (gameData.charAt(0) == 'R') {
			addSequential(drive.new CmdMoveForward(switchDistance/2, 4000, 0.75)); //drive forward to autoline
			Log.info("MainGuido(AUTO)", "[1]reached autoLine");
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); //turn right
			addSequential(drive.new CmdMoveForward(switchOffset, 2000, 0.75)); //compensate for horizontal offset
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); //turn left
			addSequential(drive.new CmdMoveForward(switchDistance/2, 4000, 0.75)); //drive forward to autoline
			addSequential(drive.new CmdMoveForward(switchDistance - autoLineDistance, 2000, 0.75)); //move to switch
			addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); //turn left
			Log.info("MainGuido(AUTO)", "[2]reached target position");
			//forklift.setState(Forklift.State.SWITCH, Intake.State.OUTTAKE); //push block on to switch
			addSequential(forklift.new CmdForkliftPush(Forklift.State.SWITCH, Intake.intakeState.OUTTAKE));
			Log.info("MainGuido(AUTO)", "[3]outtake activated");
		}

	}
}
