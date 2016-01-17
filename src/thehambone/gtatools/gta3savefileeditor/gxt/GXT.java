
package thehambone.gtatools.gta3savefileeditor.gxt;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 17, 2016.
 *
 * @author thehambone
 */
public final class GXT
{
    private static Map<String, String> gxtTable;
    
    public static Map<String, String> getGXTTable()
    {
        return gxtTable;
    }
    
    public static void loadGXTTable(File gxtFile) throws IOException
    {
        GXTReader r = new GXTReader(gxtFile);
        gxtTable = r.readFile();
        
        Logger.info("Loaded GXT Table from file: " + gxtFile);
    }
}
