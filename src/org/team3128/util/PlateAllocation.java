package org.team3128.util;

import org.team3128.common.util.enums.Direction;

import edu.wpi.first.wpilibj.DriverStation;

public class PlateAllocation {
	public static Direction nearSwitch, scale, farSwitch;
	
	private static String gameData = "";
	
	public static void update() {
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		if (gameData.length() == 3) {
			nearSwitch	 = (gameData.charAt(0) == 'R') ? Direction.RIGHT : Direction.LEFT;
			scale		 = (gameData.charAt(1) == 'R') ? Direction.RIGHT : Direction.LEFT;
			farSwitch	 = (gameData.charAt(2) == 'R') ? Direction.RIGHT : Direction.LEFT;
		}
	}
}
