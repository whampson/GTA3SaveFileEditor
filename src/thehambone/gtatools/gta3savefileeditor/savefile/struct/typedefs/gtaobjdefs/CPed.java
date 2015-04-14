package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;
import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAFloat;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;

/**
 * A CPed object is a dump of data from the CPed class from the GTA III
 * game executable. There is only one CPed dump in a save file, however, and it
 * links to the player. This class dump is located in the in the "PlayerPeds"
 * block (block 1).
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 20, 2015
 */
public class CPed extends GTAObject
{
    public static final int SIZE = 1520;
    
    private byte[] b0, b1, b2, b3;
    private Variable<WeaponSlot> weaponSlots;
    private Variable<GTAFloat> playerHealth, playerArmor, x, y, z;
    
    public CPed()
    {
        b0 = new byte[52];
        b1 = new byte[640];
        b2 = new byte[148];
        b3 = new byte[348];
        
        x = new Variable<>("playerX", false, false, true, false, false, new GTAFloat());
        y = new Variable<>("playerY", false, false, true, false, false, new GTAFloat());
        z = new Variable<>("playerZ", false, false, true, false, false, new GTAFloat());
        playerHealth = new Variable<>("playerHealth", false, false, true, false, false, new GTAFloat());
        playerArmor = new Variable<>("playerHealth", false, false, true, false, false, new GTAFloat());
        weaponSlots = new Variable<>("weaponSlots", false, false, true, false, false, new WeaponSlot(), 13);
    }
    
    public Variable getPlayerXAsVariable()
    {
        return x;
    }
    
    public Variable getPlayerYAsVariable()
    {
        return y;
    }
    
    public Variable getPlayerZAsVariable()
    {
        return z;
    }
    
    public Variable getHealthAsVariable()
    {
        return playerHealth;
    }
    
    public Variable getArmorAsVariable()
    {
        return playerArmor;
    }
    
    public Variable getWeaponSlotsAsVariable()
    {
        return weaponSlots;
    }
    
    @Override
    public int getSize()
    {
        return SIZE;
    }

    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        in.read(b0);
        x.setValue(new GTAFloat(in.readFloat()));
        y.setValue(new GTAFloat(in.readFloat()));
        z.setValue(new GTAFloat(in.readFloat()));
        in.read(b1);
        playerHealth.setValue(new GTAFloat(in.readFloat()));
        playerArmor.setValue(new GTAFloat(in.readFloat()));
        in.read(b2);
        for (int i = 0; i < weaponSlots.getElementCount(); i++) {
            WeaponSlot w = new WeaponSlot();
            w.load(in);
            weaponSlots.setValueAt(i, w);
        }
        in.read(b3);
        return in.getPointer() - startOffset;
    }

    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.write(b0);
        out.writeFloat(x.getValue().floatValue());
        out.writeFloat(y.getValue().floatValue());
        out.writeFloat(z.getValue().floatValue());
        out.write(b1);
        out.writeFloat(playerHealth.getValue().floatValue());
        out.writeFloat(playerArmor.getValue().floatValue());
        out.write(b2);
        for (int i = 0; i < weaponSlots.getElementCount(); i++) {
            weaponSlots.getValueAt(i).save(out);
        }
        out.write(b3);
        return out.getPointer() - startOffset;
    }
}