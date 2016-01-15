
package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;

/**
 * Created on Jan 14, 2016.
 *
 * @author thehambone
 */
public class WeaponSlot extends Record
{
    public final VarInt nWeaponID = new VarInt();
    public final VarInt nClipAmmo = new VarInt();
    public final VarInt nWeaponAmmo = new VarInt();
    
    public WeaponSlot()
    {
        super(0x18);
    }
    
    private void load(DataBuffer buf)
    {
        nWeaponID.load(buf, offset + 0x00);
        nClipAmmo.load(buf, offset + 0x08);
        nWeaponAmmo.load(buf, offset + 0x0C);
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            nWeaponID, nClipAmmo, nWeaponAmmo
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
