package thehambone.gtatools.gta3savefileeditor.savefile.var;

/**
 * A {@code VarFloat} is a data structure representing a 32-bit floating-point
 * number.
 * <p>
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public class VarFloat extends Variable<Float>
{
    /**
     * Creates a new {@code VarFloat} object.
     */
    public VarFloat()
    {
        super(4);
    }
    
    @Override
    public Float getValue()
    {
        buf.seek(offset);
        return buf.readFloat();
    }
    
    @Override
    public void setValue(Float value)
    {
        buf.seek(offset);
        buf.writeFloat(value);
    }
    
    @Override
    public void parseValue(String value)
    {
        setValue(Float.parseFloat(value));
    }
    
    @Override
    public String toString()
    {
        return String.format(getToStringFormat(), "");
    }
}
