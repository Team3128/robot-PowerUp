/*
 * Date: 1/12/2018
 * Description: Setup teleop/auto modes for testing purposes
 *
 */

package org.team3128.main;

import org.team3128.common.util.datatypes.PIDConstants;
import org.team3128.common.util.units.Length;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class MainGuidoPractice extends MainGuido {

	public MainGuidoPractice() {
		super();
	}

	@Override
	protected void constructHardware() {
		limitSiwtchLocation = 0;
		
		wheelCirc = 12.68 * Length.in;
		
		positionUpwardsPID = new PIDConstants(0.8, 0, 0.9, 1.2);
		positionDownwardsPID = new PIDConstants(0.8, 0, 0.9, 1.2);
		velocityPID = new PIDConstants(0, 0, 0, 0);
		
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
	}

	@Override
	protected void autonomousInit() {
		super.autonomousInit();
	}
}