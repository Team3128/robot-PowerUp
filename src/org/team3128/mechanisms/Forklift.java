package org.team3128.mechanisms;

import org.team3128.common.util.Constants;
import org.team3128.common.util.Log;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Intake.IntakeState;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Control system for the fork-lift mechanism V.1
 * 
 * @author Eli, Adham
 * 
 */

public class Forklift
{
	/**
	 * The ratio of the number of native units that results in a forkift
	 * movement of 1 centimeter.
	 * 
	 * MAX ENCODER POSITION = 20,000 MAX HEIGHT = 12ft
	 */
	public final double ratio = 1.14 * 4096.0 / (2 * 2.635 * Length.in * Math.PI);
	final double tolerance = 5 * Length.in;

	public double error, currentPosition;

	private IntakeState intakeState;

	public enum ForkliftState
	{
		GROUND(0 * Length.ft),
		SWITCH(3 * Length.ft),
		SCALE(5.75 * Length.ft);

		public double targetHeight;

		private ForkliftState(double height)
		{
			this.targetHeight = height;
		}
	}

	public enum ForkliftControlMode
	{
		PERCENT(2, "Percent Output"),
		POSITION_UP(0, "Position (Up)"),
		POSITION_DOWN(1, "Position (Down)");

		private int pidSlot;
		private String name;

		private ForkliftControlMode(int pidSlot, String name)
		{
			this.pidSlot = pidSlot;
			this.name = name;
		}

		public int getPIDSlot()
		{
			return pidSlot;
		}

		public String getName()
		{
			return name;
		}

	}

	public void setControlMode(ForkliftControlMode mode)
	{
		if (mode != controlMode)
		{
			controlMode = mode;

			forkliftMotor.selectProfileSlot(mode.pidSlot, 0);
		}
	}

	Intake intake;
	TalonSRX forkliftMotor;
	DigitalInput softStopLimitSwitch;
	Thread depositCubeThread, depositingRollerThread;

	public ForkliftControlMode controlMode;
	public ForkliftState state;

	int limitSwitchLocation, forkliftMaxVelocity;

	int currentPIDSlot = 0;

	public double restPosition = 0;

	public double brakePower = 0.15;
	public double brakeHeight = 0.5 * Length.ft;

	public double maxHeight;

	public boolean disabled = false;

	public boolean canLower = false;
	public boolean canRaise = true;
	
	private double desiredTarget = 0;
	private double setPoint = 0;
	
	public Forklift(ForkliftState state, Intake intake, TalonSRX forkliftMotor, DigitalInput softStopLimitSwitch,
			int limitSwitchLocation, int forkliftMaxVelocity)
	{
		this.intake = intake;
		this.forkliftMotor = forkliftMotor;
		this.softStopLimitSwitch = softStopLimitSwitch;
		this.limitSwitchLocation = limitSwitchLocation;
		this.state = state;

		controlMode = ForkliftControlMode.PERCENT;

		forkliftMotor.configMotionCruiseVelocity(1000, Constants.CAN_TIMEOUT);
		forkliftMotor.configMotionAcceleration(4000, Constants.CAN_TIMEOUT);

		forkliftMotor.selectProfileSlot(1, 0);
		
		forkliftMotor.configOpenloopRamp(0.2, Constants.CAN_TIMEOUT);

		depositingRollerThread = new Thread(() ->
		{
			while (true)
			{
				if (this.getForkliftSwitch())
				{
					this.forkliftMotor.setSelectedSensorPosition(limitSwitchLocation, 0, Constants.CAN_TIMEOUT);
				}

				if (this.disabled)
				{
					this.forkliftMotor.set(ControlMode.PercentOutput, 0);
				}
				else {
					double target = 0;
					
					this.canRaise = this.forkliftMotor.getSelectedSensorPosition(0) < this.maxHeight;
					this.canLower = this.forkliftMotor.getSelectedSensorPosition(0) > 100;
					
					if (this.controlMode == ForkliftControlMode.PERCENT) {
						if (this.desiredTarget > 0 && this.canRaise) {
							target = this.desiredTarget;
						}
						else if (this.desiredTarget < 0 && this.canLower) {
							target = 0.7 * this.desiredTarget;
						}
						
						if ((Math.abs(target) < 0.1
								&& this.forkliftMotor.getSelectedSensorPosition(0) / ratio >= this.brakeHeight)) {
							target = this.brakePower;
						}
					}
						
					if (Math.abs(target - this.setPoint) > 0.0001) {
						this.forkliftMotor.set(ControlMode.PercentOutput, target);
						
						this.setPoint = target;
					}
				}

				
				this.currentPosition = forkliftMotor.getSelectedSensorPosition(0) / ratio;
				double targetHeight = this.state.targetHeight;

				this.error = Math.abs(currentPosition - targetHeight);

				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

			}
		});

		depositingRollerThread.start();
	}

	public void setState(ForkliftState forkliftState)
	{
		if (state != forkliftState)
		{
			if (forkliftState.targetHeight < state.targetHeight)
			{
				setControlMode(ForkliftControlMode.POSITION_DOWN);
			}
			else
			{
				setControlMode(ForkliftControlMode.POSITION_UP);
			}
			state = forkliftState;
			Log.info("Forklift and Intake", "Going to " + state.targetHeight + " inches.");
			forkliftMotor.set(ControlMode.MotionMagic, state.targetHeight * ratio);
		}
	}

	public void powerControl(double joystick)
	{
		setControlMode(ForkliftControlMode.PERCENT);
		
		desiredTarget = joystick;
	}

	public boolean getForkliftSwitch()
	{
		return !softStopLimitSwitch.get();
	}

	public class CmdForkliftPush extends Command
	{
		ForkliftState heightState;
		IntakeState intState;

		public CmdForkliftPush(ForkliftState heightState, IntakeState intState)
		{
			Log.debug("Command Forklift", "Setting height to " + heightState.targetHeight);

			this.heightState = heightState;
			this.intState = intState;
		}

		@Override
		protected void initialize()
		{
			// setState(tempHeightState, tempIntState);
			setState(heightState);
			Log.debug("Forklift and Intake", "Changing state to ... ");
		}

		/*
		 * @Override protected void execute() {
		 * 
		 * }
		 * 
		 * @Override protected void end() {
		 * 
		 * }
		 */

		@Override
		protected void interrupted()
		{
			Log.debug("Forklift and Intake", "Ending, was interrupted.");
			end();
		}

		@Override
		protected boolean isFinished()
		{
			Log.debug("Forklift and Intake", "Task completed.");
			return false;
			// return isTimedOut();
		}
	}
}
