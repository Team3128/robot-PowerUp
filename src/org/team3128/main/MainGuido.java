/*
 *     
\[] * Date: 1/12/2018	
 * Description: Setup teleop and autonomous modes for 2018 MainGuido for testing purposes
 *
 * 
 */

package org.team3128.main;

import org.team3128.autonomous.AutoArcTurn;
import org.team3128.autonomous.AutoDriveDistance;
import org.team3128.autonomous.AutoScaleDropoffTest;
import org.team3128.autonomous.AutoSetForkliftState;
import org.team3128.autonomous.AutoSwitchFromCenter;
import org.team3128.common.NarwhalRobot;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.hardware.misc.Piston;
import org.team3128.common.hardware.misc.TwoSpeedGearshift;
import org.team3128.common.listener.ListenerManager;
import org.team3128.common.listener.POVValue;
import org.team3128.common.listener.controllers.ControllerExtreme3D;
import org.team3128.common.listener.controltypes.Button;
import org.team3128.common.listener.controltypes.POV;
import org.team3128.common.util.Constants;
import org.team3128.common.util.Log;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Angle;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Forklift.ForkliftState;
import org.team3128.mechanisms.Intake;
import org.team3128.mechanisms.Intake.IntakeState;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.hal.PowerJNI;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MainGuido extends NarwhalRobot
{

	// Drive Train
	public double wheelCirc;
	public SRXTankDrive drive;
	public TalonSRX leftDriveLeader, leftDriveFollower;
	public TalonSRX rightDriveLeader, rightDriveFollower;
	
	public ADXRS450_Gyro gyro;

	public TwoSpeedGearshift gearshift;
	public Piston gearshiftPiston, climberPiston, climberLockPiston;
	
	public double shiftUpSpeed, shiftDownSpeed;

	public int lowGearMaxSpeed;

	// Pneumatics
	public Compressor compressor;

	// Forklift
	public Forklift forklift;
	public TalonSRX forkliftMotorLeader, forkliftMotorFollower;
	DigitalInput forkliftSoftStopLimitSwitch;

	public int limitSiwtchLocation, forkliftMaxVelocity;

	// Intake
	Intake intake;
	IntakeState intakeState;
	Piston intakePiston;
	public VictorSPX intakeMotorLeader, intakeMotorFollower;
	DigitalInput intakeLimitSwitch;
	
	boolean intakeInverted;

	// Controls
	public ListenerManager listenerRight;
	public ListenerManager listenerLeft;

	public Joystick leftJoystick;
	public Joystick rightJoystick;

	// Misc(general)
	public PowerDistributionPanel powerDistPanel;

	public long startTimeMillis = 0;
	
	public DriverStation ds;
	public RobotController rc;

	@Override
	protected void constructHardware()
	{
		// Drive Train Setup
		leftDriveLeader = new TalonSRX(20);
		leftDriveFollower = new TalonSRX(21);
		rightDriveLeader = new TalonSRX(10);
		rightDriveFollower = new TalonSRX(11);

		// set Leaders
		leftDriveLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);
		rightDriveLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
				Constants.CAN_TIMEOUT);

		// set Followers
		leftDriveFollower.set(ControlMode.Follower, leftDriveLeader.getDeviceID());
		rightDriveFollower.set(ControlMode.Follower, rightDriveLeader.getDeviceID());

		gyro = new ADXRS450_Gyro();
		
		// create SRXTankDrive
		drive = new SRXTankDrive(leftDriveLeader, rightDriveLeader, wheelCirc, 1, 25.25 * Length.in, 30.5 * Length.in,
				lowGearMaxSpeed);
		
		shiftUpSpeed = 5.0 * Length.ft * 60 / wheelCirc;
		shiftDownSpeed = 4.0 * Length.ft * 60 / wheelCirc;
		
		gearshift = new TwoSpeedGearshift(false, gearshiftPiston);
		drive.addShifter(gearshift, shiftUpSpeed, shiftDownSpeed);

		// create intake
		intakeState = Intake.IntakeState.STOPPED;
		intakeMotorLeader = new VictorSPX(1);
		intakeMotorFollower = new VictorSPX(2);

		intakeMotorFollower.set(ControlMode.Follower, intakeMotorLeader.getDeviceID());
		intakeMotorLeader.setInverted(true);

		compressor = new Compressor();

		// intakeLimitSwitch = new DigitalInput(2);
		intake = new Intake(intakeMotorLeader, intakeState, intakePiston, intakeInverted);

		// create forklift
		forkliftMotorLeader = new TalonSRX(30);
		forkliftMotorFollower = new TalonSRX(31);

		forkliftMotorLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
				Constants.CAN_TIMEOUT);
		forkliftMotorFollower.set(ControlMode.Follower, forkliftMotorLeader.getDeviceID());

		forklift = new Forklift(ForkliftState.GROUND, intake, forkliftMotorLeader, forkliftSoftStopLimitSwitch,
				limitSiwtchLocation, forkliftMaxVelocity);

		// instantiate PDP
		powerDistPanel = new PowerDistributionPanel();

		// set Listeners
		leftJoystick = new Joystick(1);
		listenerLeft = new ListenerManager(leftJoystick);
		addListenerManager(listenerLeft);

		rightJoystick = new Joystick(0);
		listenerRight = new ListenerManager(rightJoystick);
		addListenerManager(listenerRight);

		ds = DriverStation.getInstance();
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
		
		listenerLeft.nameControl(new Button(7), "ZeroForklift");
		listenerLeft.addButtonDownListener("ZeroForklift", () ->
		{
			forkliftMotorLeader.setSelectedSensorPosition(0, 0, Constants.CAN_TIMEOUT);
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
		
		listenerLeft.nameControl(new Button(11), "ReZero");
		listenerLeft.addButtonDownListener("ReZero", () -> {
			forklift.rezero = true;
			forklift.powerControl(-0.5);
		});
		listenerLeft.addButtonUpListener("ReZero", () -> {
			forklift.rezero = false;
			forklift.powerControl(0);
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
		
//		listenerLeft.nameControl(new Button(9), "FullDrive");
//		listenerLeft.addButtonDownListener("FullDrive", () -> {
//			drive.arcadeDrive(-1.0, 0, 1.0, true);
//		});
//		listenerLeft.addButtonUpListener("FullDrive", () -> {
//			drive.arcadeDrive(0, 0, 1.0, true);
//		});
	}

	protected void constructAutoPrograms(SendableChooser<CommandGroup> programChooser)
	{
		programChooser.addDefault("None", null);
		programChooser.addObject("Drive 50 Inches", new AutoDriveDistance(this, 50 * Length.in));
		programChooser.addObject("Drive 75 Inches", new AutoDriveDistance(this, 75 * Length.in));
		programChooser.addObject("Drive 100 Inches", new AutoDriveDistance(this, 100 * Length.in));
		programChooser.addObject("Drive 125 Inches", new AutoDriveDistance(this, 125 * Length.in));
		
		programChooser.addObject("Arc Turn Left 90 degrees", new AutoArcTurn(this, 90 * Angle.DEGREES, Direction.LEFT));
		
		programChooser.addObject("Forklift Set Scale", new AutoSetForkliftState(this, ForkliftState.SCALE));
		programChooser.addObject("Forklift Set Switch", new AutoSetForkliftState(this, ForkliftState.SWITCH));
		programChooser.addObject("Forklift Set Floor", new AutoSetForkliftState(this, ForkliftState.GROUND));

		programChooser.addObject("Test Scale Dropoff", new AutoScaleDropoffTest(this));
		
		programChooser.addObject("Center Switch", new AutoSwitchFromCenter(drive, forklift));
	}

	@Override
	protected void teleopInit()
	{
		forklift.disabled = false;

		intakeState = IntakeState.STOPPED;

		leftDriveLeader.setSelectedSensorPosition(0, 0, Constants.CAN_TIMEOUT);
		rightDriveLeader.setSelectedSensorPosition(0, 0, Constants.CAN_TIMEOUT);

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
		
		//drive.autoshift();
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
		drive.shiftToLow();
	}

	@Override
	protected void updateDashboard()
	{
		SmartDashboard.putNumber("Forklift Velocity", forkliftMotorLeader.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Forklift Position", forkliftMotorLeader.getSelectedSensorPosition(0));

		SmartDashboard.putNumber("Current Forklift Error (in)", forklift.error / Length.in);
		SmartDashboard.putNumber("Current Forklift Position (in)", forklift.currentPosition / Length.in);

		SmartDashboard.putNumber("Right Speed (nu/100ms)", rightDriveLeader.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Left Speed (nu/100ms)", leftDriveLeader.getSelectedSensorVelocity(0));
	
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		
		SmartDashboard.putNumber("Left Encoder Position", leftDriveLeader.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Encoder Position", rightDriveLeader.getSelectedSensorPosition(0));
		
		SmartDashboard.putNumber("Left Motor Output", leftDriveLeader.getMotorOutputPercent());
		SmartDashboard.putNumber("Right Motor Output", rightDriveLeader.getMotorOutputPercent());

		
		SmartDashboard.putString("Forklift Control Mode", forklift.controlMode.getName());
		if (drive.isInHighGear())
		{
			SmartDashboard.putString("Gear", "HIGH GEAR");
		}
		else
		{
			SmartDashboard.putString("Gear", "LOW GEAR");
		}
		
		SmartDashboard.putNumber("Battery Voltage", PowerJNI.getVinVoltage());
	}
}