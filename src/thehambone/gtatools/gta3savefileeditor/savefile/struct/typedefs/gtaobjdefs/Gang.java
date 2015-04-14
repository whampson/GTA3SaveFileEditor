package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;
import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAByte;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAInteger;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;

/**
 * A GangObject contains information about a gang in the game. Gang objects are
 * stored in an array in the "Gangs" block (block 12). There are 9 gang objects
 * in the array, 1 for each gang.
 * Array indices:
 *      0: Mafia
 *      1: Triads
 *      2: Diablos
 *      3: Yakuza
 *      4: Yardies
 *      5: Colombian Cartel
 *      6: Southside Hoods
 *      7: (unused)
 *      8: (unused)
 * 
 * The "ped model override" flag controls which ped model is loaded out of a
 * particular gang's 2 defined ped models. A value of -1 tells the game to load
 * both models, 0 loads the first model, and 1 loads the second model.
 * 
 * The "ped model override flag" is responsible for causing the Purple Nines
 * glitch. The Purple Nines glitch is scenario in which only 1 variant of the
 * Southside Hoods's gang members spawns on the map. Many of D-Ice's missions
 * require killing a number of the Southside Hoods's other ped variant, known as
 * the "Purple Nines", in order to pass. Since the glitch causes the Purple
 * Nines to never spawn, the missions are impossible to pass and as a result,
 * 100% completion in the game is no longer attainable.
 * The Purple Nines glitch occurs when the user loads a game in which D-Ice's
 * mission strand has been completed and then attempts to start a new game by
 * going to the "Load" menu and selecting "New Game". After D-Ice's missions are
 * completed, the game, by using the "ped model override" flag, no longer loads
 * the Purple Nines variant of the Southside Hoods, as they are said to have
 * been "wiped out" by the player. Upon loading a new game, some values are
 * retained in memory, one of these being the status of the "ped model override"
 * flag for the Southside Hoods. As a result, when the user saves their new game
 * for the first time, the "leaked" value from the previous game will be written
 * to the new save, thus causing the glitch scenario to occur.
 * The glitch is easily fixed by setting the "ped model override" flag for the
 * Southside Hoods back to -1 (0xFF).
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 14, 2015
 */
public class Gang extends GTAObject
{
    public static final int SIZE = 16;
    
    private Variable<GTAInteger> vehicleID, primaryWeaponID, secondaryWeaponID;
    private Variable<GTAByte> pedModelOverride;
    
    public Gang()
    {
        vehicleID = new Variable<>("vehicle", false, false, true, false, false, new GTAInteger());
        pedModelOverride = new Variable<>("pedModelOverride", false, false, true, false, false, new GTAByte());
        primaryWeaponID = new Variable<>("primaryWeaponID", false, false, true, false, false, new GTAInteger());
        secondaryWeaponID = new Variable<>("secondaryWeaponID", false, false, true, false, false, new GTAInteger());
    }
    
    public Variable<GTAInteger> getVehicleIDAsVariable()
    {
        return vehicleID;
    }
    
    public Variable<GTAInteger> getPrimaryWeaponIDAsVariable()
    {
        return primaryWeaponID;
    }
    
    public Variable<GTAInteger> getSecondaryWeaponIDAsVariable()
    {
        return secondaryWeaponID;
    }
    
    public Variable<GTAByte> getPedModelOverrideFlagAsVariable()
    {
        return pedModelOverride;
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
        vehicleID.setValue(new GTAInteger(in.readInt()));
        pedModelOverride.setValue(new GTAByte(in.read()));
        in.skip(3);
        primaryWeaponID.setValue(new GTAInteger(in.readInt()));
        secondaryWeaponID.setValue(new GTAInteger(in.readInt()));
        return in.getPointer() - startOffset;
    }

    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.writeInt(vehicleID.getValue().intValue());
        out.write(pedModelOverride.getValue().byteValue());
        out.skip(3);
        out.writeInt(primaryWeaponID.getValue().intValue());
        out.writeInt(secondaryWeaponID.getValue().intValue());
        return out.getPointer() - startOffset;
    }
}