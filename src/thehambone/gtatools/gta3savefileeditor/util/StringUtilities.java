
package thehambone.gtatools.gta3savefileeditor.util;

/**
 * Created on Jan 9, 2016.
 *
 * @author thehambone
 */
public final class StringUtilities
{
    public static String repeatChar(char ch, int n)
    {
        String s = "";
        for (int i = 0; i < n; i++) {
            s += ch;
        }
        return s;
    }
}
