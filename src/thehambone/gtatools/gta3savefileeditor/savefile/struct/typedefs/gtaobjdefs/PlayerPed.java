package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;
import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAInteger;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;

/**
 * (NEED INFO)
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 20, 2015
 * @deprecated 
 */
public class PlayerPed extends GTAObject
{
    public static final int SIZE = 1562;
    
    private int i00;
    private short s04;
    private int i06;
    private CPed cPed;
    private Variable<GTAInteger> maxWantedLevel, maxChaosLevel;
    private String modelName;
    
    public PlayerPed()
    {
        i00 = 0;
        s04 = 0;
        i06 = 0;
        cPed = new CPed();
        maxWantedLevel = new Variable<>("maxWantedLevel", false, false, true, false, false, new GTAInteger());
        maxChaosLevel = new Variable<>("maxWantedLevel", false, false, true, false, false, new GTAInteger());
        modelName = "player";
    }
    
    public Variable getMaxChaosLevelAsVariable()
    {
        return maxChaosLevel;
    }
    
    public Variable getMaxWantedLevelAsVariable()
    {
        return maxWantedLevel;
    }
    
    public CPed getCPed()
    {
        return cPed;
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
        i00 = in.readInt();
        s04 = in.readShort();
        i06 = in.readInt();
        cPed.load(in);
        maxWantedLevel.setValue(new GTAInteger(in.readInt()));
        maxChaosLevel.setValue(new GTAInteger(in.readInt()));
        modelName = in.readStringUTF8(24);
        return in.getPointer() - startOffset;
    }

    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.writeInt(i00);
        out.writeShort(s04);
        out.writeInt(i06);
        cPed.save(out);
        out.writeInt(maxWantedLevel.getValue().intValue());
        out.writeInt(maxChaosLevel.getValue().intValue());
        out.writeStringUTF8(modelName, 24);
        return out.getPointer() - startOffset;
    }
}