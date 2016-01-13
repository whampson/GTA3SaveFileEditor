package thehambone.gtatools.gta3savefileeditor.savefile.struct;

/**
 * A GTADataType object represents a particular kind of data structure. A
 * GTADataType varies from a
 * {@link thehambone.gtatools.gta3savefileeditor.savefile.struct.Block} or a
 * {@link thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject} 
 * in that it is both fixed-sized and can contain only one value at a time.
 * 
 * GTADataTypes are built upon existing types found in the Java Programming
 * Language, but are specialized to allow their values to be read from or
 * written to a save file buffer, as well as mapped to a
 * {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable}. In
 * this sense, GTADataTypes are essentially wrappers for existing Java types.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 21, 2015
 * @deprecated 
 */
public abstract class GTADataType implements DataStructure
{
    public static final int SIZEOF_BOOLEAN8   = 1;
    public static final int SIZEOF_BOOLEAN16  = 2;
    public static final int SIZEOF_BOOLEAN32  = 4;
    public static final int SIZEOF_BYTE       = 1;
    public static final int SIZEOF_FLOAT      = 4;
    public static final int SIZEOF_INT        = 4;
    public static final int SIZEOF_SHORT      = 2;
    
    protected String variableMappedToName;
    protected boolean isMappedToVariable;
    
    public boolean isMappedToVariable()
    {
        return isMappedToVariable;
    }
    
    public String getVariableMappedToName()
    {
        return variableMappedToName;
    }
    
    public void setVariableMappedToName(String variableMappedToName)
    {
        this.variableMappedToName = variableMappedToName;
        this.isMappedToVariable = true;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends GTADataType> T[] makeArray(int size)
    {
        GTADataType[] array = new GTADataType[size];
        for (int i = 0; i < array.length; i++) {
            try {
                array[i] = this.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        return (T[])array;
    }
}