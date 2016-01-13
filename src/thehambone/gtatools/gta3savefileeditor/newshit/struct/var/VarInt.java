package thehambone.gtatools.gta3savefileeditor.newshit.struct.var;

import thehambone.gtatools.gta3savefileeditor.util.NumberUtilities;

/**
 * A {@code VarInt} is a data structure representing a 32-bit integer type.
 * <p>
 * Created on Sep 16, 2015.
 * 
 * @author thehambone
 */
public class VarInt extends IntegerVariable<Integer>
{
    /**
     * Creates a new {@code VarInt} object.
     */
    public VarInt()
    {
        super(4);
    }
    
    
    @Override
    public void parseValue(String s, boolean parseUnsigned)
    {
        if (parseUnsigned) {
            setValue(NumberUtilities.parseIntUnsigned(s));
        } else {
            parseValue(s);
        }
    }
    
    @Override
    public long toUnsignedLong()
    {
        return Integer.toUnsignedLong(getValue());
    }
    
    @Override
    public Integer getValue()
    {
        buf.seek(offset);
        return buf.readInt();
    }
    
    @Override
    public void setValue(Integer value)
    {
        buf.seek(offset);
        buf.writeInt(value);
    }
    
    @Override
    public void parseValue(String value)
    {
        setValue(Integer.parseInt(value));
    }
    
    @Override
    public String toString()
    {
        return String.format(getToStringFormat(), "");
    }
}
