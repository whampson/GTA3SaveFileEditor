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
 */
public class GTABoolean8 extends GTADataType
{
    private boolean value;
    
    public GTABoolean8()
    {
        value = false;
    }
    
    public GTABoolean8(boolean value)
    {
        this.value = value;
    }
    
    public GTABoolean8(String value) {
        this.value = Boolean.parseBoolean(value);
    }
    
    public boolean booleanValue()
    {
        return value;
    }
    
    @Override
    public String toString()
    {
        return Boolean.toString(value);
    }
    
    @Override
    public int getSize()
    {
        return SIZEOF_BOOLEAN8;
    }
    
    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        value = in.readBoolean8();
        return in.getPointer() - startOffset;
    }
    
    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        return out.writeBoolean8(value);
    }
}