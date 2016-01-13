package thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Block;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.DataStructure;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAStringUTF8;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;

/**
 * The 19th of 20 main blocks in a GTA III save file. This block contains
 * information about the various ped types found in the game. For this reason,
 * it is known as the "PedTypes" block.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 30, 2015
 * @deprecated 
 */
public class Block19 extends Block
{
    public Block19(SaveFile.Platform platform, VariableDefinitions vars)
    {
        super(Identifier.BLOCK19, vars, new DataStructure[] {
            new SubBlockPTP(platform, vars)
        });
    }
    
    private static class SubBlockPTP extends Block
    {
        public SubBlockPTP(SaveFile.Platform platform, VariableDefinitions vars)
        {
            super(Identifier.PTP, vars, new DataStructure[] {
                new GTAStringUTF8("PTP", 4),
                new SubBlockPedTypeData(platform, vars)
            });
        }
    }
    
    private static class SubBlockPedTypeData extends Block
    {
        public SubBlockPedTypeData(SaveFile.Platform platform, VariableDefinitions vars)
        {
            super(Identifier.PED_TYPE_DATA, vars, new DataStructure[] {
                vars.aPedTypes.asDataStructure(platform)
            });
        }
    }
}