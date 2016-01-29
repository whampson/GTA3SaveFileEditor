package thehambone.gtatools.gta3savefileeditor.savefile.var;

import thehambone.gtatools.gta3savefileeditor.util.NumberUtilities;

/**
 * A {@code VarInt} is a data structure representing a 16-bit integer type.
 * <p>
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public class VarShort extends IntegerVariable<Short>
{
    /**
     * Creates a new {@code VarShort} object.
     */
    public VarShort()
    {
        super(2);
    }
    
    @Override
    public void parseValue(String s, boolean parseUnsigned)
    {
        if (parseUnsigned) {
            setValue(NumberUtilities.parseShortUnsigned(s));
        } else {
            parseValue(s);
        }
    }
    
    @Override
    public long toUnsignedLong()
    {
        return Short.toUnsignedLong(getValue());
    }
    
    @Override
    public Short getValue()
    {
        buf.seek(offset);
        return buf.readShort();
    }
    
    @Override
    public void setValue(Short value)
    {
        buf.seek(offset);
        buf.writeShort(value);
    }
    
    @Override
    public void parseValue(String value)
    {
        setValue(Short.parseShort(value));
    }
    
    @Override
    public String toString()
    {
        return String.format(getToStringFormat(), "");
    }
}
