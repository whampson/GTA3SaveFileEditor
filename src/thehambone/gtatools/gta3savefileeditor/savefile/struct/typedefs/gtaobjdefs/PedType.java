package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;

/**
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 30, 2015
 * @deprecated 
 */
public class PedType extends GTAObject
{
    public static final int SIZE = 32;
    
    private int i00, threat, i1C;
    private float f04, f08, f0C, f10, f14;
    
    public int getThreat()
    {
        return threat;
    }
    
    public void setThreat(int threat)
    {
        this.threat = threat;
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
        i00 = in.readInt();
        f04 = in.readFloat();
        f08 = in.readFloat();
        f0C = in.readFloat();
        f10 = in.readFloat();
        f14 = in.readFloat();
        threat = in.readInt();
        i1C = in.readInt();
        return in.getPointer() - startOffset;
    }
    
    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.writeInt(i00);
        out.writeFloat(f04);
        out.writeFloat(f08);
        out.writeFloat(f0C);
        out.writeFloat(f10);
        out.writeFloat(f14);
        out.writeInt(threat);
        out.writeInt(i1C);
        return out.getPointer() - startOffset;
    }
}