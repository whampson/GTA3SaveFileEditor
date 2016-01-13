package thehambone.gtatools.gta3savefileeditor.newshit.struct.var;

/**
 * A {@code VarBoolean8} is an 8-bit {@code VarBoolean} data structure, meaning
 * the numerical value of this boolean data structure is 8-bits wide.
 * <p>
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public class VarBoolean8 extends VarBoolean
{
    /**
     * Creates a new {@code VarBoolean8} object.
     */
    public VarBoolean8()
    {
        super(1);
    }
    
    @Override
    public Boolean getValue()
    {
        buf.seek(offset);
        return buf.readBoolean8();
    }
    
    @Override
    public void setValue(Boolean value)
    {
        buf.seek(offset);
        buf.writeBoolean8(value);
    }
}
