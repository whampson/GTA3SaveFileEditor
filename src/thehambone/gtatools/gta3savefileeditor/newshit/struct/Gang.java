
package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarBoolean8;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;

/**
 * Created on Jan 18, 2016.
 *
 * @author thehambone
 */
public class Gang extends Record
{
    public final VarInt nVehicleModelID = new VarInt();
    public final VarBoolean8 nPedModelOverrideIndex = new VarBoolean8();
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
