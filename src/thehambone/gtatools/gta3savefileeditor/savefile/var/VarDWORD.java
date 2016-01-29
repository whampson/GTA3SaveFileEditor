
package thehambone.gtatools.gta3savefileeditor.savefile.var;

import thehambone.gtatools.gta3savefileeditor.util.NumberUtilities;

/**
 * Created on Jan 12, 2016.
 *
 * @author thehambone
 */
public class VarDWORD extends Variable<Integer>
{
    public VarDWORD()
    {
        super(4);
    }
    
    public boolean toBoolean()
    {
        return getValue() == 1;
    }
    
    public int toInt()
    {
        return getValue();
    }
    
    public float toFloat()
    {
        return Float.intBitsToFloat(getValue());
    }
    
    public long toUnsignedLong()
    {
        return Integer.toUnsignedLong(getValue());
    }
    
    public void parseBoolean(boolean bool)
    {
        setValue(bool ? 1 : 0);
    }
    
    public void parseFloat(String s)
    {
        setValue(Float.floatToRawIntBits(Float.parseFloat(s)));
    }
    
    public void parseInt(String s)
    {
        parseInt(s, false);
    }
    
    public void parseInt(String s, boolean parseUnsigned)
    {
        if (parseUnsigned) {
            setValue(NumberUtilities.parseIntUnsigned(s));
        } else {
            parseValue(s);
        }
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
        return String.format(getToStringFormat(),
                "intValue = " + toInt() + "; "
                        + "floatValue = " + toFloat() + "; ");
    }
}
