/*
 *     
 * Date: 1/12/2018
 * Description: Setup teleop and autonomous(100 in drive) modes for 2018 PreBot for testing purposes
 *
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

public class MainGuido extends NarwhalRobot {
	
	// Drive Train
	public double wheelDiameter;
	public SRXTankDrive drive;
	public TalonSRX leftDrive1, leftDrive2;
	public TalonSRX rightDrive1, rightDrive2;
	public boolean fullSpeed = false;
	
	// Forklift
	Forklift forklift;
	public TalonSRX leader, follower;
	DigitalInput forkLimSwitch;
	Intake intake;
	
	// Controls
	public ListenerManager listenerRight;
	public ListenerManager listenerLeft;

	public Joystick leftJoystick;
	public Joystick rightJoystick;

	// Misc(general)
	public PowerDistributionPanel powerDistPanel;

	@Override
	protected void constructHardware() {
		// Drive Train Setup
		leftDrive1 = new TalonSRX(1);
		leftDrive2 = new TalonSRX(2);
		rightDrive1 = new TalonSRX(3);
		rightDrive2 = new TalonSRX(4);
		
		rightDrive1.setInverted(true);

		// set Leaders
		leftDrive1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);
		rightDrive1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);

		// set Followers
		leftDrive2.set(ControlMode.Follower, leftDrive1.getDeviceID());
		rightDrive2.set(ControlMode.Follower, rightDrive1.getDeviceID());

		// create SRXTankDrive
		drive = new SRXTankDrive(leftDrive1, rightDrive1, wheelDiameter * Math.PI, 1, 25.25 * Length.in, 30.5 * Length.in, 400);
		
		// create forklift
		forklift = new Forklift(Forklift.State.GROUND, intake, leader, follower, forkLimSwitch);

		// instantiate PDP
		powerDistPanel = new PowerDistributionPanel();

		// set Listeners
		leftJoystick = new Joystick(0);
		listenerLeft = new ListenerManager(leftJoystick);
		addListenerManager(listenerLeft);
		
		rightJoystick = new Joystick(1);
		listenerRight = new ListenerManager(rightJoystick);
		addListenerManager(listenerRight);
		
	}

	@Override
	protected void setupListeners() {
		//name controls
		listenerRight.nameControl(ControllerExtreme3D.JOYX, "moveX");
		listenerRight.nameControl(ControllerExtreme3D.TWIST, "moveTurn");
		listenerRight.nameControl(ControllerExtreme3D.THROTTLE, "Throttle");
		listenerRight.nameControl(new Button(1), "fullSpeed");
		
		//debug
		Log.info("MainGuido", "Controllers Named");

		//get Joy-stick data
		listenerRight.addMultiListener(() -> {
			Log.info("MainPreBot", "Multi Listener Entered");
			double x = listenerRight.getAxis("moveX");
			double y = listenerRight.getAxis("moveTurn");
			double t = listenerRight.getAxis("Throttle") * -1;
			
			drive.arcadeDrive(x, y, t, true);
		}, "moveX", "moveTurn", "Throttle", "fullSpeed");

		listenerRight.addButtonDownListener("fullSpeed", this::switchFullSpeed);
	
	}

	protected void constructAutoPrograms(SendableChooser<CommandGroup> programChooser) {
		programChooser.addDefault("None", null);
		programChooser.addObject("Calibrate PID", new CalibrateRunPID(drive));
		programChooser.addObject("[Left] Switch Block", new AutoPlaceBlockSwitch_Left(drive, forklift));
	}

	@Override
	protected void teleopInit() {
		// set fullSpeed to true
		fullSpeed = true;
	}

	@Override
	protected void autonomousInit() {
		// set fullSpeed to false
		fullSpeed = false;
	}

	public void switchFullSpeed() {
		//switch fullSpeed
		fullSpeed = !fullSpeed;
	}
}