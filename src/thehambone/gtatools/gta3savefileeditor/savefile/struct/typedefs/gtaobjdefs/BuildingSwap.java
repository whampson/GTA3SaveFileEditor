package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;
import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;

/**
 * A BuildingSwap object contains data pertaining to replaceable building
 * models(?). Little is known about this object.
 * 
 * BuildingSwaps are stored in a 25-element array in the "SimpleVars" block
 * (block 0).
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 14, 2015
 */
public class BuildingSwap extends GTAObject
{
    public static final int SIZE = 16;
    
    private int type, handle, newModelID, oldModelID;
    
    public BuildingSwap()
    {
        type = 0;
        handle = 0;
        newModelID = -1;
        oldModelID = -1;
    }

    @Override
    public int getSize()
    {
        return SIZE;
    }

    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        type = in.readInt();
        handle = in.readInt();
        newModelID = in.readInt();
        oldModelID = in.readInt();
        return in.getPointer() - startOffset;
    }

    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.writeInt(type);
        out.writeInt(handle);
        out.writeInt(newModelID);
        out.writeInt(oldModelID);
        return out.getPointer() - startOffset;
    }
}