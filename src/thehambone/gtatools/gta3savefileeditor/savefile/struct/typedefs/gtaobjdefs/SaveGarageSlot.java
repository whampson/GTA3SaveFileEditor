package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;

/**
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 04, 2015
 * @deprecated 
 */
public class SaveGarageSlot extends GTAObject
{
    public static final int SIZE = 120;
    
    private StoredCar portland, staunton, shoreside;
    
    public SaveGarageSlot()
    {
        portland = new StoredCar();
        staunton = new StoredCar();
        shoreside = new StoredCar();
    }
    
    public StoredCar[] getStoredCars()
    {
        return new StoredCar[] { portland, staunton, shoreside };
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
        portland.load(in);
        staunton.load(in);
        shoreside.load(in);
        return in.getPointer() - startOffset;
    }
    
    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        portland.save(out);
        staunton.save(out);
        shoreside.save(out);
        return out.getPointer() - startOffset;
    }
}