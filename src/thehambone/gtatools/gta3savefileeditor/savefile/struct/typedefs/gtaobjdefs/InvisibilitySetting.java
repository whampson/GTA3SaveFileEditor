package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;
import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;

/**
 * An InvisibilitySetting object. Not much is known about this data structure.
 * 
 * InvisibilitySettings are stored in a 20-element array in the "SimpleVars"
 * block (block 0).
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 14, 2015
 */
public class InvisibilitySetting extends GTAObject
{
    public static final int SIZE = 8;
    
    private int type, handle;
    
    public InvisibilitySetting()
    {
        this.type = 0;
        this.handle = 0;
    }
    
    public InvisibilitySetting(int type, int handle)
    {
        this.type = type;
        this.handle = handle;
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
        return in.getPointer() - startOffset;
    }

    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.writeInt(type);
        out.writeInt(handle);
        return out.getPointer() - startOffset;
    }

}