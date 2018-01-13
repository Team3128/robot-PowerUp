package org.team3128.mechanisms;

import org.team3128.common.util.Constants;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Intake.State;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Control system for the forklift mechanism V.1
 * 
 * @author Eli, Adham
 * 
 */

public class Forklift {
	final double constant = 6;
	final double stallHeight = 8 * Length.ft;
	private static double groundHeight = 0 * Length.ft;
	private static double switchHeight = 3 * Length.ft;
	private static double scaleHeight = 8 * Length.ft;

	public enum State {

		GROUND(groundHeight), SWITCH(switchHeight), SCALE(scaleHeight);

		private double targetHeight;

		private State(double height) {
			this.targetHeight = height;
		}
	}

	Intake intake;
	TalonSRX leader;
	TalonSRX follower;
	DigitalInput limSwitch;
	State state;
	Thread depositCubeThread;

	public Forklift(State state, Intake intake, TalonSRX leader, TalonSRX follower, DigitalInput limSwitch) {
		this.intake = intake;
		this.leader = leader;
		this.follower = follower;
		this.limSwitch = limSwitch;
		this.state = state;

		// set leader feedback device
		leader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);

		// set Follower
		follower.set(ControlMode.Follower, leader.getDeviceID());

	}

	public void setState(State heightState, Intake.State intakeState) {
		state = heightState;
		depositCubeThread.start();
		depositCubeThread = new Thread(() -> {
			while (true) {
				if (!limSwitch.get() && leader.getSelectedSensorPosition(0) <= stallHeight + 3 * Length.in) {
					leader.set(ControlMode.Position, state.targetHeight);

					if (heightState.targetHeight >= leader.getSelectedSensorPosition(0) * constant - 3 * Length.in
							&& limSwitch.get() == false) {
						intake.setState(intakeState);
						break;
					}
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
	}
}
