package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAInteger;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;

/**
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 01, 2015
 */
public class WeaponSlot extends GTAObject
{
    public static final int SIZE = 24;
    
    private int weaponID, bulletsInClip, i04, i10, i14;
    private Variable<GTAInteger> bulletsTotal;
    
    public WeaponSlot()
    {
        weaponID = 0;
        bulletsInClip = 0;
        i04 = 0;
        i10 = 0;
        i14 = 0;
        
        bulletsTotal = new Variable<>("bullets", false, false, true, false, false, new GTAInteger());
    }
    
    public int getWeaponID()
    {
        return weaponID;
    }
    
//    public int getBulletsInClip()
//    {
//        return bulletsInClip;
//    }
    
    public Variable getBulletsTotalAsVariable()
    {
        return bulletsTotal;
    }
    
    public void setWeaponID(int weaponID)
    {
        this.weaponID = weaponID;
    }
    
//    public void setBulletsInClip(int bulletsInClip)
//    {
//        this.bulletsInClip = bulletsInClip;
//    }
//    
    
    @Override
    public int getSize()
    {
        return SIZE;
    }
    
    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        weaponID = in.readInt();
        i04 = in.readInt();
        bulletsInClip = in.readInt();
        bulletsTotal.setValue(new GTAInteger(in.readInt()));
        i10 = in.readInt();
        i14 = in.readInt();
        return in.getPointer() - startOffset;
    }
    
    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.writeInt(weaponID);
        out.writeInt(i04);
        out.writeInt(bulletsInClip);
        out.writeInt(bulletsTotal.getValue().intValue());
        out.writeInt(i10);
        out.writeInt(i14);
        return out.getPointer() - startOffset;
    }
}