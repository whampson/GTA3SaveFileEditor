package thehambone.gtatools.gta3savefileeditor.newshit.struct.var;

import java.nio.charset.Charset;

/**
 * A {@code VarString} is a data structure representing a fixed-length string.
 * <p>
 * Created on Sep 16, 2015.
 * 
 * @author thehambone
 */
public abstract class VarString extends Variable<String>
{
    protected final Charset charset;
    protected final int charWidth;
    protected final int maxLength;
    
    /**
     * Creates a new {@code VarString} object with the specified character set,
     * character width, and maximum length.
     * 
     * @param charset the character encoding to be used with this string
     * @param charWidth the size (in bytes) of a single character in this string
     * @param maxLength the maximum number of characters the string can hold
     */
    protected VarString(Charset charset, int charWidth, int maxLength)
    {
        super(charWidth * maxLength);
        
        this.charset = charset;
        this.charWidth = charWidth;
        this.maxLength = maxLength;
    }
    
    /**
     * Gets the number of characters found within the string, not including the
     * null-terminator.
     * 
     * @return the current number of characters in the string
     */
    public int getLength()
    {
        byte[] strBuf = new byte[size];
        buf.seek(offset);
        buf.read(strBuf);
        return length(strBuf);
    }
    
    /**
     * Returns the maximum number of characters this string can hold.
     * 
     * @return the maximum length of the string
     */
    public int getMaxLength()
    {
        return maxLength;
    }
    
    /**
     * Takes a {@code String} and shortens it to {@code maxLength - 1} (if it is
     * larger than the maximum length), and appends a null-terminator to the
     * end of the string.
     * 
     * @param str the {@code String} to be trimmed
     * @return the trimmed {@code String}
     */
    protected String trim(String str)
    {
        str = str.replaceAll("\\x00", "");
        if (str.length() > maxLength) {
            str = str.substring(0, maxLength - 1);
        }
        str += '\0';
        return str;
    }
    
    /**
     * Calculates the number of characters found within the string buffer up to,
     * but not including, the null-terminator (if one is present).
     * 
     * @param strBuf the string buffer on which the length will be computed
     * @return the number of characters in the string buffer
     */
    protected abstract int length(byte[] strBuf);
    
    @Override
    public String getValue()
    {
        byte[] strBuf = new byte[size];
        buf.seek(offset);
        buf.read(strBuf);
        return new String(strBuf, 0, length(strBuf) * charWidth, charset);
    }
    
    @Override
    public void setValue(String value)
    {
        buf.seek(offset);
        buf.write(trim(value).getBytes(charset));
    }
    
    @Override
    public void parseValue(String value)
    {
        setValue(value);
    }
    
    @Override
    public String toString()
    {
        return String.format(getToStringFormat(),
                "maxLength = " + maxLength + "; "
                        + "charset = " + charset + "; "
                        + "charWidth = " + charWidth + "; ");
    }
}
