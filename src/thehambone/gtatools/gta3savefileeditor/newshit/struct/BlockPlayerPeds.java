
package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;

/**
 * Created on Jan 14, 2016.
 *
 * @author thehambone
 */
public class BlockPlayerPeds extends Block
{
    public final VarInt nNumberOfPlayerPeds = new VarInt();
    public final VarArray<PlayerPed> aPlayerPed = new VarArray<>(
            PlayerPed.class, 0);
    
    public BlockPlayerPeds(int size)
    {
        super("PlayerPeds", size);
    }
    
    @Override
    public void setSize(int size)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            nNumberOfPlayerPeds, aPlayerPed
        };
    }
    
    @Override
    protected void loadAndroid(DataBuffer buf)
    {
        nNumberOfPlayerPeds.load(buf, offset + 0x04);
        
        aPlayerPed.setElementCount(nNumberOfPlayerPeds.getValue());
        aPlayerPed.load(buf, offset + 0x08, SaveFileNew.Platform.ANDROID);
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        nNumberOfPlayerPeds.load(buf, offset + 0x04);
        
        aPlayerPed.setElementCount(nNumberOfPlayerPeds.getValue());
        aPlayerPed.load(buf, offset + 0x08, SaveFileNew.Platform.IOS);
    }
    
    @Override
    protected void loadPC(DataBuffer buf)
    {
        nNumberOfPlayerPeds.load(buf, offset + 0x04);
        
        aPlayerPed.setElementCount(nNumberOfPlayerPeds.getValue());
        aPlayerPed.load(buf, offset + 0x08, SaveFileNew.Platform.PC);
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
