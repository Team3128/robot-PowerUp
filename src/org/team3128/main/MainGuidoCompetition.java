/*
 * Created By: Adham Elarabawy
   Edited By: Eli Smith
   
 * Date: 1/12/2018
 * Description: Setup teleop and autonomous(100 in drive) modes for 2018 PreBot for testing purposes
 *
 *
 * Barely Edited with Minimal Effort, by: Aiden Zhang
 * 
 * Date: Doesn't Matter
 * Description: Removed 2 motors. Fixed trash formatting and naming created by Dad. The end.
 * 
 */

package org.team3128.main;

import org.team3128.autonomous.CalibrateRunPID;
import org.team3128.autonomous.AutoPlaceBlockSwitch_Left;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Intake;
import org.team3128.mechanisms.Forklift.State;
import org.team3128.common.NarwhalRobot;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.listener.ListenerManager;
import org.team3128.common.listener.controllers.ControllerExtreme3D;
import org.team3128.common.listener.controltypes.Button;
import org.team3128.common.util.Constants;
import org.team3128.common.util.Log;
import org.team3128.common.util.datatypes.PIDConstants;
import org.team3128.common.util.units.Length;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
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

	public void switchFullSpeed() {
		super.switchFullSpeed();
	}
}