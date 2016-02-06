package thehambone.gtatools.gta3savefileeditor.util;

/**
 * Created on Jan 9, 2016.
 *
 * @author thehambone
 */
public final class StringUtilities
{
    /**
     * Creates a string containing {@code n} repetitions of a character.
     * 
     * @param ch the character to repeat
     * @param n the number if times to repeat the character
     * @return a string containing the character repetition
     */
    public static String repeatChar(char ch, int n)
    {
        String s = "";
        for (int i = 0; i < n; i++) {
            s += ch;
        }
        return s;
    }
}
