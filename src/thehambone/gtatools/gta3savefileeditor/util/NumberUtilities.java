package thehambone.gtatools.gta3savefileeditor.util;

/**
 * A collection of methods for parsing numerical numerical input.
 * <p>
 * Created on Mar 30, 2015.
 * 
 * @author thehambone
 */
public class NumberUtilities
{
    /**
     * The maximum value of an unsigned byte. Equal to {@code 2^8}.
     */
    public static final long UNSIGNED_BYTE_MAX_VALUE = (1 << 8) - 1;
    
    /**
     * The maximum value of an unsigned short. Equal to {@code 2^16}.
     */
    public static final long UNSIGNED_SHORT_MAX_VALUE = (1 << 16) - 1;
    
    /**
     * The maximum value of an unsigned int. Equal to {@code 2^32}.
     */
    public static final long UNSIGNED_INT_MAX_VALUE = (1L << 32) - 1;
    
    /**
     * Checks whether the contents of a string represent an integer. An integer
     * is defined as any whole number, positive or negative, including zero. A
     * number written in exponential notation will be treated as an integer if
     * it resolves to a whole number. For instance {@code 1e5} will be treated
     * as an integer, while {@code 1.2e5} or {@code 1e-5} will not.
     * <p>
     * This method does <u>not</u> check whether the string can be cast to an
     * {@code int} type.
     * 
     * @param s the string to check
     * @return {@code true} if the contents of the string represent an integer,
     *         {@code false} if not
     */
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
    
    /**
     * Checks whether the contents of a string represent either a non-whole
     * rational number or an irrational number. "Decimal" in this context does
     * not refer to the base-10 numbering system, rather it refers to the
     * presence of (.) character that separates the fractional part of a number
     * from the whole part.
     * 
     * @param s the string to check
     * @return {@code true} if the contents of the string represent a fractional
     *         or irrational number, {@code false} if not
     */
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
    
    /**
     * Checks whether the contents of a string are numeric.
     * 
     * @param s the string to check
     * @return {@code true} if the string represents either an integer or
     *         decimal number, {@code false} otherwise
     */
    public static boolean isNumeric(String s)
    {
        return isInteger(s) || isDecimal(s);
    }
    
    /**
     * Converts the string representing an unsigned integer to a signed
     * {@code byte} type.
     * 
     * @param s the string to parse
     * @return a {@code byte} whose value matches two's compliment of the value
     *         in the string
     * @throws NumberFormatException if the string contents do not represent a
     *         number or if the number is out of range for a {@code byte}
     */
    public static byte parseByteUnsigned(String s)
    {
        short sh = Short.parseShort(s);
        if (sh < 0 || sh > UNSIGNED_BYTE_MAX_VALUE) {
            throw new NumberFormatException("For input string: " + sh);
        }
        return (byte)sh;
    }
    
    /**
     * Converts the string representing an unsigned integer to a signed
     * {@code int} type.
     * 
     * @param s the string to parse
     * @return an {@code int} whose value matches two's compliment of the value
     *         in the string
     * @throws NumberFormatException if the string contents do not represent a
     *         number or if the number is out of range for an {@code int}
     */
    public static int parseIntUnsigned(String s)
    {
        long l = Long.parseLong(s);
        if (l < 0 || l > UNSIGNED_INT_MAX_VALUE) {
            throw new NumberFormatException("For input string: " + l);
        }
        return (int)l;
    }
    
    /**
     * Converts the string representing an unsigned integer to a signed
     * {@code short} type.
     * 
     * @param s the string to parse
     * @return a {@code short} whose value matches two's compliment of the value
     *         in the string
     * @throws NumberFormatException if the string contents do not represent a
     *         number or if the number is out of range for a {@code short}
     */
    public static short parseShortUnsigned(String s)
    {
        int i = Integer.parseInt(s);
        if (i < 0 || i > UNSIGNED_SHORT_MAX_VALUE) {
            throw new NumberFormatException("For input string: " + i);
        }
        return (short)i;
    }
}
