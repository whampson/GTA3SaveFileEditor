
package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarBoolean32;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarInt;

/**
 * Created on Jan 20, 2016.
 *
 * @author thehambone
 */
public class BlockGarages extends Block
{
    public final VarBoolean32 bFreeBombs = new VarBoolean32();
    public final VarBoolean32 bFreeResprays = new VarBoolean32();
    public final VarInt nBankVansCollected = new VarInt();
    public final VarInt nCollectCars1Status = new VarInt();
    public final VarInt nCollectCars2Status = new VarInt();
    public final VarArray<SaveCarGarageSlot> aSaveCarGarageSlot
            = new VarArray<>(SaveCarGarageSlot.class, 6);
    
    public BlockGarages(int blockSize)
    {
        super("Garages", blockSize);
    }
    
    private void load(DataBuffer buf, SaveFile.Platform platform)
    {
        bFreeBombs.load(buf, offset + 0x08);
        bFreeResprays.load(buf, offset + 0x0C);
        nBankVansCollected.load(buf, offset + 0x14);
        nCollectCars1Status.load(buf, offset + 0x1C);
        nCollectCars2Status.load(buf, offset + 0x20);
        aSaveCarGarageSlot.load(buf, offset + 0x2C, platform);
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
            bFreeBombs, bFreeResprays, nBankVansCollected, nCollectCars1Status,
            nCollectCars2Status, aSaveCarGarageSlot
        };
    }

    @Override
    protected void loadAndroid(DataBuffer buf)
    {
        load(buf, SaveFile.Platform.ANDROID);
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        load(buf, SaveFile.Platform.IOS);
    }
    
    @Override
    protected void loadPC(DataBuffer buf)
    {
        load(buf, SaveFile.Platform.PC);
    }
    
    @Override
    protected void loadPS2(DataBuffer buf)
    {
        throw new UnsupportedPlatformException("PS2 not supported yet.");
    }
    
    @Override
    protected void loadXbox(DataBuffer buf)
    {
        load(buf, SaveFile.Platform.XBOX);
    }
}
