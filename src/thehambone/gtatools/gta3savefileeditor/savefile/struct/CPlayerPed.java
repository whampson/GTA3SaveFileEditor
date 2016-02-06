package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarFloat;

/**
 * Created on Jan 14, 2016.
 *
 * @author thehambone
 */
public class CPlayerPed extends Record
{
    public final RWv3D vPosition = new RWv3D();
    public final VarFloat fHealth = new VarFloat();
    public final VarFloat fArmor = new VarFloat();
    public final VarArray<WeaponSlot> aWeaponSlot
            = new VarArray<>(WeaponSlot.class, 13);
    
    public CPlayerPed()
    {
        super(1);   // Size varies by platform
    }
    
    
    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            vPosition, fHealth, fArmor
        };
    }
    
    @Override
    protected void loadAndroid(DataBuffer buf)
    {
        this.size = 0x0618;
        
        vPosition.load(buf, offset + 0x34, SaveFile.Platform.ANDROID);
        fHealth.load(buf, offset + 0x2C8);
        fArmor.load(buf, offset + 0x02CC);
        aWeaponSlot.load(buf, offset + 0x0364, SaveFile.Platform.ANDROID);
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        this.size = 0x0614;
        
        vPosition.load(buf, offset + 0x34, SaveFile.Platform.IOS);
        fHealth.load(buf, offset + 0x2C4);
        fArmor.load(buf, offset + 0x02C8);
        aWeaponSlot.load(buf, offset + 0x0350, SaveFile.Platform.IOS);
    }
    
    @Override
    protected void loadPC(DataBuffer buf)
    {
        this.size = 0x05F0;
        
        vPosition.load(buf, offset + 0x34, SaveFile.Platform.PC);
        fHealth.load(buf, offset + 0x2C0);
        fArmor.load(buf, offset + 0x02C4);
        aWeaponSlot.load(buf, offset + 0x035C, SaveFile.Platform.PC);
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
