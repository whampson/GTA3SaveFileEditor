package thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Block;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Align;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.DataStructure;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.GTADataType;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAStringUTF8;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;

/**
 * The first of 20 main blocks in a GTA III save file. This block, known
 * internally as the "SimpleVars" block, contains miscellaneous information
 * about the save and general game variables - the save title, timestamp,
 * weather info, time, etc. - as well as data pertaining to the current state of
 * the game's main script. Despite the word "simple" being in its name, this
 * block is the most complex block in the file.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 12, 2015
 * @deprecated 
 */
public class Block00 extends Block
{
    public Block00(SaveFile.Platform platform, VariableDefinitions vars)
    {
        super(Identifier.BLOCK00, vars, new DataStructure[] {
            vars.sz16Title.asDataStructure(platform),
            vars.aTimestamp.asDataStructure(platform),
            new Align(platform, -1, -1, 0x0004, -1, -1),
            vars.iCurrentIsland.asDataStructure(platform),
            vars.fCameraPosition.asDataStructure(platform),
            vars.iMillisPerGameMinute.asDataStructure(platform),
            vars.iWeatherTimer.asDataStructure(platform),
            vars.bGameTimeHours.asDataStructure(platform),
            new Align(platform, -1, -1, 0x0003, -1, -1),
            vars.bGameTimeMinutes.asDataStructure(platform),
            new Align(platform, -1, -1, 0x0003, -1, -1),
            vars.sCurrentPadMode.asDataStructure(platform),
            new Align(platform, -1, -1, 0x0002, -1, -1),
            vars.iGlobalTimer.asDataStructure(platform),
            vars.fTimeScale.asDataStructure(platform),
            vars.fTimerTimeStep.asDataStructure(platform),
            vars.fTimerTimeStepNonClipped.asDataStructure(platform),
            vars.iFrameCounter.asDataStructure(platform),
            vars.fTimeStep.asDataStructure(platform),
            vars.fFramesPerUpdate.asDataStructure(platform),
            vars.fTimeScale2.asDataStructure(platform),
            vars.sWeatherTypeOld.asDataStructure(platform),
            new Align(platform, -1, -1, 0x0002, -1, -1),
            vars.sWeatherTypeNew.asDataStructure(platform),
            new Align(platform, -1, -1, 0x0002, -1, -1),
            vars.sWeatherTypeForced.asDataStructure(platform),
            new Align(platform, -1, -1, 0x0002, -1, -1),
            vars.fWeatherInterpolationValue.asDataStructure(platform),
            vars.aCompilationTime.asDataStructure(platform),
            vars.iWeatherTypeInList.asDataStructure(platform),
            new Align(platform, -1, -1, 0x0008, -1, -1),
            new SubBlockSCR(platform, vars)
        });
    }

    private static class SubBlockSCR extends Block
    {
        public SubBlockSCR(SaveFile.Platform platform, VariableDefinitions vars)
        {
            super(Identifier.SCR, vars, new DataStructure[] {
                new GTAStringUTF8("SCR", 4),
                new SubBlockScripts(platform, vars)
            });
        }
    }
    
    private static class SubBlockScripts extends Block
    {
        public SubBlockScripts(SaveFile.Platform platform, VariableDefinitions vars)
        {
            super(Identifier.SCRIPT_DATA, vars, new DataStructure[] {
                vars.iGlobalVariablesSpaceSize.asDataStructure(platform),
                vars.aGlobalVariables.asDataStructure(platform, vars.iGlobalVariablesSpaceSize, GTADataType.SIZEOF_INT),
                new SubBlockSCM(platform, vars),
                vars.iRunningScriptsCount.asDataStructure(platform),
                vars.aRunningScripts.asDataStructure(platform, vars.iRunningScriptsCount)
            });
        }
    }
    
    private static class SubBlockSCM extends Block
    {
        public SubBlockSCM(SaveFile.Platform platform, VariableDefinitions vars)
        {
            super(Identifier.SCM, vars, new DataStructure[] {
                vars.iOnAMissionFlag.asDataStructure(platform),
                vars.aContactInfo.asDataStructure(platform),
                new Align(platform, -1, -1, 0x0100, -1, -1),
                vars.iLastMissionPassedTime.asDataStructure(platform),
                vars.aBuildingSwaps.asDataStructure(platform),
                vars.aInvisibliltySettings.asDataStructure(platform),
                vars.b8AlreadyRunningAMissionScript.asDataStructure(platform),
                new Align(platform, -1, -1, 0x0003, -1, -1),
                vars.iMainScriptSize.asDataStructure(platform),
                vars.iLargestMissionScriptSize.asDataStructure(platform),
                vars.sExclusiveMissionScriptsCount.asDataStructure(platform),
                new Align(platform, -1, -1, 0x0002, -1, -1)
            });
        }
    }
}