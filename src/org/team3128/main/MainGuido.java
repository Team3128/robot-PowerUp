/*
 *     
 * Date: 1/12/2018
 * Description: Setup teleop and autonomous modes for 2018 MainGuido for testing purposes
 *
 * 
 */

package org.team3128.main;

import org.team3128.common.NarwhalRobot;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.hardware.misc.Piston;
import org.team3128.common.hardware.misc.TwoSpeedGearshift;
import org.team3128.common.hardware.motor.NarwhalSRX;
import org.team3128.common.hardware.motor.NarwhalSRX.Reverse;
import org.team3128.common.listener.ListenerManager;
import org.team3128.common.listener.controllers.ControllerExtreme3D;
import org.team3128.common.listener.controltypes.Button;
import org.team3128.common.util.Constants;
import org.team3128.common.util.Log;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Intake;
import org.team3128.mechanisms.Intake.intakeState;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class MainGuido extends NarwhalRobot {
	
	// Drive Train
	public double wheelDiameter;
	public SRXTankDrive drive;
	public NarwhalSRX leftDriveLeader, leftDriveFollower;
	public NarwhalSRX rightDrive1, rightDrive2;
	
	public TwoSpeedGearshift gearshift;
	public Piston gearshiftPiston;
	
	// Forklift
	Forklift forklift;
	public TalonSRX forkliftSRX1, forkliftSRX2;
	DigitalInput forkLimSwitch;
		
	// Intake
	Intake intake;
	intakeState intakeState;
	public VictorSPX intakeSPX1, intakeSPX2;
	DigitalInput intakeLimSwitch;
	
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
		leftDriveLeader = new NarwhalSRX(20, Reverse.NONE, Reverse.NONE);
		leftDriveFollower = new NarwhalSRX(21, Reverse.NONE, Reverse.NONE);
		rightDrive1 = new NarwhalSRX(10, Reverse.NONE, Reverse.NONE);
		rightDrive2 = new NarwhalSRX(11, Reverse.NONE, Reverse.NONE);
		
		//rightDrive1.setInverted(false);
		//rightDrive2.setInverted(false);
		//leftDrive1.setInverted(true);
		//leftDrive2.setInverted(true);

		// set Leaders
		leftDriveLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);
		rightDrive1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);

		// set Followers
		leftDriveFollower.set(ControlMode.Follower, leftDriveLeader.getDeviceID());
		rightDrive2.set(ControlMode.Follower, rightDrive1.getDeviceID());

		// create SRXTankDrive
		drive = new SRXTankDrive(leftDriveLeader, rightDrive1, wheelDiameter * Math.PI, 1, 25.25 * Length.in, 30.5 * Length.in, 400);
		
		gearshiftPiston = new Piston(0, 1);
		gearshift = new TwoSpeedGearshift(false, gearshiftPiston);
		
		drive.addShifter(gearshift, shiftUpSpeed, shiftDownSpeed);
		
		/*
		// create intake
		intakeState = Intake.intakeState.STOPPED;
		intakeSPX1 = new VictorSPX(1);
		intakeSPX2 = new VictorSPX(2);
		intakeLimSwitch = new DigitalInput(2); //random channel
		intake = new Intake(intakeSPX1, intakeSPX2, intakeLimSwitch, intakeState);
		
		// create forklift
		forkliftSRX1 = new TalonSRX(30);
		forkliftSRX2 = new TalonSRX(31);
		forkLimSwitch = new DigitalInput(1); //random chanel
		forklift = new Forklift(Forklift.State.GROUND, intake, forkliftSRX1, forkliftSRX2, forkLimSwitch);
		*/
		
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
		//name controls
		listenerRight.nameControl(ControllerExtreme3D.JOYY, "moveForwards");
		listenerRight.nameControl(ControllerExtreme3D.TWIST, "moveTurn");
		listenerRight.nameControl(ControllerExtreme3D.THROTTLE, "Throttle");
		
		//debug
		Log.info("MainGuido", "Controllers Named");

		//get Joy-stick data
		listenerRight.addMultiListener(() -> {
			Log.info("MainGuido", "Multi Listener Entered");
			double x = listenerRight.getAxis("moveForwards");
			double y = listenerRight.getAxis("moveTurn");
			double t = listenerRight.getAxis("Throttle") * -1;
			
			drive.arcadeDrive(x, y, t, true);
		}, "moveForwards", "moveTurn", "Throttle");


		listenerLeft.nameControl(new Button(11), "ClearStickyFaults");
		listenerLeft.addButtonDownListener("ClearStickyFaults", () -> {
			
		});
	}

	@Override
	protected void teleopInit()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void autonomousInit()
	{
		// TODO Auto-generated method stub
		
	}
}