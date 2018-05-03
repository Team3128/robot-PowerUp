/*
 * Description: Setup teleop/auto for competition
 *
 */

package org.team3128.main;

import org.team3128.common.hardware.misc.Piston;
import org.team3128.common.listener.controltypes.Button;
import org.team3128.common.util.Log;
import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.util.PlateAllocation;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MainGuidoCompetition extends MainGuido
{
	public boolean canDeployBuddyBar;
	
	public MainGuidoCompetition()
	{
		super();
	}

	@Override
	protected void constructHardware()
	{
		auto_delay = 0;
		
		canDeployBuddyBar = false;
		
		limitSiwtchLocation = 0;

		wheelCirc = 12.6 * Length.in;
		lowGearMaxSpeed = 3800;

		gearshiftPiston = new Piston(3, 4);
		intakePiston = new Piston(1, 6);

		climberPiston = new Piston(2, 5);
		climberLockPiston = new Piston(0, 7);

		climberLockPiston.setPistonOff();
		climberPiston.setPistonOn();
		// intakePiston.invertPiston();

		intakeInverted = true;
		forkliftSoftStopLimitSwitch = new DigitalInput(6);

		super.constructHardware();

		forklift.maxHeight = 19000;

		CameraServer cameraServer = CameraServer.getInstance();
		UsbCamera camera = cameraServer.startAutomaticCapture(0);
		camera.setFPS(20);
		camera.setResolution(240, 135);
	}

	@Override
	protected void setupListeners()
	{

		super.setupListeners();

		listenerLeft.nameControl(new Button(8), "BrakeClimber");
		listenerLeft.addButtonDownListener("BrakeClimber", () ->
		{
			canDeployBuddyBar = true;
			climberLockPiston.invertPiston();
		});
		listenerRight.nameControl(new Button(7), "DeployBar");
		listenerRight.addButtonDownListener("DeployBar", () ->
		{
			if (canDeployBuddyBar)
			{
				climberPiston.invertPiston();
			}
			else {
				Log.info("MainGuidoCompetition", "Climber not locked!");
			}
		});
	}

	protected void constructAutoPrograms(SendableChooser<CommandGroup> programChooser)
	{
		super.constructAutoPrograms(programChooser);
	}

	@Override
	protected void teleopInit()
	{
		invertSetup();
		canDeployBuddyBar = false;

		super.teleopInit();
	}

	@Override
	protected void autonomousInit()
	{
		invertSetup();

		super.autonomousInit();
	}

	public void invertSetup()
	{
		leftDriveLeader.setInverted(false);
		leftDriveFollower.setInverted(false);

		rightDriveLeader.setInverted(false);
		rightDriveFollower.setInverted(false);

		rightDriveFollower.setInverted(true);
		rightDriveLeader.setInverted(true);

		rightDriveLeader.setSensorPhase(true);
		leftDriveLeader.setSensorPhase(true);
	}

	@Override
	protected void updateDashboard()
	{
		super.updateDashboard();
		SmartDashboard.putString("Can Lower Buddy Bar", canDeployBuddyBar + "");
		
		SmartDashboard.putNumber("Match Timer", ds.getMatchTime());
		SmartDashboard.putBoolean("Alliance Color", ds.getAlliance() == Alliance.Blue);
		
		boolean alliance_color = ds.getAlliance() == Alliance.Blue;
		
		// TRUE TO BLUE\
		// FALSE TO RALSE (RED)
		
		boolean near_switch_right = PlateAllocation.getNearSwitch() == Direction.RIGHT;
		boolean near_switch_right_color = true;
		
		if (near_switch_right && alliance_color || !near_switch_right && !alliance_color) {
			near_switch_right_color = true;
		}
		else if (near_switch_right && !alliance_color || !near_switch_right && alliance_color) {
			near_switch_right_color = false;
		}
		
		SmartDashboard.putBoolean("Near Switch Right", near_switch_right_color);
		SmartDashboard.putBoolean("Near Switch Left", !near_switch_right_color);
		
		
		boolean scale_right = PlateAllocation.getScale() == Direction.RIGHT;
		boolean scale_right_color = true;
		
		if (scale_right && alliance_color || !scale_right && !alliance_color) {
			scale_right_color = true;
		}
		else if (scale_right && !alliance_color || !scale_right && alliance_color) {
			scale_right_color = false;
		}
		
		SmartDashboard.putBoolean("Scale Right", scale_right_color);
		SmartDashboard.putBoolean("Scale Left", !scale_right_color);
				
		boolean far_switch_right = PlateAllocation.getFarSwitch() == Direction.RIGHT;
		boolean far_switch_right_color = true;
		
		if (far_switch_right && alliance_color || !far_switch_right && !alliance_color) {
			far_switch_right_color = true;
		}
		else if (far_switch_right && !alliance_color || !far_switch_right && alliance_color) {
			far_switch_right_color = false;
		}
		
		SmartDashboard.putBoolean("Far Switch Right", far_switch_right_color);
		SmartDashboard.putBoolean("Far Switch Left", !far_switch_right_color);
	}
}