package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAInteger;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;

/**
 * A collection of like
 * {@link thehambone.gtatools.gta3savefileeditor.savefile.struct.GTADataType}s.
 * 
 * @author thehambone
 * @version 0.1
 * @param <T> a subclass of {@link thehambone.gtatools.gta3savefileeditor.savefile.struct.GTADataType}
 * @since 0.1, February 23, 2015
 * @deprecated 
 */
public class Array<T extends GTADataType> implements DataStructure
{
    private final List<T> array;
    private final T type;
    private final boolean isSizeStatic;
    private final boolean isElementCountDefined;
    private final boolean isStructSizeDefined;
    
    protected String variableMappedToName;
    protected boolean isMappedToVariable;
    
    private Variable<GTAInteger> elementCountVar;
    private Variable<GTAInteger> arraySizeBytesVar;
    private int elementSizeBytes;
    
    public Array(List<T> array, T type)
    {
        this.array = array;
        this.type = type;
        isSizeStatic = true;
        isElementCountDefined = false;
        isStructSizeDefined = false;
    }
    
    public Array(List<T> array, T type, Variable<GTAInteger> elementCount)
    {
        this.array = array;
        this.type = type;
        this.elementCountVar = elementCount;
        isSizeStatic = false;
        isElementCountDefined = true;
        isStructSizeDefined = false;
    }
    
    public Array(List<T> array, T defaultValue, Variable<GTAInteger> arraySizeBytes, int elementSizeBytes)
    {
        this.array = array;
        this.type = defaultValue;
        this.arraySizeBytesVar = arraySizeBytes;
        this.elementSizeBytes = elementSizeBytes;
        isSizeStatic = false;
        isElementCountDefined = false;
        isStructSizeDefined = true;
    }
    
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
        isMappedToVariable = true;
    }

    @Override
    public int getSize()
    {
        int size = 0;
        for (GTADataType element : array) {
            size += element.getSize();
        }
        return size;
    }

    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        allocateArray();
        for (GTADataType element : array) {
            element.load(in);
        }
        return in.getPointer() - startOffset;
    }

    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        for (GTADataType element : array) {
            element.save(out);
        }
        return out.getPointer() - startOffset;
    }
    
    private void allocateArray()
    {
        int count = 0;
        if (!isSizeStatic) {
            if (isElementCountDefined) {
                count = elementCountVar.getValue().intValue();
            } else if (isStructSizeDefined) {
                count = arraySizeBytesVar.getValue().intValue() / elementSizeBytes;
            }
        } else {
            count = array.size();
        }
        T[] allocatedArray = type.makeArray(count);
        array.clear();
        array.addAll(Arrays.asList(allocatedArray));
    }
}