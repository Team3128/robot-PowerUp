package org.team3128.mechanisms;

import org.team3128.common.hardware.misc.Piston;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

/**
 * Control system for the intake mechanism V.1
 * 
 * @author Eli, Adham
 * 
 */

public class Intake {
	public enum IntakeState {
		STOPPED(0, true),
		INTAKE(-1.0, false),
		OUTTAKE(1.0, true);

		private double rollerPower;
		private boolean isClosed;
		
		private IntakeState(double rollerPower, boolean isClosed) {
			this.rollerPower = rollerPower;
			this.isClosed = isClosed;
		}

		public double getRollerPower() {
			return rollerPower;
		}
		public boolean getPistonPosition() {
			return isClosed;
		}
	}

	VictorSPX intakeMotors;
	//private DigitalInput limSwitch;
	private IntakeState state;
	private Piston piston;

	//constructor
	public Intake(VictorSPX intakeMotors, IntakeState state, Piston piston) {
		this.intakeMotors = intakeMotors;
		//this.limSwitch = limSwitch;
		this.state = state;
		this.piston = piston;		
	}
	
	public void setState(IntakeState newState) {
		if (state != newState) {
			state = newState;
			
			intakeMotors.set(ControlMode.PercentOutput, state.getRollerPower());
		
			if(newState.getPistonPosition()) {
				piston.setPistonOn();
			}
			else {
				piston.setPistonOff();
			}
		}
	}

}