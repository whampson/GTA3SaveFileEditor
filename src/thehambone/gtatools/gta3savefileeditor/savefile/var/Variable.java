package thehambone.gtatools.gta3savefileeditor.savefile.var;

import thehambone.gtatools.gta3savefileeditor.util.Checksum;
import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.DataStructure;
import thehambone.gtatools.gta3savefileeditor.savefile.UnspecifiedPlatformException;
import thehambone.gtatools.gta3savefileeditor.savefile.UnsupportedPlatformException;


/**
 * A {@code Variable} is a data structure whose data represents a discrete
 * selection of data that can be manipulated freely according to its type.
 * <p>
 * Created on Sep 18, 2015.
 * 
 * @author thehambone
 * @param <T> the type of the variable data 
 */
public abstract class Variable<T> implements DataStructure
{
    protected int size;
    protected int offset;
    protected boolean isLoaded;
    protected DataBuffer buf;
    protected SaveFile.Platform platform;
    
    /**
     * Creates a new {@code Variable} with the specified size.
     * 
     * @param size the size in bytes of the variable data
     */
    protected Variable(int size)
    {
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be negative: "
                    + size);
        }
        
        this.size = size;
        offset = -1;
        isLoaded = false;
        buf = null;
    }
    
    /**
     * Loads data into the data structure.
     * 
     * @param buf the {@code DataBuffer} object containing the data to be loaded
     * @param offset the location in the buffer of this data structure
     * @throws UnspecifiedPlatformException if a platform was not specified when
     *                                      the data structure being loaded
     *                                      requires it
     * @throws UnsupportedPlatformException if the specified platform is not
     *                                      supported
     * @see DataStructure#load(DataBuffer, int, SaveFile.Platform) 
     */
    public void load(DataBuffer buf, int offset)
    {
        load(buf, offset, null);
    }
    
    /**
     * Checks whether this variable has a distinct value (such as the number 4).
     * Not all variable types have distinct values.
     * 
     * @return {@code true} if the variable has a distinct value, {@code false}
     *         otherwise
     */
    public boolean hasValue()
    {
        return isAccessible();
    }
    
    /**
     * Returns a format string portraying the current state of the variable.
     * This string is meant to be modified in the toString() methods of child
     * classes.
     * 
     * @return the toString() format string for objects of this type
     */
    protected String getToStringFormat()
    {
        return getClass().getSimpleName() + "@"
                + Integer.toHexString(hashCode()) + ": { "
                + "offset = 0x" + Integer.toHexString(offset) + "; "
                + "size = " + size + "; "
                + "%s"
                + "value = " + getValue() + "; "
                + "isAccessible = " + isAccessible() + "; "
                + "checksum = 0x" + Integer.toHexString(checksum()) + "; "
                + "}";
    }
    
    /*
     * Calculates a CRC32 checksum of the data.
     */
    private int checksum()
    {
        byte[] data = new byte[size];
        buf.mark();
        buf.seek(offset);
        buf.read(data);
        buf.reset();
        return Checksum.crc32(data);
    }
    
    /**
     * Returns the value of this variable, if it has one.
     * <p>
     * Use {@link #hasValue()} to check whether this variable has a distinct
     * value before calling this method.
     * 
     * @return the value of the variable according to its type
     */
    public abstract T getValue();
    
    /**
     * Sets the value of this variable. When a value is set, data in the
     * underlying buffer is changed immediately.
     * <p>
     * Use {@link #hasValue()} to check whether this variable has a distinct
     * value before calling this method.
     * 
     * @param value the new value of the variable
     */
    public abstract void setValue(T value);
    
    /**
     * Attempts to set the value of this variable based on the value of a
     * String.
     * 
     * @param value the string value representing the new value of this variable
     */
    public abstract void parseValue(String value);
    
    @Override
    public int getOffset()
    {
        return offset;
    }

    @Override
    public int getSize()
    {
        return size;
    }
    
    @Override
    public void load(DataBuffer buf, int offset, SaveFile.Platform platform)
    {
        this.buf = buf;
        this.offset = offset;
        this.platform = platform;
        isLoaded = true;
    }

    @Override
    public boolean isAccessible()
    {
        return buf != null
                && offset > -1
                && isLoaded;
    }
}
