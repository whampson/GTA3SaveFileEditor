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
public class GTAStringUTF8 extends GTADataType
{
    private final int bufferSize;
    
    private String value;
    
    public GTAStringUTF8(int bufferSize)
    {
        value = "";
        this.bufferSize = bufferSize;
    }
    
    public GTAStringUTF8(String value, int bufferSize)
    {
        this.value = value;
        this.bufferSize = bufferSize;
    }
    
    public String stringValue()
    {
        return value;
    }
    
    public int getBufferSize()
    {
        return bufferSize;
    }
    
    @Override
    public int getSize()
    {
        return bufferSize;
    }
    
    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        value = in.readStringUTF8(bufferSize);
        return in.getPointer() - startOffset;
    }
    
    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        return out.writeStringUTF8(value, bufferSize);
    }
}