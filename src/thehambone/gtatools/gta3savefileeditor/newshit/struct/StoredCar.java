
package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarByte;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarFloat;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;

/**
 * Created on Jan 20, 2016.
 *
 * @author thehambone
 */
public class StoredCar extends Record
{
    public final VarInt nModelID = new VarInt();
    public final RWv3D vPosition = new RWv3D();
    public final VarArray<VarFloat> aRotation
            = new VarArray<>(VarFloat.class, 3);
    public final VarInt nImmunities = new VarInt();
    public final VarByte nPrimaryColorID = new VarByte();
    public final VarByte nSecondaryColorID = new VarByte();
    public final VarByte nRadioStationID = new VarByte();
    public final VarByte nModelVariationA = new VarByte();
    public final VarByte nModelVariationB = new VarByte();
    public final VarByte nBombID = new VarByte();
    
    public StoredCar()
    {
        super(0x28);
    }
    
    private void load(DataBuffer buf, SaveFile.Platform platform)
    {
        nModelID.load(buf, offset + 0x00);
        vPosition.load(buf, offset + 0x04, platform);
        aRotation.load(buf, offset + 0x10, platform);
        nImmunities.load(buf, offset + 0x1C);
        nPrimaryColorID.load(buf, offset + 0x20);
        nSecondaryColorID.load(buf, offset + 0x21);
        nRadioStationID.load(buf, offset + 0x22);
        nModelVariationA.load(buf, offset + 0x23);
        nModelVariationB.load(buf, offset + 0x24);
        nBombID.load(buf, offset + 0x25);
    }

    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            nModelID, vPosition, aRotation, nImmunities, nPrimaryColorID,
            nSecondaryColorID, nRadioStationID, nModelVariationA,
            nModelVariationB, nBombID
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
