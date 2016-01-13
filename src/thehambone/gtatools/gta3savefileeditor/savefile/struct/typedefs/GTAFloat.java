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
public class GTAFloat extends GTADataType
{
    private float value;
    
    public GTAFloat()
    {
        value = 0;
    }
    
    public GTAFloat(float value)
    {
        this.value = value;
    }
    
    public GTAFloat(String value)
    {
        this.value = Float.parseFloat(value);
    }
    
    public float floatValue()
    {
        return value;
    }
    
    @Override
    public String toString()
    {
        return Float.toString(value);
    }
    
    @Override
    public int getSize()
    {
        return SIZEOF_FLOAT;
    }
    
    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        value = in.readFloat();
        return in.getPointer() - startOffset;
    }
    
    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        return out.writeFloat(value);
    }
}