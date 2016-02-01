
package thehambone.gtatools.gta3savefileeditor.gxt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * This class is responsible for keeping track of the active GXT table. A GXT
 * table contains all of the strings used in a particular GTA game for a
 * specific language.
 * <p>
 * Created on Jan 17, 2016.
 *
 * @author thehambone
 */
public final class GXT
{
    private static Map<String, String> gxtTable;
    
    /**
     * Gets the loaded GXT table as a {@code Map} of strings. If no table is
     * loaded, {@code null} is returned.
     * 
     * @return a {@code Map} containing the GXT keys and values; {@code null}
     *         if no GXT table is loaded
     */
    public static Map<String, String> getGXTTable()
    {
        return gxtTable;
    }
    
    /**
     * Loads a GXT table from a file.
     * 
     * @param gxtFile the file to load
     * @throws IOException if an IO error occurs while reading the file
     */
    public static void loadGXTTable(File gxtFile) throws IOException
    {
        GXTReader r = new GXTReader(gxtFile);
        gxtTable = r.readFile();
    }
    
    public static void loadGXTTable(InputStream gxtStream) throws IOException
    {
        GXTReader r = new GXTReader(gxtStream);
        gxtTable = r.readFile();
    }
}
