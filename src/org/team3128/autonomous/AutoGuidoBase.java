package org.team3128.autonomous;

import org.team3128.common.autonomous.primitives.CmdDelay;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.Log;
import org.team3128.mechanisms.Forklift;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGuidoBase extends CommandGroup {
	protected AutoGuidoBase(SRXTankDrive drive, Forklift forklift, double delay) {
		addSequential(forklift.new CmdZeroForklift());	
		addSequential(new CmdDelay(delay));
		
		Log.info("AutGuidoBase", "Delay: " + delay);
	}
}
