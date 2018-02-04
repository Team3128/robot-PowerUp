package org.team3128.mechanisms;

import org.team3128.common.listener.POVValue;
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

public class Forklift {
	/**
	 * The ratio of the number of native units that results in
	 * a forkift movement of 1 centimeter.
	 * 
	 * MAX ENCODER POSITION = 20,000
	 * MAX HEIGHT = 12ft
	 */
	public final double ratio = 1.14 * 4096.0 / (2 * 2.635 * Length.in * Math.PI);
	final double tolerance = 5 * Length.in;
	
	public double error, currentPosition;

	private IntakeState intakeState;

	public enum ForkliftState {
		GROUND(0 * Length.ft),
		SWITCH(3 * Length.ft),
		SCALE(5.75 * Length.ft);

		public double targetHeight;

		private ForkliftState(double height) {
			this.targetHeight = height;
		}
	}

	Intake intake;
	TalonSRX forkliftMotor;
	DigitalInput softStopLimitSwitch;
	public ForkliftState state;
	Thread depositCubeThread, depositingRollerThread;

	public Forklift(ForkliftState state, Intake intake, TalonSRX forkliftMotor, DigitalInput softStopLimitSwitch) {
		this.intake = intake;
		this.forkliftMotor = forkliftMotor;
		this.softStopLimitSwitch = softStopLimitSwitch;
		this.state = state;
		
		forkliftMotor.configMotionCruiseVelocity(1000, Constants.CAN_TIMEOUT);
		forkliftMotor.configMotionAcceleration(4000, Constants.CAN_TIMEOUT);
		
		forkliftMotor.selectProfileSlot(1, 0);

		depositingRollerThread = new Thread(() ->
		{
			while(true)
			{
//				if (softStopLimitSwitch.get()) {
//					forkliftMotor.setSelectedSensorPosition(0, 0, Constants.CAN_TIMEOUT);
//				}
				
				currentPosition = forkliftMotor.getSelectedSensorPosition(0) / ratio;
				double targetHeight = this.state.targetHeight;
				
				error = Math.abs(currentPosition - targetHeight);
				
				// If the desired IntakeState is not stopped (i.e. intaking from ground or outaking at ground, switch,
				// or scale), run the intake only if the forklift is actually at the desired height
				if (this.intakeState != IntakeState.STOPPED) {
					if (error <= tolerance) {
						this.intake.setState(intakeState);
					}
					else {
						this.intake.setState(IntakeState.STOPPED);
					}
				}
				
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

	public void setState(ForkliftState forkliftState, IntakeState intState) {
		if (state != forkliftState) {
			if (forkliftState.targetHeight < state.targetHeight) {
				forkliftMotor.selectProfileSlot(1, 0);
			}
			else {
				forkliftMotor.selectProfileSlot(0, 0);
			}
			state = forkliftState;
			Log.info("Forklift and Intake", "Going to" + state.targetHeight );
			forkliftMotor.set(ControlMode.MotionMagic, state.targetHeight * ratio);
		}		
		
		intakeState = intState;
	}
	

	public void onPOVUpdate(POVValue newValue) {
		switch (newValue.getDirectionValue()) {
		case 7:
		case 0:
		case 1:
			setState(ForkliftState.SCALE, IntakeState.OUTTAKE);
			break;
		case 4:
		case 5:
		case 6:
			setState(ForkliftState.GROUND, IntakeState.INTAKE);
			break;
		default:
			setState(ForkliftState.GROUND, IntakeState.STOPPED);
			break;
		}
	}
	
	public void switchOuttake() {
		setState(ForkliftState.SWITCH, IntakeState.OUTTAKE);
	}
	
	public void rest() {
		setState(ForkliftState.GROUND, IntakeState.STOPPED);
	}

	public class CmdForkliftPush extends Command {
		ForkliftState heightState;
		IntakeState intState;
		
		public CmdForkliftPush(ForkliftState heightState, IntakeState intState) {
			Log.debug("Command Forklift", "Setting height to " + heightState.targetHeight);
			
			this.heightState = heightState;
			this.intState = intState;
		}

		@Override
		protected void initialize() {
			// setState(tempHeightState, tempIntState);
			setState(heightState, intState);
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
		protected void interrupted() {
			Log.debug("Forklift and Intake", "Ending, was interrupted.");
			end();
		}

		@Override
		protected boolean isFinished() {
			Log.debug("Forklift and Intake", "Task completed.");
			return false;
			// return isTimedOut();
		}
	}
}
