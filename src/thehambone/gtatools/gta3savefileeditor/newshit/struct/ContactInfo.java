package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;

/**
 * Created on Sep 20, 2015.
 * 
 * @author thehambone
 */
public class ContactInfo extends Record
{
    public VarInt pMissionFlag = new VarInt();
    public VarInt nBaseBrief = new VarInt();
    
    public ContactInfo()
    {
        super(0x08);
    }
    
    private void load(DataBuffer buf)
    {
        pMissionFlag.load(buf, offset + 0x00);
        nBaseBrief.load(buf, offset + 0x04);
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new VarInt[] {
            pMissionFlag, nBaseBrief
        };
    }
    
    @Override
    protected void loadAndroid(DataBuffer buf)
    {
        throw new UnsupportedPlatformException("Android not supported yet.");
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        throw new UnsupportedPlatformException("iOS not supported yet.");
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
        throw new UnsupportedPlatformException("Xbox not supported yet.");
    }
}
