package org.team3128.mechanisms;

import org.team3128.common.hardware.motor.MotorGroup;
import org.team3128.common.listener.POVValue;
import org.team3128.common.util.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Control system for the intake mechanism V.1
 * 
 * @author Eli, Adham
 * 
 */

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

	VictorSPX leader;
	VictorSPX follower;
	private DigitalInput limSwitch;
	private State state;

	public Intake(VictorSPX leader, VictorSPX follower, DigitalInput limSwitch, State state) {
		this.leader = leader;
		this.follower = follower;
		this.limSwitch = limSwitch;
		this.state = state;
		follower.set(ControlMode.Follower, leader.getDeviceID());
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
		leader.set(ControlMode.Velocity, newState.getRollerPower());
		state = newState;
	}

	public State getState() {
		return state;
	}

	public void setIntake() {
		if (state.getRollerPower() < 0 && limSwitch.get() == true) {
			leader.set(ControlMode.Velocity, 0);
		}
	}

}
