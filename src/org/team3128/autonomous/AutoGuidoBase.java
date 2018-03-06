package org.team3128.autonomous;

import org.team3128.autonomous.util.CmdUpdatePlateAlloc;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.util.Log;
import org.team3128.mechanisms.Forklift;
import org.team3128.mechanisms.Forklift.ForkliftState;
import org.team3128.util.PlateAllocation;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGuidoBase extends CommandGroup {
	protected AutoGuidoBase(SRXTankDrive drive, Forklift forklift) {
		addSequential(forklift.new CmdSetForkliftPosition(ForkliftState.GROUND));
		addSequential(new CmdUpdatePlateAlloc(1000));
		
		Log.info("AutoGuidoBase", "Plate Allocation: " + PlateAllocation.getNearSwitch());

	}
}
