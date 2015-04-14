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
public class GTAByte extends GTADataType
{
    private byte value;
    
    public GTAByte()
    {
        value = 0;
    }
    
    public GTAByte(byte value)
    {
        this.value = value;
    }
    
    public GTAByte(String value) {
        this.value = Byte.parseByte(value);
    }
    
    public byte byteValue()
    {
        return value;
    }
    
    @Override
    public String toString()
    {
        return Byte.toString(value);
    }
    
    @Override
    public int getSize()
    {
        return SIZEOF_BYTE;
    }
    
    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        value = in.read();
        return in.getPointer() - startOffset;
    }
    
    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        return out.write(value);
    }
}