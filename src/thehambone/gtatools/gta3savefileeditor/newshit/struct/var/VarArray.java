package thehambone.gtatools.gta3savefileeditor.newshit.struct.var;

import java.util.Iterator;
import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.DataStructure;
import thehambone.gtatools.gta3savefileeditor.util.Logger;


/**
 * A {@code VarArray} is a data structure consisting of an indexed set of
 * related elements.
 * <p>
 * Each element can be modified, but its type cannot be changed. A
 * {@code VarArray} cannot be resized.
 * <p>
 * Created on Sep 21, 2015.
 * 
 * @author thehambone
 * @param <T> the type of the elements in the array
 */
public class VarArray<T extends DataStructure>
        extends Variable<T> implements Iterable<T>
{
    private final Class<T> elementTypeClass;
    
    /**
     * Creates a new {@code VarArray} object.
     * 
     * @param elementTypeClass the class used to create the elements of the
     *                         array
     * @param capacity the number of elements found in the array
     */
    public VarArray(Class<T> elementTypeClass, int capacity)
    {
        super(0);
        
        this.elementTypeClass = elementTypeClass;
        size = capacity * getSizeOfElement();
    }
    
    /**
     * Gets the size in bytes of a single element in the array. Each element in
     * an array has the same size.
     * 
     * @return the size in bytes of a single element
     */
    // Safe to cast object defined by Class<T> to T.
    @SuppressWarnings("unchecked")
    public final int getSizeOfElement()
    {
        // Create new instance of element class.
        T element = null;
        try {
            element = (T) elementTypeClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex); // Shouldn't ever happen.
        }
        
        return element.getSize();
    }
    
    /**
     * Gets the number of elements in the array.
     * 
     * @return the number of elements in the array
     */
    public int getElementCount()
    {
        return size / getSizeOfElement();
    }
    
    /**
     * Sets the size of the array. Only use this if the array size is not known
     * at compile time and before the array has been loaded
     * <p>
     * <b>WARNING: Do not use this in an attempt to resize the array! Doing so
     * will likely cause a data leak, where the same data is found in two
     * separate data structures.</b>
     * 
     * @param count the desired number of elements in the array
     */
    public void setElementCount(int count)
    {
        size = count * getSizeOfElement();
    }
    
    /**
     * Returns the element in the array at the specified index.
     * 
     * @param index the index of the element to be retrieved
     * @return the element data found at the index as an {@link DataStructure}
     *         object
     * @throws IndexOutOfBoundsException if the specified index is outside the
     *         bounds of the array
     */
    // Safe to cast object defined by Class<T> to T.
    @SuppressWarnings("unchecked")
    public T getElementAt(int index)
    {
        rangeCheck(index);
        
        // Create new instance of element class.
        T element = null;
        try {
            element = (T) elementTypeClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex); // Shouldn't ever happen.
        }
        
        // Load element data at the offset indicated by the index.
        element.load(buf, offset + (index * element.getSize()), platform);
        
        return element;
    }
    
    /*
     * Checks whether the index falls within the bounds of the array.
     * Throws an IndexOutOfBoundsException if the index is out of bounds.
     */
    private void rangeCheck(int index)
    {
        if (index > getElementCount() - 1 || index < 0) {
            throw new IndexOutOfBoundsException(
                    String.format("Index: %d, Size: %d",
                            index, getElementCount() - 1));
        }
    }
    
    @Override
    public boolean hasValue()
    {
        return getValue() != null;
    }
    
    @Override
    public T getValue()
    {
        return getElementAt(0);
    }
    
    @Override
    public void setValue(T value)
    {
        if (elementTypeClass.isAssignableFrom(Variable.class)) {
            ((Variable<T>)getElementAt(0)).setValue(value);
        } else {
            throw new UnsupportedOperationException(
                    "the type of this array is not of type Variable");
        }
    }
    
    @Override
    public void parseValue(String value)
    {
        if (Variable.class.isAssignableFrom(elementTypeClass)) {
            ((Variable)getElementAt(0)).parseValue(value);
        } else {
            throw new UnsupportedOperationException(
                    "the type of this array is not of type Variable");
        }
    }
    
    @Override
    public void load(DataBuffer buf, int offset, SaveFile.Platform platform)
    {
        super.load(buf, offset, platform);
        
//        if (platform == null) {
//            throw new UnspecifiedPlatformException(
//                    "a platform must be specified when loading this data "
//                    + "structure");
//        } else if (!platform.isSupported()) {
//            throw new UnsupportedPlatformException("platform unsupported: "
//                    + platform);
//        }
    }
    
    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            private final int maxIndex = getElementCount();
            
            private int currentIndex = 0;
            
            @Override
            public boolean hasNext()
            {
                return maxIndex > currentIndex;
            }

            @Override
            public T next()
            {
                return getElementAt(currentIndex++);
            }
        };
    }
    
    @Override
    public String toString()
    {
        return String.format(getToStringFormat(),
                "elementCount = " + getElementCount() + "; "
                        + "sizeOfElement = " + getSizeOfElement() + "; ");
    }
}
