package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;
import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;

/**
 * A GlobalVariable is a number whose value can be referenced from anywhere
 * inside the game's main script (main.scm). Global variables are stored in an
 * array at the start of the "Scripts" sub-block of the "SimpleVars" block
 * (block 0).
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 22, 2015
 * @deprecated 
 */
public class GlobalVariable extends GTAObject
{
    public static final int SIZE = 4;
    
    private int value;
    
    public GlobalVariable()
    {
        this.value = 0;
    }
    
    public GlobalVariable(int value)
    {
        this.value = value;
    }
         
    public int getValue()
    {
        return value;
    }
    
    public void setValue(int value)
    {
        this.value = value;
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
        value = in.readInt();
        return in.getPointer() - startOffset;
    }

    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.writeInt(value);
        return out.getPointer() - startOffset;
    }
}