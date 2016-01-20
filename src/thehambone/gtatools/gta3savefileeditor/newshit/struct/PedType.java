
package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;

/**
 * Created on Jan 19, 2016.
 *
 * @author thehambone
 */
public class PedType extends Record
{
    public final VarInt nThreatFlags = new VarInt();
    
    public PedType()
    {
        super(0x20);
    }
    
    private void load(DataBuffer buf)
    {
        nThreatFlags.load(buf, offset + 0x18);
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            nThreatFlags
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
