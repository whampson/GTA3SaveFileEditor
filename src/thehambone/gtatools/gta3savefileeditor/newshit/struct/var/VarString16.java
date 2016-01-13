package thehambone.gtatools.gta3savefileeditor.newshit.struct.var;

import java.nio.charset.Charset;

/**
 * A {@code VarString8} is a {@code VarString} with 16-bit characters.
 * <p>
 * Characters are encoded using the UTF-16LE encoding.
 * <p>
 * Created on Sep 16, 2015.
 * 
 * @author thehambone
 */
public class VarString16 extends VarString
{
    /**
     * Creates a new {@code VarString16} object.
     * 
     * @param maxLength the maximum number of characters this string can hold
     */
    public VarString16(int maxLength)
    {
        super(Charset.forName("UTF-16LE"), 2, maxLength);
    }
    
    @Override
    protected int length(byte[] strBuf)
    {
        int len = strBuf.length;
        for (int i = 0; i < strBuf.length; i += 2) {
            if (strBuf[i] == 0 && strBuf[i + 1] == 0) {
                len = i / 2;
                break;
            }
        }
        return len;
    }
}
