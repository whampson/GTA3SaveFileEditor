package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarArray;

/**
 * Created on Jan 20, 2016.
 *
 * @author thehambone
 */
public class SaveCarGarageSlot extends Record
{
    public final VarArray<StoredCar> aStoredCar
            = new VarArray<>(StoredCar.class, 3);
    public SaveCarGarageSlot()
    {
        super(0x78);
    }
    
    private void load(DataBuffer buf, SaveFile.Platform platform)
    {
        aStoredCar.load(buf, offset + 0x00, platform);
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            aStoredCar
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
