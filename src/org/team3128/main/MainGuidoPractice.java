/*
 * Date: 1/12/2018
 * Description: Setup teleop/auto modes for testing purposes
 *
 */

package org.team3128.main;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class MainGuidoPractice extends MainGuido {

	public MainGuidoPractice() {
		super();
	}

	@Override
	protected void constructHardware() {
		super.constructHardware();
	}

	@Override
	protected void setupListeners() {
		super.setupListeners();
	}

	protected void constructAutoPrograms(SendableChooser<CommandGroup> programChooser) {
		super.constructAutoPrograms(programChooser);
	}

	@Override
	protected void teleopInit() {
		super.teleopInit();
	}

	@Override
	protected void autonomousInit() {
		super.autonomousInit();
	}
}