package thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Align;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Block;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.DataStructure;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;

/**
 * The third of 20 main blocks in a GTA III save file. This block is known as
 * the "Garages" block because it contains data pertaining to various garages
 * in the game, including the garages used by the player to store cars as well
 * as garages used in missions.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 02, 2015
 */
public class Block02 extends Block
{
    public Block02(SaveFile.Platform platform, VariableDefinitions vars)
    {
        super(Block.Identifier.BLOCK02, vars, new DataStructure[] {
            new GarageData(platform, vars)
        });
    }
    
    public static class GarageData extends Block
    {
        public GarageData(SaveFile.Platform platform, VariableDefinitions vars)
        {
            super(Block.Identifier.GARAGE_DATA, vars, new DataStructure[] {
                new Align(platform, -1, -1, 0x0004, -1, -1),
                vars.bFreeBombs.asDataStructure(platform),
                vars.bFreeResprays.asDataStructure(platform),
                new Align(platform, -1, -1, 0x001C, -1, -1),
                vars.aSaveGarageSlots.asDataStructure(platform),
                new Align(platform, -1, -1, 0x1274, -1, -1)
            });
        }
    }
}