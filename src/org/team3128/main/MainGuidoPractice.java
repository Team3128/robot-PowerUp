/*
 * Date: 1/12/2018
 * Description: Setup teleop/auto modes for testing purposes
 *
 */

package org.team3128.main;

import org.team3128.common.hardware.misc.Piston;
import org.team3128.common.util.units.Length;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class MainGuidoPractice extends MainGuido {

	public MainGuidoPractice() {
		super();
	}

	@Override
	protected void constructHardware() {		
		limitSiwtchLocation = 0;
		
		wheelCirc = 12.20 * Length.in;
		lowGearMaxSpeed = 3600;
		
		intakeInverted = false;
		forkliftSoftStopLimitSwitch = new DigitalInput(7);
				
		gearshiftPiston = new Piston(2, 5);
		intakePiston = new Piston(1, 6);
				
		super.constructHardware();
		
		forklift.maxHeight = 17000;
		
		CameraServer cameraServer = CameraServer.getInstance();
		UsbCamera camera = cameraServer.startAutomaticCapture(0);
		camera.setFPS(15);
		camera.setResolution(240, 135);
	}

	@Override
	protected void setupListeners() {
		super.setupListeners();
	}

	protected void constructAutoPrograms(SendableChooser<CommandGroup> programChooser) {
		super.constructAutoPrograms(programChooser);
	}

	@Override
	protected void teleopInit() {
		super.teleopInit();
		
		leftDriveLeader.setSensorPhase(true);

		rightDriveLeader.setInverted(true);
		rightDriveLeader.setSensorPhase(true);
	}

	@Override
	protected void autonomousInit() {
		super.autonomousInit();
		
		leftDriveLeader.setInverted(false);
		rightDriveLeader.setInverted(false);

		leftDriveLeader.setSensorPhase(true);

		rightDriveLeader.setInverted(true);
		rightDriveLeader.setSensorPhase(true);
	}
	@Override
	protected void updateDashboard()
	{
		super.updateDashboard();
		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTable table = inst.getTable("datatable");
		//table.getEntry("cameraURL").setString("10.31.28.2:1181/?action=stream");
		table.getEntry("forkliftPosition").setDouble(forklift.currentPosition / Length.in);
		if (drive.isInHighGear()) {
			
			table.getEntry("gearValue").setString("HIGH GEAR");
			
		} else {
		
			table.getEntry("gearValue").setString("LOW GEAR");
		
		}
	}
}