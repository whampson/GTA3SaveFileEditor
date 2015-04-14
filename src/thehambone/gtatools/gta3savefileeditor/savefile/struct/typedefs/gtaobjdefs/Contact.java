package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;
import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;

/**
 * A Contact object contains information about mission contacts. Not much is
 * known about this object.
 * 
 * Contacts are stored in a 16-element array in the "SimpleVars" block
 * (block 0).
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 14, 2015
 */
public class Contact extends GTAObject
{
    public static final int SIZE = 8;
    
    private int onAMissionFlag, baseBriefID;
    
    public Contact()
    {
        this.onAMissionFlag = 0;
        this.baseBriefID = 0;
    }
    
    public Contact(int onAMissionFlag, int baseBriefID)
    {
        this.onAMissionFlag = onAMissionFlag;
        this.baseBriefID = baseBriefID;
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
        onAMissionFlag = in.readInt();
        baseBriefID = in.readInt();
        return in.getPointer() - startOffset;
    }

    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.writeInt(onAMissionFlag);
        out.writeInt(baseBriefID);
        return out.getPointer() - startOffset;
    }
}