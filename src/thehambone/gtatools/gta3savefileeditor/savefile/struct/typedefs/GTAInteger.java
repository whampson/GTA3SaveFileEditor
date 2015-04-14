package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs;

import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.GTADataType;

/**
 *
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 21, 2015
 */
public class GTAInteger extends GTADataType
{
    private int value;
    
    public GTAInteger()
    {
        value = 0;
    }
    
    public GTAInteger(int value)
    {
        this.value = value;
    }
    
    public GTAInteger(String value)
    {
        this.value = Integer.parseInt(value);
    }
    
    public int intValue()
    {
        return value;
    }
    
    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public int getSize()
    {
        return SIZEOF_INT;
    }
    
    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        value = in.readInt();
        return in.getPointer() - startOffset;
    }
    
    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        return out.writeInt(value);
    }
}