package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.UnspecifiedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;

/**
 * A {@code Record} is a data structure whose data consists individually
 * distinct child data structures, called members.
 * <p>
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public abstract class Record implements DataStructure
{
    protected int size;
    protected int offset;
    
    /**
     * Creates a new {@code Record} with the specified size.
     * 
     * @param size the size in bytes of the {@code Record}
     */
    protected Record(int size)
    {
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be negative: "
                    + size);
        }
        
        this.size = size;
        offset = -1;
    }
    
    /**
     * Gets the members of this record along with their names.
     * <p>
     * This method uses reflection to get the names of the members.
     * 
     * @return a {@code Map} containing the members of this record along with
     *         their names
     */
    public Map<String, DataStructure> getMembersWithNames()
    {
        // Assumes fields and members are listed in the same order.
        Map<String, DataStructure> memberMap = new LinkedHashMap<>();
        
        DataStructure[] members = getMembers();
        Field[] fields = getClass().getFields();
        
        assert (members.length == fields.length);
        
        for (int i = 0; i < fields.length; i++) {
            memberMap.put(fields[i].getName(), members[i]);
        }
        
        return memberMap;
    }
    
    /**
     * Returns the members of this record as an array of {@code DataStructures}.
     * 
     * @return an array of {@code DataStructures} containing the members of this
     *         record
     */
    public abstract DataStructure[] getMembers();
    
    /**
     * Loads this data structure according to the Android platform.
     * 
     * @param buf the buffer from which data will be loaded
     */
    protected abstract void loadAndroid(DataBuffer buf);
    
    /**
     * Loads this data structure according to the iOS platform.
     * 
     * @param buf the buffer from which data will be loaded
     */
    protected abstract void loadIOS(DataBuffer buf);
    
    /**
     * Loads this data structure according to the PC platform.
     * 
     * @param buf the buffer from which data will be loaded
     */
    protected abstract void loadPC(DataBuffer buf);
    
    /**
     * Loads this data structure according to the PS2 platform.
     * 
     * @param buf the buffer from which data will be loaded
     */
    protected abstract void loadPS2(DataBuffer buf);
    
    /**
     * Loads this data structure according to the Xbox platform.
     * 
     * @param buf the buffer from which data will be loaded
     */
    protected abstract void loadXbox(DataBuffer buf);
    
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
    public void load(DataBuffer buf, int offset, SaveFileNew.Platform platform)
    {
        this.offset = offset;
        buf.seek(offset);
        
        if (platform == null) {
            throw new UnspecifiedPlatformException(
                    "a platform must be specified when loading this data "
                    + "structure");
        } else if (!platform.isSupported()) {
            throw new UnsupportedPlatformException(
                    "platform not supported: " + platform);
        }
        
         switch (platform) {
            case ANDROID:
                loadAndroid(buf);
                break;
            case IOS:
                loadIOS(buf);
                break;
            case PC:
                loadPC(buf);
                break;
            case PS2:
                loadPS2(buf);
                break;
            case XBOX:
                loadXbox(buf);
                break;
        }
    }
    
    @Override
    public boolean isAccessible()
    {
        boolean membersLoaded = true;
        for (DataStructure ds : getMembers()) {
            if (!ds.isAccessible()) {
                membersLoaded = false;
                break;
            }
        }
        return membersLoaded && offset > -1;
    }
}
