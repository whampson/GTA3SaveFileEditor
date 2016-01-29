package thehambone.gtatools.gta3savefileeditor.savefile.var;

import thehambone.gtatools.gta3savefileeditor.util.NumberUtilities;

/**
 * A {@code VarByte} is a data structure representing an 8-bit integer type.
 * <p>
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public class VarByte extends IntegerVariable<Byte>
{
    /**
     * Creates a new {@code VarByte} object.
     */
    public VarByte()
    {
        super(1);
    }
    
    @Override
    public void parseValue(String s, boolean parseUnsigned)
    {
        if (parseUnsigned) {
            setValue(NumberUtilities.parseByteUnsigned(s));
        } else {
            parseValue(s);
        }
    }
    
    @Override
    public long toUnsignedLong()
    {
        return Byte.toUnsignedLong(getValue());
    }
    
    @Override
    public Byte getValue()
    {
        buf.seek(offset);
        return buf.read();
    }
    
    @Override
    public void setValue(Byte value)
    {
        buf.seek(offset);
        buf.write(value);
    }
    
    @Override
    public void parseValue(String value)
    {
        setValue(Byte.parseByte(value));
    }
    
    @Override
    public String toString()
    {
        return String.format(getToStringFormat(), "");
    }
}
