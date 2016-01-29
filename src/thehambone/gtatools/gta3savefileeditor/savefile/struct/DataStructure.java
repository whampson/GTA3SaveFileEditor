package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.UnspecifiedPlatformException;
import thehambone.gtatools.gta3savefileeditor.savefile.UnsupportedPlatformException;


/**
 * The {@code DataStructure} interface serves as a template for a data
 * structure, which is a collection of data (usually bytes) organized in a
 * specialized format.
 * <p>
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public interface DataStructure
{
    /**
     * Gets the location of the first byte in this data structure.
     * 
     * @return the position in the source buffer of this data structure
     */
    public int getOffset();
    
    /**
     * Returns the size in bytes of this data structure.
     * 
     * @return 
     */
    public int getSize();
    
    /**
     * Loads data into the data structure. Certain data structures differ
     * between gaming platforms, so it is necessary to provide the platform that
     * this data structure should be loaded according to.
     * <p>
     * The loading process first assigns a source buffer to the
     * {@code DataStructure} object, then assigns the offset in the buffer where
     * the data structure is located, and finally loads its members according to
     * the platform provided, if any are present.
     * <p>
     * A data structure must be loaded before it can be used.
     * 
     * @param buf the {@code DataBuffer} object containing the data to be loaded
     * @param offset the location in the buffer of this data structure
     * @param platform the gaming platform which data should be loaded according
     *                 to
     * @throws UnspecifiedPlatformException if a platform was not specified when
     *                                      the data structure being loaded
     *                                      requires it
     * @throws UnsupportedPlatformException if the specified platform is not
     *                                      supported
     */
    public void load(DataBuffer buf, int offset, SaveFile.Platform platform);
    
    /**
     * Checks whether data has been loaded into this data structure.
     * 
     * @return {@code true} if data has been loaded, {@code false} otherwise
     */
    public boolean isAccessible();
}
