/*
 * Date: 1/12/2018
 * Description: Setup teleop/auto for competition
 *
 */

package org.team3128.main;

import org.team3128.common.hardware.misc.Piston;
import org.team3128.common.listener.controltypes.Button;
import org.team3128.common.util.units.Length;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class MainGuidoCompetition extends MainGuido {
	public MainGuidoCompetition() {
		super();
	}

	@Override
	protected void constructHardware() {
		limitSiwtchLocation = 0;
		
		wheelCirc = 12.68 * Length.in;
		
		gearshiftPiston = new Piston(3, 4);
		intakePiston = new Piston(1, 6);
		
		climberPiston = new Piston(2, 5);
		climberLockPiston = new Piston(0, 7);
		
		climberLockPiston.setPistonOff();
		climberPiston.setPistonOn();
		//intakePiston.invertPiston();
		
		intakeInverted = true;
		
		super.constructHardware();
		forklift.maxHeight = 17000;
	}

	@Override
	protected void setupListeners() {
		
		super.setupListeners();
		
		listenerLeft.nameControl(new Button(8), "BrakeClimber");
		listenerLeft.addButtonDownListener("BrakeClimber", ()->{
			climberLockPiston.invertPiston();
		});
		listenerRight.nameControl(new Button(7), "DeployBar");
		listenerRight.addButtonDownListener("DeployBar", ()->{
			climberPiston.invertPiston();
		});
	}

	protected void constructAutoPrograms(SendableChooser<CommandGroup> programChooser) {
		super.constructAutoPrograms(programChooser);
	}

	@Override
	protected void teleopInit() {
		rightDriveFollower.setInverted(true);
		rightDriveLeader.setInverted(true);
		
		
		super.teleopInit();
	}

	@Override
	protected void autonomousInit() {
		super.autonomousInit();
	}
}