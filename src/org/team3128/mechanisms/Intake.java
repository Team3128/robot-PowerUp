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
		STOPPED(0, true, "Stopped"),
		INTAKE(-1.0, false, "Intake"),
		OUTTAKE(1.0, true, "Outtake");

		private double rollerPower;
		private boolean isClosed;
		private String name;
		
		private IntakeState(double rollerPower, boolean isClosed, String name) {
			this.rollerPower = rollerPower;
			this.isClosed = isClosed;
			this.name= name;
		}

		public double getRollerPower() {
			return rollerPower;
		}
		public boolean getPistonPosition() {
			return isClosed;
		}
		
		public String getName() {
			return name;
		}
	}

	VictorSPX intakeMotors;
	//private DigitalInput limSwitch;
	private IntakeState state, newState;
	private Piston piston;
	private double invertMultiplier;

	//constructor
	public Intake(VictorSPX intakeMotors, IntakeState state, Piston piston, boolean inverted) {
		this.intakeMotors = intakeMotors;
		//this.limSwitch = limSwitch;
		this.state = state;
		this.piston = piston;		
		
		this.invertMultiplier = (inverted) ? -1 : 1;
	}
	
	public void setState(IntakeState newState) {
		if (state != newState) {
			this.newState = newState;
			
			if(newState.getPistonPosition()) {
				piston.setPistonOn();
			}
			else {
				piston.setPistonOff();
			}
			
			Thread intakeThread = new Thread(() -> {
				if (this.state.equals(IntakeState.INTAKE) && this.newState.equals(IntakeState.STOPPED)) {
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
				setIntakePower(this.newState.getRollerPower());
				this.state = this.newState;
			});
			intakeThread.start();
			
			
		}
	}
	
	public synchronized void setIntakePower(double power) {
		intakeMotors.set(ControlMode.PercentOutput, invertMultiplier * power);
	}
}