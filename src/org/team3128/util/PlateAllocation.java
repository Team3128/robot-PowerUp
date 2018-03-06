package org.team3128.util;

import org.team3128.common.util.enums.Direction;

import edu.wpi.first.wpilibj.DriverStation;

public class PlateAllocation {
	private static Direction nearSwitch = Direction.RIGHT;
	private static Direction scale = Direction.RIGHT;
	private static Direction farSwitch = Direction.RIGHT;
	
	private static String gameData = "";
	public static boolean fetched = false;
	
	public static void update() {
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		if (gameData.length() == 3) {
			nearSwitch	 = (gameData.charAt(0) == 'R') ? Direction.RIGHT : Direction.LEFT;
			scale		 = (gameData.charAt(1) == 'R') ? Direction.RIGHT : Direction.LEFT;
			farSwitch	 = (gameData.charAt(2) == 'R') ? Direction.RIGHT : Direction.LEFT;
			
			fetched = true;
		}
	}
	
	public static Direction getNearSwitch() {
		return nearSwitch;
	}
	
	public static Direction getScale() {
		return scale;
	}
	
	public static Direction getFarSwitch() {
		return farSwitch;
	}
}
