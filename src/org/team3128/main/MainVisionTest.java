
package org.team3128.main;

import org.team3128.common.NarwhalRobot;
import org.team3128.narwhalvision.NarwhalVisionReceiver;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class MainVisionTest extends NarwhalRobot
{

	NarwhalVisionReceiver reciever;
	
	@Override
	protected void constructHardware()
	{
		reciever = new NarwhalVisionReceiver();
	}

	@Override
	protected void setupListeners()
	{
		
	}

	protected void constructAutoPrograms(SendableChooser<CommandGroup> programChooser)
	{
	}

	@Override
	protected void teleopInit()
	{
		
	}

	@Override
	protected void teleopPeriodic()
	{
	}

	@Override
	protected void disabledInit()
	{
	}

	@Override
	protected void autonomousInit()
	{
		
	}

	@Override
	protected void updateDashboard()
	{
		
	}
}