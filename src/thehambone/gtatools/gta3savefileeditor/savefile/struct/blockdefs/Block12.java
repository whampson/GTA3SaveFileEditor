package thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Block;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.DataStructure;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAStringUTF8;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;

/**
 * The 13th of 20 main blocks that make up a GTA III save file. Data about the
 * game's 7 gangs (plus 2 unused gangs) is stored in this block.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 14, 2015
 * @deprecated 
 */
public class Block12 extends Block
{
    public Block12(SaveFile.Platform platform, VariableDefinitions vars)
    {
        super(Identifier.BLOCK12, vars, new DataStructure[] {
             new SubBlockGNG(platform, vars)
        });
    }
    
    private static class SubBlockGNG extends Block
    {
        public SubBlockGNG(SaveFile.Platform platform, VariableDefinitions vars)
        {
            super(Identifier.GNG, vars, new DataStructure[] {
                new GTAStringUTF8("GNG", 4),
                new SubBlockGangData(platform, vars)
            });
        }
    }
    
    private static class SubBlockGangData extends Block
    {
        public SubBlockGangData(SaveFile.Platform platform, VariableDefinitions vars)
        {
            super(Identifier.GANG_DATA, vars, new DataStructure[] {
                vars.aGangs.asDataStructure(platform)
            });
        }
    }
}