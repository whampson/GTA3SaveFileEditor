package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarFloat;


/**
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public class RWv3D extends Record
{
    public final VarFloat fX = new VarFloat();
    public final VarFloat fY = new VarFloat();
    public final VarFloat fZ = new VarFloat();
    
    public RWv3D()
    {
        super(0x0C);
    }
    
    private void load(DataBuffer buf)
    {
        fX.load(buf, offset);
        fY.load(buf, offset + 0x04);
        fZ.load(buf, offset + 0x08);
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new VarFloat[] {
            fX, fY, fZ
        };
    }
    
    @Override
    protected void loadAndroid(DataBuffer buf)
    {
        load(buf);
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        load(buf);
    }
    
    @Override
    protected void loadPC(DataBuffer buf)
    {
        load(buf);
    }
    
    @Override
    protected void loadPS2(DataBuffer buf)
    {
        load(buf);
    }
    
    @Override
    protected void loadXbox(DataBuffer buf)
    {
        load(buf);
    }
}
