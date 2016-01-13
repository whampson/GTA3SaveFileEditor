package thehambone.gtatools.gta3savefileeditor.newshit.struct.var;

import java.nio.charset.Charset;

/**
 * A {@code VarString8} is a {@code VarString} with 8-bit characters.
 * <p>
 * Characters are encoded using the UTF-8 encoding.
 * <p>
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public class VarString8 extends VarString
{
    /**
     * Creates a new {@code VarString8} object.
     * 
     * @param maxLength the maximum number of characters this string can hold
     */
    public VarString8(int maxLength)
    {
        super(Charset.forName("UTF-8"), 1, maxLength);
    }
    
    @Override
    protected int length(byte[] strBuf)
    {
        int len = strBuf.length;
        for (int i = 0; i < strBuf.length; i++) {
            if (strBuf[i] == 0) {
                len = i;
                break;
            }
        }
        return len;
    }
}
