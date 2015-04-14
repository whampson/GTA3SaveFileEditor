package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;

/**
 * A DataStructure is represents any piece of data in a GTA III save file, be it
 * a single value, a fixed-size array, or a chunk of data of variable length.
 * 
 * @author thehambone
 * @version 0.1
 * @since February 12, 2015
 */
public interface DataStructure
{
    /**
     * Gets the size in bytes of this piece of data. If the data is an array,
     * the total number of bytes contained within the array is returned, which
     * may or may not be equal to the number of elements in the array.
     * 
     * @return the number of bytes that makes up this piece of data
     */
    public int getSize();
    
    /**
     * Loads this piece of data from the specified input stream. The number of
     * bytes loaded and how the loaded data is handled varies by implementation.
     * How the data is handled may also vary across platforms.
     * 
     * @param in the current input stream
     * @return the number of bytes read
     * @throws IOException if an I/O error occurs
     */
    public int load(SaveFileInputStream in) throws IOException;
    
    /**
     * Saves this piece of data to the specified output stream. The number of
     * bytes written varies by implementation. How the data is written may also
     * vary across platforms.
     * 
     * @param out the current output stream
     * @return the number of bytes written
     * @throws IOException if an I/O error occurs
     */
    public int save(SaveFileOutputStream out) throws IOException;
}