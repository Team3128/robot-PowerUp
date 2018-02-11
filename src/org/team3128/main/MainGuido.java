/*
 *     
 * Date: 1/12/2018	
 * Description: Setup teleop and autonomous modes for 2018 MainGuido for testing purposes
 *
 * 
 */

package org.team3128.main;

import org.team3128.autonomous.AutoCrossBaseline_Middle;
import org.team3128.autonomous.CalibrateForkliftPID;
import org.team3128.autonomous.CalibrateRunPID;
import org.team3128.common.NarwhalRobot;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.hardware.misc.Piston;
import org.team3128.common.hardware.misc.TwoSpeedGearshift;
import org.team3128.common.hardware.motor.NarwhalSRX;
import org.team3128.common.hardware.motor.NarwhalSRX.Reverse;
import org.team3128.common.listener.ListenerManager;
import org.team3128.common.listener.POVValue;
import org.team3128.common.listener.controllers.ControllerExtreme3D;
import org.team3128.common.listener.controltypes.Button;
import org.team3128.common.listener.controltypes.POV;
import org.team3128.common.util.Constants;
import org.team3128.common.util.Log;
import org.team3128.common.util.datatypes.PIDConstants;
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

public class MainGuido extends NarwhalRobot
{

	// Drive Train
	public double wheelCirc;
	public SRXTankDrive drive;
	public NarwhalSRX leftDriveLeader, leftDriveFollower;
	public NarwhalSRX rightDriveLeader, rightDriveFollower;

	public TwoSpeedGearshift gearshift;
	public Piston gearshiftPiston;

	// Pneumatics
	public Compressor compressor;

	// Forklift
	public Forklift forklift;
	public TalonSRX forkliftMotorLeader, forkliftMotorFollower;
	DigitalInput forkliftSoftStopLimitSwitch;

	public PIDConstants positionUpwardsPID, positionDownwardsPID, velocityPID;

	public int limitSiwtchLocation, forkliftMaxVelocity;

	// Intake
	Intake intake;
	IntakeState intakeState;
	Piston intakePiston;
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

	public int max_speed = 0;

	public long startTimeMillis = 0;

	@Override
	protected void constructHardware()
	{
		// Drive Train Setup
		leftDriveLeader = new NarwhalSRX(20, Reverse.ENCODER, Reverse.NONE);
		leftDriveFollower = new NarwhalSRX(21, Reverse.OUTPUT, Reverse.NONE);
		rightDriveLeader = new NarwhalSRX(10, Reverse.BOTH, Reverse.OUTPUT);
		rightDriveFollower = new NarwhalSRX(11, Reverse.OUTPUT, Reverse.NONE);

		// rightDrive1.setInverted(false);
		// rightDrive2.setInverted(false);
		// leftDrive1.setInverted(true);
		// leftDrive2.setInverted(true);

		// set Leaders
		leftDriveLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);
		rightDriveLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
				Constants.CAN_TIMEOUT);

		// set Followers
		leftDriveFollower.set(ControlMode.Follower, leftDriveLeader.getDeviceID());
		rightDriveFollower.set(ControlMode.Follower, rightDriveLeader.getDeviceID());

		// create SRXTankDrive
		drive = new SRXTankDrive(leftDriveLeader, rightDriveLeader, wheelCirc, 1, 25.25 * Length.in, 30.5 * Length.in,
				3600);
		// drive.setLeftSpeedScalar(0.3);

		gearshiftPiston = new Piston(2, 5);
		gearshift = new TwoSpeedGearshift(false, gearshiftPiston);
		drive.addShifter(gearshift, shiftUpSpeed, shiftDownSpeed);

		// create intake
		intakeState = Intake.IntakeState.STOPPED;
		intakeMotorLeader = new VictorSPX(1);
		intakeMotorFollower = new VictorSPX(2);
		intakePiston = new Piston(1, 6);
		// intakePiston.invertPiston();

		intakeMotorFollower.set(ControlMode.Follower, intakeMotorLeader.getDeviceID());

		intakeMotorLeader.setInverted(true);

		compressor = new Compressor();

		// intakeLimitSwitch = new DigitalInput(2);
		intake = new Intake(intakeMotorLeader, intakeState, intakePiston);

		// create forklift
		forkliftMotorLeader = new TalonSRX(30);
		forkliftMotorFollower = new TalonSRX(31);
		forkliftSoftStopLimitSwitch = new DigitalInput(7);

		forkliftMotorLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
				Constants.CAN_TIMEOUT);
		forkliftMotorFollower.set(ControlMode.Follower, forkliftMotorLeader.getDeviceID());

		forklift = new Forklift(ForkliftState.GROUND, intake, forkliftMotorLeader, forkliftSoftStopLimitSwitch,
				limitSiwtchLocation, forkliftMaxVelocity, positionUpwardsPID, positionDownwardsPID, velocityPID);

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
	protected void setupListeners()
	{
		listenerRight.nameControl(ControllerExtreme3D.JOYY, "MoveForwards");
		listenerRight.nameControl(ControllerExtreme3D.TWIST, "MoveTurn");
		listenerRight.nameControl(ControllerExtreme3D.THROTTLE, "Throttle");

		listenerRight.addMultiListener(() ->
		{
			double x = listenerRight.getAxis("MoveForwards");
			double y = listenerRight.getAxis("MoveTurn");
			double t = listenerRight.getAxis("Throttle") * -1;
			drive.arcadeDrive(x, y, t, true);
		}, "MoveForwards", "MoveTurn", "Throttle");

		listenerRight.nameControl(new Button(2), "GearShift");
		listenerRight.addButtonDownListener("GearShift", drive::shift);

		listenerRight.nameControl(new POV(0), "IntakePOV");
		listenerRight.addListener("IntakePOV", (POVValue pov) ->
		{
			int val = pov.getDirectionValue();

			switch (val)
			{
			case 7:
			case 8:
			case 1:
				intake.setState(IntakeState.OUTTAKE);
				break;
			case 3:
			case 4:
			case 5:
				intake.setState(IntakeState.INTAKE);
				break;
			default:
				intake.setState(IntakeState.STOPPED);
			}
		});

		listenerRight.nameControl(new Button(5), "ForkliftRightUp");
		listenerRight.addButtonDownListener("ForkliftRightUp", () ->
		{
			forklift.powerControl(1.0);
		});
		listenerRight.addButtonUpListener("ForkliftRightUp", () ->
		{
			forklift.powerControl(0);
		});

		listenerRight.nameControl(new Button(3), "ForkliftRightDown");
		listenerRight.addButtonDownListener("ForkliftRightDown", () ->
		{
			forklift.powerControl(-0.7);
		});
		listenerRight.addButtonUpListener("ForkliftRightDown", () ->
		{
			forklift.powerControl(0.0);
		});

		listenerRight.nameControl(new Button(11), "StartCompressor");
		listenerRight.addButtonDownListener("StartCompressor", () ->
		{
			compressor.start();
			Log.info("MainGuido", "Starting Compressor");

		});

		listenerRight.nameControl(new Button(12), "StopCompressor");
		listenerRight.addButtonDownListener("StopCompressor", () ->
		{
			compressor.stop();
		});

		listenerLeft.nameControl(new Button(11), "ClearStickyFaults");
		listenerLeft.addButtonDownListener("ClearStickyFaults", powerDistPanel::clearStickyFaults);

		listenerLeft.nameControl(ControllerExtreme3D.JOYY, "ForkliftTest");
		listenerLeft.addListener("ForkliftTest", (double joyY) ->
		{
			forklift.powerControl(joyY);
		});

		listenerLeft.nameControl(new POV(0), "IntakePOV");
		listenerLeft.addListener("IntakePOV", (POVValue pov) ->
		{
			int val = pov.getDirectionValue();

			switch (val)
			{
			case 7:
			case 8:
			case 1:
				intake.setState(IntakeState.OUTTAKE);
				break;
			case 3:
			case 4:
			case 5:
				intake.setState(IntakeState.INTAKE);
				break;
			default:
				intake.setState(IntakeState.STOPPED);
				break;
			}
		});
	}

	protected void constructAutoPrograms(SendableChooser<CommandGroup> programChooser)
	{
		programChooser.addDefault("None", null);
		programChooser.addObject("100 Inch Run", new CalibrateRunPID(this));
		programChooser.addObject("Calibrate Forklift PID", new CalibrateForkliftPID(this));
		programChooser.addObject("Auto Cross Baseline", new AutoCrossBaseline_Middle(drive, forklift));
	}

	@Override
	protected void teleopInit()
	{
		forklift.disabled = false;

		leftDriveLeader.setSensorPhase(true);

		rightDriveLeader.setInverted(true);
		rightDriveLeader.setSensorPhase(true);

		intakeState = IntakeState.STOPPED;

		leftDriveLeader.setSelectedSensorPosition(0, 0, Constants.CAN_TIMEOUT);
		gearshift.shiftToHigh();
		if (gearshift.isInHighGear())
		{

			Log.info("MainGuido", "Log: Gearshift is in High Gear");

		}
		else
		{

			Log.info("MainGuido", "Log: Gearshift is in Low Gear");

		}

		startTimeMillis = System.currentTimeMillis();
	}

	@Override
	protected void teleopPeriodic()
	{
		// Log.info("MainGuido", ((System.currentTimeMillis() - startTimeMillis)
		// / 1000.0) + "," + (wheelCirc *
		// rightDriveLeader.getSelectedSensorVelocity(0) * 10.0 / 4096.0));
	}

	@Override
	protected void disabledInit()
	{
		forklift.disabled = true;
	}

	@Override
	protected void autonomousInit()
	{
		forklift.disabled = false;
		leftDriveLeader.setInverted(false);
		rightDriveLeader.setInverted(false);

		leftDriveLeader.setSensorPhase(true);

		rightDriveLeader.setInverted(true);
		rightDriveLeader.setSensorPhase(true);
	}

	@Override
	protected void updateDashboard()
	{
		SmartDashboard.putNumber("Forklift Velocity", forkliftMotorLeader.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Forklift Position", forkliftMotorLeader.getSelectedSensorPosition(0));

		SmartDashboard.putNumber("Current Forklift Error (in)", forklift.error / Length.in);
		SmartDashboard.putNumber("Current Forklift Position (in)", forklift.currentPosition / Length.in);

		SmartDashboard.putString("Forklift Control Mode", forklift.controlMode.getName());
		if (drive.isInHighGear())
		{
			SmartDashboard.putString("Gear", "HIGH GEAR");
		}
		else
		{
			SmartDashboard.putString("Gear", "LOW GEAR");
		}
	}
}