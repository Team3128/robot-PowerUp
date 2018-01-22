/*
 * Date: 1/12/2018
 * Description: Setup teleop/auto for competition
 *
 */

package org.team3128.main;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class MainGuidoCompetition extends MainGuido {

	public MainGuidoCompetition() {
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