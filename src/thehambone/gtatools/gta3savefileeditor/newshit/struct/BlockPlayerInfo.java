
package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarBoolean8;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;

/**
 * Created on Jan 13, 2016.
 *
 * @author thehambone
 */
public class BlockPlayerInfo extends Block
{
    public final VarInt nMoney = new VarInt();
    public final VarInt nMoneyOnScreen = new VarInt();
    public final VarInt nHiddenPackagesCollected = new VarInt();
    public final VarInt nHiddenPackagesCount = new VarInt();
    public final VarBoolean8 bPlayerNeverGetsTired = new VarBoolean8();
    public final VarBoolean8 bPlayerFastReload = new VarBoolean8();
    public final VarBoolean8 bPlayerGetOutOfJailFree = new VarBoolean8();
    public final VarBoolean8 bPlayerFreeHealthCare = new VarBoolean8();
    
    protected final VarInt blockSize = new VarInt();
    
    public BlockPlayerInfo(int size)
    {
        super("PlayerInfo", size);
    }
    
    private void load(DataBuffer buf)
    {
        blockSize.load(buf, offset + 0x00);
        nMoney.load(buf, offset + 0x04);
        nMoneyOnScreen.load(buf, offset + 0x13);
        nHiddenPackagesCollected.load(buf, offset + 0x17);
        nHiddenPackagesCount.load(buf, offset + 0x1B);
        bPlayerNeverGetsTired.load(buf, offset + 0x1F);
        bPlayerFastReload.load(buf, offset + 0x20);
        bPlayerGetOutOfJailFree.load(buf, offset + 0x21);
        bPlayerFreeHealthCare.load(buf, offset + 0x22);
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
            nMoney, nMoneyOnScreen, nHiddenPackagesCollected,
            nHiddenPackagesCount, bPlayerNeverGetsTired, bPlayerFastReload,
            bPlayerGetOutOfJailFree, bPlayerFreeHealthCare
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
