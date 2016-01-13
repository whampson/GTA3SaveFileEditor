package thehambone.gtatools.gta3savefileeditor.newshit.struct.var;

/**
 * A {@code VarBoolean} is a data structure whose numerical value represents
 * either the one of the boolean logic values {@code true} and {@code false}.
 * <p>A numerical value of 0 represents {@code false}, while any other numerical
 * value (typically 1) represents {@code true}.
 * <p>
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public abstract class VarBoolean extends Variable<Boolean>
{
    /**
     * Creates a new {@code VarBoolean} data structure with the specified size.
     * 
     * @param size the size in bytes of the boolean data
     */
    protected VarBoolean(int size)
    {
        super(size); // and don't forget the fries
    }
    
    @Override
    public void parseValue(String value)
    {
        setValue(Boolean.parseBoolean(value));
    }
    
    @Override
    public String toString()
    {
        return String.format(getToStringFormat(), "");
    }
}
