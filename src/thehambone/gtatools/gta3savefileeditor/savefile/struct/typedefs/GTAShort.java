package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs;

import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.GTADataType;

/**
 *
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 22, 2015
 * @deprecated 
 */
public class GTAShort extends GTADataType
{
    private short value;
    
    public GTAShort()
    {
        value = 0;
    }
     
    public GTAShort(short value)
    {
        this.value = value;
    }
    
    public GTAShort(String value)
    {
        this.value = Short.parseShort(value);
    }
    
    public short shortValue()
    {
        return value;
    }
    
    @Override
    public String toString()
    {
        return Short.toString(value);
    }
    
    @Override
    public int getSize()
    {
        return SIZEOF_SHORT;
    }
    
    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        value = in.readShort();
        return in.getPointer() - startOffset;
    }
    
    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        return out.writeShort(value);
    }
}