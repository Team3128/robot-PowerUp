package org.team3128.mechanisms;

import org.team3128.common.hardware.motor.MotorGroup;
import org.team3128.common.listener.POVValue;

import edu.wpi.first.wpilibj.DigitalInput;

public class Intake {
	public enum State {
		STOPPED(0), INTAKE(1), OUTTAKE(-1);

		private double rollerPower;

		private State(double rollerPower) {
			this.setRollerPower(rollerPower);
		}

		public double getRollerPower() {
			return rollerPower;
		}

		public void setRollerPower(double rollerPower) {
			this.rollerPower = rollerPower;
		}

	}

	private MotorGroup rollers;
	private DigitalInput limSwitch;
	private State state;

	public Intake(MotorGroup rollers, DigitalInput limSwitch, State state) {
		this.rollers = rollers;
		this.limSwitch = limSwitch;
		this.state = state;
	}

	public void onPOVUpdate(POVValue newValue) {
		switch (newValue.getDirectionValue()) {
		case 0:
			setState(State.STOPPED);
			break;
		case 1:
		case 2:
		case 8:
			setState(State.OUTTAKE);
			break;
		case 4:
		case 5:
		case 6:
			setState(State.INTAKE);
			break;
		}
	}

	public void setState(State newState) {
		rollers.setTarget(newState.getRollerPower());
		state = newState;
	}

	public State getState() {
		return state;
	}

	public void setIntake() {
		if (state.getRollerPower() < 0 && limSwitch.get() == true) {
			rollers.setTarget(0);
		}
	}

}
