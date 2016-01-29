
package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarByte;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarInt;

/**
 * Created on Jan 18, 2016.
 *
 * @author thehambone
 */
public class Gang extends Record
{
    public final VarInt nVehicleModelID = new VarInt();
    public final VarByte nPedModelOverrideIndex = new VarByte();
    public final VarInt nWeaponID1 = new VarInt();
    public final VarInt nWeaponID2 = new VarInt();
    
    public Gang()
    {
        super(0x10);
    }
    
    public void load(DataBuffer buf)
    {
        nVehicleModelID.load(buf, offset + 0x00);
        nPedModelOverrideIndex.load(buf, offset + 0x04);
        nWeaponID1.load(buf, offset + 0x08);
        nWeaponID2.load(buf, offset + 0x0C);
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            nVehicleModelID, nPedModelOverrideIndex, nWeaponID1, nWeaponID2
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
        throw new UnsupportedPlatformException("Xbox not supported yet.");
    }
}
