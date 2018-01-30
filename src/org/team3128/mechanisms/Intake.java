package org.team3128.mechanisms;

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
		STOPPED(0),
		INTAKE(1),
		OUTTAKE(-1);

		private double rollerPower;
		
		private IntakeState(double rollerPower) {
			this.rollerPower = rollerPower;
		}

		public double getRollerPower() {
			return rollerPower;
		}
	}

	VictorSPX intakeMotors;
	//private DigitalInput limSwitch;
	private IntakeState state;

	//constructor
	public Intake(VictorSPX intakeMotors, IntakeState state) {
		this.intakeMotors = intakeMotors;
		//this.limSwitch = limSwitch;
		this.state = state;
	}
	
	public void setState(IntakeState newState) {
		if (state != newState) {
			state = newState;
			
			intakeMotors.set(ControlMode.PercentOutput, state.getRollerPower());
		}
	}

}
