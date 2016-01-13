package thehambone.gtatools.gta3savefileeditor.util;

/**
 * A collection of methods for parsing numerical numerical input.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 30, 2015
 */
public class NumberUtilities
{
    public static final long UNSIGNED_BYTE_MAX_VALUE = (1 << 8) - 1;
    public static final long UNSIGNED_SHORT_MAX_VALUE = (1 << 16) - 1;
    public static final long UNSIGNED_INT_MAX_VALUE = (1L << 32) - 1;
    
    public static boolean isInteger(String s)
    {
        boolean isInteger = true;
        boolean foundExponentChar = false;
        
        if (s.isEmpty()) {
            return false;
        }
        
        if (s.charAt(0) == '-' || s.charAt(0) == '+') {
            s = s.substring(1);
        }
        
        for (int i = 0; i < s.length(); i++) {
            if ((s.charAt(i) == 'e' || s.charAt(i) == 'E')
                    && i != s.length() - 1) {
                if (foundExponentChar) {
                    isInteger = false;
                    break;
                }
                foundExponentChar = true;
                continue;
            }
            if (!Character.isDigit(s.charAt(i))) {
                isInteger = false;
                break;
            }
        }
        
        return isInteger && !s.isEmpty();
    }
    
    public static boolean isDecimal(String s)
    {
        boolean isDecimal = true;
        boolean foundDecimalPoint = false;
        boolean foundExponentChar = false;
        
        if (s.isEmpty()) {
            return false;
        }
        
        if (s.charAt(0) == '-' || s.charAt(0) == '+') {
            s = s.substring(1);
        }
        
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.' && i != s.length() - 1) {
                if (foundDecimalPoint) {
                    isDecimal = false;
                    break;
                }
                foundDecimalPoint = true;
                continue;
            } else if ((s.charAt(i) == 'e' || s.charAt(i) == 'E')
                    && i != s.length() - 1) {
                if (foundExponentChar) {
                    isDecimal = false;
                    break;
                }
                foundExponentChar = true;
                if (s.charAt(i + 1) == '-' && (i + 1) != s.length() - 1) {
                    i++;
                }
                continue;
            }
            if (!Character.isDigit(s.charAt(i))) {
                isDecimal = false;
                break;
            }
        }
        
        return isDecimal && foundDecimalPoint && !s.isEmpty();
    }
    
    public static boolean isNumeric(String s)
    {
        return isInteger(s) || isDecimal(s);
    }
    
    /**
     * Parses the string argument as a signed {@code byte}.
     * @param s
     * @return 
     */
    public static byte parseByteUnsigned(String s)
    {
        short sh = Short.parseShort(s);
        if (sh < 0 || sh > UNSIGNED_BYTE_MAX_VALUE) {
            throw new NumberFormatException("For input string: " + sh);
        }
        return (byte)sh;
    }
    
    public static int parseIntUnsigned(String s)
    {
        long l = Long.parseLong(s);
        if (l < 0 || l > UNSIGNED_INT_MAX_VALUE) {
            throw new NumberFormatException("For input string: " + l);
        }
        return (int)l;
    }
    
    public static short parseShortUnsigned(String s)
    {
        int i = Integer.parseInt(s);
        if (i < 0 || i > UNSIGNED_SHORT_MAX_VALUE) {
            throw new NumberFormatException("For input string: " + i);
        }
        return (short)i;
    }
}
