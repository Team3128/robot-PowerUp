/*
 *     
 * Date: 1/12/2018
 * Description: Setup teleop and autonomous modes for 2018 MainGuido for testing purposes
 *
 * 
 */

package org.team3128.main;

import org.team3128.autonomous.CalibrateRunPID;
import org.team3128.common.NarwhalRobot;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.hardware.misc.Piston;
import org.team3128.common.hardware.misc.TwoSpeedGearshift;
import org.team3128.common.hardware.motor.NarwhalSRX;
import org.team3128.common.hardware.motor.NarwhalSRX.Reverse;
import org.team3128.common.listener.ListenerManager;
import org.team3128.common.listener.controllers.ControllerExtreme3D;
import org.team3128.common.listener.controltypes.Button;
import org.team3128.common.listener.controltypes.POV;
import org.team3128.common.util.Constants;
import org.team3128.common.util.Log;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Forklift.ForkliftState;
import org.team3128.mechanisms.Intake;
import org.team3128.mechanisms.Intake.IntakeState;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MainGuido extends NarwhalRobot {
	
	// Drive Train
	public double wheelCirc = 12.68 * Length.in;
	public SRXTankDrive drive;
	public NarwhalSRX leftDriveLeader, leftDriveFollower;
	public NarwhalSRX rightDriveLeader, rightDriveFollower;
	
	public TwoSpeedGearshift gearshift;
	public Piston gearshiftPiston;
	
	// Pneumatics
	public Compressor compressor;
	
	// Forklift
	Forklift forklift;
	public TalonSRX forkliftMotorLeader, forkliftMotorFollower;
	DigitalInput forkliftSoftStopLimitSwitch;
		
	// Intake
	Intake intake;
	IntakeState intakeState;
	public VictorSPX intakeMotorLeader, intakeMotorFollower;
	DigitalInput intakeLimitSwitch;
	
	// Controls
	public ListenerManager listenerRight;
	public ListenerManager listenerLeft;

	public Joystick leftJoystick;
	public Joystick rightJoystick;

	// Misc(general)
	public PowerDistributionPanel powerDistPanel;
	public double shiftUpSpeed = 100;
	public double shiftDownSpeed = 200;
	
	@Override
	protected void constructHardware() {
		// Drive Train Setup
		leftDriveLeader = new NarwhalSRX(20, Reverse.ENCODER, Reverse.NONE);
		leftDriveFollower = new NarwhalSRX(21, Reverse.OUTPUT, Reverse.NONE);
		rightDriveLeader = new NarwhalSRX(10, Reverse.BOTH, Reverse.OUTPUT);
		rightDriveFollower = new NarwhalSRX(11, Reverse.OUTPUT, Reverse.NONE);
		
		//rightDrive1.setInverted(false);
		//rightDrive2.setInverted(false);
		//leftDrive1.setInverted(true);
		//leftDrive2.setInverted(true);

		// set Leaders
		leftDriveLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);
		rightDriveLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);

		// set Followers
		leftDriveFollower.set(ControlMode.Follower, leftDriveLeader.getDeviceID());
		rightDriveFollower.set(ControlMode.Follower, rightDriveLeader.getDeviceID());

		// create SRXTankDrive
		drive = new SRXTankDrive(leftDriveLeader, rightDriveLeader, wheelCirc, 1, 25.25 * Length.in, 30.5 * Length.in, 3600);
		
		gearshiftPiston = new Piston(2, 7);
		gearshift = new TwoSpeedGearshift(false, gearshiftPiston);
		
		drive.addShifter(gearshift, shiftUpSpeed, shiftDownSpeed);
		
		
		// create intake
		intakeState = Intake.IntakeState.STOPPED;
		intakeMotorLeader = new VictorSPX(1);
		intakeMotorFollower = new VictorSPX(2);
		
		intakeMotorLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);
		intakeMotorFollower.set(ControlMode.Follower, intakeMotorLeader.getDeviceID());
		
		compressor = new Compressor();
		
		//intakeLimitSwitch = new DigitalInput(2);
		intake = new Intake(intakeMotorLeader, intakeState);
		
		// create forklift
		forkliftMotorLeader = new TalonSRX(30);
		forkliftMotorFollower = new TalonSRX(31);
		forkliftSoftStopLimitSwitch = new DigitalInput(1);
		forklift = new Forklift(ForkliftState.GROUND, intake, forkliftMotorLeader, forkliftSoftStopLimitSwitch);
		
		// instantiate PDP
		powerDistPanel = new PowerDistributionPanel();

		// set Listeners
		leftJoystick = new Joystick(1);
		listenerLeft = new ListenerManager(leftJoystick);
		addListenerManager(listenerLeft);
		
		rightJoystick = new Joystick(0);
		listenerRight = new ListenerManager(rightJoystick);
		addListenerManager(listenerRight);
		
	}

	@Override
	protected void setupListeners() {
		listenerRight.nameControl(ControllerExtreme3D.JOYY, "MoveForwards");
		listenerRight.nameControl(ControllerExtreme3D.TWIST, "MoveTurn");
		listenerRight.nameControl(ControllerExtreme3D.THROTTLE, "Throttle");

		listenerRight.addMultiListener(() -> {
			Log.info("MainGuido", "Multi Listener Entered");
			double x = listenerRight.getAxis("MoveForwards");
			double y = listenerRight.getAxis("MoveTurn");
			double t = listenerRight.getAxis("Throttle") * -1;
			
			drive.arcadeDrive(x, y, t, true);
		}, "MoveForwards", "MoveTurn", "Throttle");
		
		listenerRight.nameControl(new Button(2), "GearShift");
		listenerRight.addButtonDownListener("GearShift", drive::shift);

		
		listenerRight.nameControl(new POV(0), "ForkliftIntakePOV");
		listenerRight.addListener("ForkliftIntakePOV", forklift::onPOVUpdate);
		
		listenerRight.nameControl(new Button(1), "SwitchOuttake");
		listenerRight.addButtonDownListener("SwitchOuttake", forklift::switchOuttake);
		listenerRight.addButtonUpListener("SwitchOuttake", forklift::rest);
		
		listenerRight.nameControl(new Button(11), "StartCompressor");
		listenerRight.addButtonDownListener("StartCompressor", () -> {
			compressor.start();
		});
		
		listenerRight.nameControl(new Button(12), "StopCompressor");
		listenerRight.addButtonDownListener("StopCompressor", () -> {
			compressor.stop();
		});
		
		listenerLeft.nameControl(new Button(11), "ClearStickyFaults");
		listenerLeft.addButtonDownListener("ClearStickyFaults", powerDistPanel::clearStickyFaults);	
		
		listenerLeft.nameControl(ControllerExtreme3D.JOYX, "ForkliftTest");
		listenerLeft.addListener("ForkliftTest", (double joyY) -> {
			forkliftMotorFollower.set(ControlMode.PercentOutput, joyY);
		});
	}

	protected void constructAutoPrograms(SendableChooser<CommandGroup> programChooser)
	{
		programChooser.addDefault("None", null);
		programChooser.addObject("100 Inch Run", new CalibrateRunPID(this));
	}

	@Override
	protected void teleopInit()
	{
		leftDriveLeader.setSensorPhase(true);
		
		rightDriveLeader.setInverted(true);
		rightDriveLeader.setSensorPhase(true);
		
		leftDriveLeader.setSelectedSensorPosition(0, 0, Constants.CAN_TIMEOUT);
	}

	@Override
	protected void autonomousInit()
	{
		leftDriveLeader.setInverted(false);
		rightDriveLeader.setInverted(false);
		
		leftDriveLeader.setSensorPhase(true);
		
		rightDriveLeader.setInverted(true);
		rightDriveLeader.setSensorPhase(true);
	}
	
	@Override
	protected void updateDashboard()
	{
		SmartDashboard.putNumber("Left Speed (nu/100ms)", leftDriveLeader.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Speed (nu/100ms)", rightDriveLeader.getSelectedSensorPosition(0));
	}
}