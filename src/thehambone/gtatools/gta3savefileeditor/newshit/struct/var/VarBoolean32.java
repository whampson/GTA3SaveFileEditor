package thehambone.gtatools.gta3savefileeditor.newshit.struct.var;

/**
 * A {@code VarBoolean32} is a 32-bit {@code VarBoolean} data structure, meaning
 * the numerical value of this boolean data structure is 32-bits wide.
 * <p>
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public class VarBoolean32 extends VarBoolean
{
    /**
     * Creates a new {@code VarBoolean32} object.
     */
    public VarBoolean32()
    {
        super(4);
    }
    
    @Override
    public Boolean getValue()
    {
        buf.seek(offset);
        return buf.readBoolean32();
    }
    
    @Override
    public void setValue(Boolean value)
    {
        buf.seek(offset);
        buf.writeBoolean32(value);
    }
}
