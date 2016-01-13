package thehambone.gtatools.gta3savefileeditor.newshit.struct.var;

/**
 * A {@code VarBoolean16} is a 16-bit {@code VarBoolean} data structure, meaning
 * the numerical value of this boolean data structure is 16-bits wide.
 * <p>
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public class VarBoolean16 extends VarBoolean
{
    /**
     * Creates a new {@code VarBoolean16} object.
     */
    public VarBoolean16()
    {
        super(2);
    }
    
    @Override
    public Boolean getValue()
    {
        buf.seek(offset);
        return buf.readBoolean16();
    }
    
    @Override
    public void setValue(Boolean value)
    {
        buf.seek(offset);
        buf.writeBoolean16(value);
    }
}
