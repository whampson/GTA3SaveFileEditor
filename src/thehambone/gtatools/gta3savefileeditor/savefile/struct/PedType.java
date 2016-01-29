
package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarInt;

/**
 * Created on Jan 19, 2016.
 *
 * @author thehambone
 */
public class PedType extends Record
{
    public final VarInt nPedTypeMask = new VarInt();
    public final VarInt nThreatFlags = new VarInt();
    public final VarInt nAvoidFlags = new VarInt();
    
    public PedType()
    {
        super(0x20);
    }
    
    private void load(DataBuffer buf)
    {
        nPedTypeMask.load(buf, offset + 0x00);
        nThreatFlags.load(buf, offset + 0x18);
        nAvoidFlags.load(buf, offset + 0x1C);
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            nPedTypeMask, nThreatFlags, nAvoidFlags
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
        throw new UnsupportedPlatformException("PS2 not supported yet.");
    }
    
    @Override
    protected void loadXbox(DataBuffer buf)
    {
        load(buf);
    }
}
