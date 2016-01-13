package thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Align;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Block;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.DataStructure;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;

/**
 * The second of 20 main blocks in a GTA III save file. This block is known as
 * the "PlayerPeds" block because it contains information about the player.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 20, 2015
 * @deprecated 
 */
public class Block01 extends Block
{
    public Block01(SaveFile.Platform platform, VariableDefinitions vars)
    {
        super(Identifier.BLOCK01, vars, new DataStructure[] {
            new SubBlockPlayerPeds(platform, vars),
            new Align(platform, -1, -1, 0x0002, -1, -1)
        });
    }
    
    private static class SubBlockPlayerPeds extends Block
    {
        public SubBlockPlayerPeds(SaveFile.Platform platform, VariableDefinitions vars)
        {
            super(Identifier.PLAYER_PED_DATA, vars, new DataStructure[] {
                vars.iNumPlayers.asDataStructure(platform),
                vars.aPlayerPed.asDataStructure(platform, vars.iNumPlayers)
            });
        }
    }
}