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
	 */
	final double ratio = 6;
	final double tolerance = 5 * Length.in;

	private IntakeState intakeState;

	public enum ForkliftState {
		GROUND(0 * Length.ft),
		SWITCH(3 * Length.ft),
		SCALE(8 * Length.ft);

		private double targetHeight;

		private ForkliftState(double height) {
			this.targetHeight = height;
		}
		
		public double getHeight() {
			return targetHeight;
		}
	}

	Intake intake;
	TalonSRX forkliftMotor;
	DigitalInput softStopLimitSwitch;
	ForkliftState state;
	Thread depositCubeThread;

	public Forklift(ForkliftState state, Intake intake, TalonSRX forkliftMotor, DigitalInput softStopLimitSwitch) {
		this.intake = intake;
		this.forkliftMotor = forkliftMotor;
		this.softStopLimitSwitch = softStopLimitSwitch;
		this.state = state;

		depositCubeThread = new Thread(() -> {
			while (true) {
				if (softStopLimitSwitch.get()) {
					forkliftMotor.setSelectedSensorPosition(0, 0, Constants.CAN_TIMEOUT);
				}
				
				double position = forkliftMotor.getSelectedSensorPosition(0) / ratio;
				double targetHeight = state.getHeight();
				
				double error = Math.abs(position - targetHeight);
				
				// If the desired IntakeState is not stopped (i.e. intaking from ground or outaking at ground, switch,
				// or scale), run the intake only if the forklift is actually at the desired height
				if (intakeState != IntakeState.STOPPED) {
					if (error <= tolerance) {
						intake.setState(intakeState);
					}
					else {
						intake.setState(IntakeState.STOPPED);
					}
				}
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		// depositCubeThread.start();
	}

	public void setState(ForkliftState forkliftState, IntakeState intState) {
		if (state != forkliftState) {
			state = forkliftState;
			
			forkliftMotor.set(ControlMode.Position, state.getHeight() * ratio);
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

		public CmdForkliftPush(ForkliftState heightState, IntakeState intState) {
			setState(heightState, intState);
		}

		@Override
		protected void initialize() {
			// setState(tempHeightState, tempIntState);
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
