package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAByte;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAInteger;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;

/**
 * A car stored in one of the garages. StoredCars are located in the "Garages"
 * block (block 2).
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 04, 2015
 * @deprecated 
 */
public class StoredCar extends GTAObject
{
    public static final int SIZE = 40;
    
    private Variable<GTAInteger> modelID, immunities;
    private Variable<GTAByte> primaryColorID, secondaryColorID, radioStationID, modelVariationA, modelVariationB, bombType;
    private Coord position, rotation;
    
    public StoredCar()
    {
        modelID = new Variable<>("modelID", false, false, true, false, false, new GTAInteger());
        immunities = new Variable<>("immunities", false, false, true, false, false, new GTAInteger());
        primaryColorID = new Variable<>("prmaryColorID", false, false, true, false, false, new GTAByte());
        secondaryColorID = new Variable<>("secondaryColorID", false, false, true, false, false, new GTAByte());
        radioStationID = new Variable<>("radioStationID", false, false, true, false, false, new GTAByte());
        modelVariationA = new Variable<>("prmaryColorID", false, false, true, false, false, new GTAByte());
        modelVariationB = new Variable<>("prmaryColorID", false, false, true, false, false, new GTAByte());
        bombType = new Variable<>("bombType", false, false, true, false, false, new GTAByte());
        position = new Coord();
        rotation = new Coord();
    }
    
    public Variable<GTAInteger> getModelIDAsVariable()
    {
        return modelID;
    }
    
    public Variable<GTAInteger> getImmunitiesAsVariable()
    {
        return immunities;
    }
    
    public Variable<GTAByte> getPrimaryColorIDAsVariable()
    {
        return primaryColorID;
    }
    
    public Variable<GTAByte> getSecondaryColorIDAsVariable()
    {
        return secondaryColorID;
    }
    
    public Variable<GTAByte> getRadioStationIDAsVariable()
    {
        return radioStationID;
    }
    
    public Variable<GTAByte> getBombTypeAsVariable()
    {
        return bombType;
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
        modelID.setValue(new GTAInteger(in.readInt()));
        position.load(in);
        rotation.load(in);
        immunities.setValue(new GTAInteger(in.readInt()));
        primaryColorID.setValue(new GTAByte(in.read()));
        secondaryColorID.setValue(new GTAByte(in.read()));
        radioStationID.setValue(new GTAByte(in.read()));
        modelVariationA.setValue(new GTAByte(in.read()));
        modelVariationB.setValue(new GTAByte(in.read()));
        bombType.setValue(new GTAByte(in.read()));
        in.skip(2);
        return in.getPointer() - startOffset;
    }

    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.writeInt(modelID.getValue().intValue());
        position.save(out);
        rotation.save(out);
        out.writeInt(immunities.getValue().intValue());
        out.write(primaryColorID.getValue().byteValue());
        out.write(secondaryColorID.getValue().byteValue());
        out.write(radioStationID.getValue().byteValue());
        out.write(modelVariationA.getValue().byteValue());
        out.write(modelVariationB.getValue().byteValue());
        out.write(bombType.getValue().byteValue());
        out.skip(2);
        return out.getPointer() - startOffset;
    }
}