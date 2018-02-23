package org.team3128.autonomous;

import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.Log;
import org.team3128.common.util.enums.Direction;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Forklift.ForkliftState;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoPlaceBlockSwitch_Side extends CommandGroup {
	public AutoPlaceBlockSwitch_Side(SRXTankDrive drive, Forklift forklift, String startSide) {

		PowerUpAutoValues autoValues = new PowerUpAutoValues();
		
<<<<<<< Updated upstream
		final double switchDistance = autoValues.SWITCH_VERTICAL_OFFSET; // vertical distance between startPos and switch
		final double horizOffset = autoValues.CENTER_OFFSET; // distance between start position horizontal and switch horizontal
		final double switchWidth = autoValues.SWITCH_VERTICAL_OFFSET; // width of the switch
=======
		final double switchDistance = autoValues.SWITCH_DISTANCE; // vertical distance between startPos and switch
		final double horizOffset = autoValues.SIWTCH_HORIZONAL_OFFSET; // distance between start position horizontal and switch horizontal
		final double switchWidth = autoValues.SWITCH_DISTANCE; // width of the switch
>>>>>>> Stashed changes
		
		startSide = startSide.toUpperCase();

		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		if (startSide.charAt(0) == 'L') {
			if (gameData.charAt(0) == 'L') {
				addSequential(drive.new CmdMoveForward(switchDistance, 4000, 0.75)); // move to switch vertical
				Log.info("MainGuido(AUTO)", "[1]reached switch vertical");
	
				addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); // turn right
				addSequential(drive.new CmdMoveForward(horizOffset, 2000, 0.75)); // move to switch horizontal
				Log.info("MainGuido(AUTO)", "[2]reached switch horizontal");
	
				addSequential(forklift.new CmdSetForkliftPosition(ForkliftState.SWITCH));
				Log.info("MainGuido(AUTO)", "[3]outtake activated");
	
			} else if (gameData.charAt(0) == 'R') {
				addSequential(drive.new CmdMoveForward(switchDistance / 2, 4000, 0.75)); // drive forward
				addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); // turn right
				addSequential(drive.new CmdMoveForward(horizOffset * 2 + switchWidth, 2000, 0.75)); // across field
				addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); // turn left
				addSequential(drive.new CmdMoveForward(switchDistance / 2, 2000, 0.75)); // move to switch vertical
				Log.info("MainGuido(AUTO)", "[1]reached switch vertical");
	
				addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); // turn left
				addSequential(drive.new CmdMoveForward(horizOffset, 2000, 0.75)); // move to switch horizontal
				Log.info("MainGuido(AUTO)", "[2]reached switch horizontal");
	
				addSequential(forklift.new CmdSetForkliftPosition(ForkliftState.SWITCH));
				Log.info("MainGuido(AUTO)", "[3]outtake activated");
			}
		} else if(startSide.charAt(0) == 'R') {
			if (gameData.charAt(0) == 'R') {
				addSequential(drive.new CmdMoveForward(switchDistance, 4000, 0.75)); // move to switch vertical
				Log.info("MainGuido(AUTO)", "[1]reached switch vertical");

				addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); // turn left
				addSequential(drive.new CmdMoveForward(horizOffset, 2000, 0.75)); // move to switch horizontal
				Log.info("MainGuido(AUTO)", "[2]reached switch horizontal");

				addSequential(forklift.new CmdSetForkliftPosition(ForkliftState.SWITCH));
				Log.info("MainGuido(AUTO)", "[3]outtake activated");

			} else if (gameData.charAt(0) == 'L') {
				addSequential(drive.new CmdMoveForward(switchDistance / 2, 4000, 0.75)); // drive forward
				addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.LEFT)); // turn left
				addSequential(drive.new CmdMoveForward(horizOffset * 2 + switchWidth, 2000, 0.75)); // across field
				addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); // turn right
				addSequential(drive.new CmdMoveForward(switchDistance / 2, 2000, 0.75)); // move to switch vertical
				Log.info("MainGuido(AUTO)", "[1]reached switch vertical");

				addSequential(drive.new CmdInPlaceTurn(90, 0.75, 1500, Direction.RIGHT)); // turn right
				addSequential(drive.new CmdMoveForward(horizOffset, 2000, 0.75)); // move to switch horizontal
				Log.info("MainGuido(AUTO)", "[2]reached switch horizontal");

				addSequential(forklift.new CmdSetForkliftPosition(ForkliftState.SWITCH));
				Log.info("MainGuido(AUTO)", "[3]outtake activated");
			}
		}
	}
}
