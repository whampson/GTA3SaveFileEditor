package thehambone.gtatools.gta3savefileeditor;

/**
 * A collection of methods for parsing numerical numerical input.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 30, 2015
 */
public class NumberUtils
{
    public static boolean isNumber(String s, int radix)
    {
        s = trimNegative(s).trim();
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (Character.digit(s.charAt(i), radix) < 0) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isInteger(String s)
    {
        if (!isNumber(s, 10)) {
            return false;
        }
        long n = Long.parseLong(s);
        return !(n > Integer.MAX_VALUE || n < Integer.MIN_VALUE);
    }
    
    public static boolean isDecimal(String s)
    {
        if (isInteger(s)) {
            return true;
        }
//        if (s.equals("NaN") || s.equals("Infinity") || s.equals("-Infinity")) {
//            return true;
//        }
        if (!s.contains(".")) {
            return false;
        }
        int dotCount = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                dotCount++;
            }
            if (dotCount > 1) {
                return false;
            }
        }
        String[] splitDecimal = s.split("\\.");
        for (String decimalPiece : splitDecimal) {
            if (!isInteger(decimalPiece)) {
                return false;
            }
        }
        return true;
    }
    
    private static String trimNegative(String s)
    {
        if (s.startsWith("-")) {
            s = s.substring(1);
        }
        return s;
    }
}