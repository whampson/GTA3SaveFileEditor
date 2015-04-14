package thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Align;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Block;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.DataStructure;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;

/**
 * The 16th of 20 main blocks in a GTA III save file. This block, known as the
 * "PlayerInfo" block, contains additional information about the player that is
 * not defined in the "PlayerPeds" block (block 1).
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 31, 2015
 */
public class Block16 extends Block
{
    public Block16(SaveFile.Platform platform, VariableDefinitions vars)
    {
        super(Identifier.BLOCK16, vars, new DataStructure[] {
            new PlayerInfoData(platform, vars)
        });
    }
    
    private static class PlayerInfoData extends Block
    {
        public PlayerInfoData(SaveFile.Platform platform, VariableDefinitions vars)
        {
            super(Identifier.PLAYER_INFO_DATA, vars, new DataStructure[] {
                vars.iMoney.asDataStructure(platform),
                new Align(platform, -1, -1, 0x000B, -1, -1),
                vars.iMoney2.asDataStructure(platform),
                vars.iHiddenPackagesCollected.asDataStructure(platform),
                vars.iHiddenPackagesTotal.asDataStructure(platform),
                vars.bInfiniteSprint.asDataStructure(platform),
                vars.bFastReload.asDataStructure(platform),
                vars.bGetOutOfJailFree.asDataStructure(platform),
                vars.bFreeHealthcare.asDataStructure(platform),
                new Align(platform, -1, -1, 0x011D, -1, -1)
            });
        }
    }
}