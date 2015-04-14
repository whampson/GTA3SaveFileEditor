package thehambone.gtatools.gta3savefileeditor.savefile.variable;

import java.util.ArrayList;
import java.util.List;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile.Platform;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Align;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Array;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.DataStructure;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.GTADataType;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTABoolean16;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTABoolean32;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTABoolean8;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAByte;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAFloat;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAInteger;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAShort;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAStringUTF16;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAStringUTF8;

/**
 * A changeable value that is stored in a save file.
 * 
 * @author thehambone
 * @version 0.1
 * @param <T> a subclass of {@link thehambone.gtatools.gta3savefileeditor.savefile.struct.GTADataType}
 * @since 0.1, February 23, 2015
 */
public class Variable<T extends GTADataType>
{
    private final String variableName;
    private final boolean definedAndroid, definediOS, definedPC, definedPS2, definedXbox;
    private final List<T> values = new ArrayList<>();
    private final T defaultValue;
    private final boolean isArray;
    
    private boolean valueChanged;
    
    public Variable(String variableIdentifier,
            boolean definedAndroid,
            boolean definediOS,
            boolean definedPC,
            boolean definedPS2,
            boolean definedXbox,
            T defaultValue,
            int initialSize)
    {
        this.variableName = variableIdentifier;
        this.definedAndroid = definedAndroid;
        this.definediOS = definediOS;
        this.definedPC = definedPC;
        this.definedPS2 = definedPS2;
        this.definedXbox = definedXbox;
        this.defaultValue = defaultValue;
        isArray = true;
        populate(initialSize);
    }
    
    public Variable(String variableIdentifier,
            boolean definedAndroid,
            boolean definediOS,
            boolean definedPC,
            boolean definedPS2,
            boolean definedXbox,
            T defaultValue)
    {
        this.variableName = variableIdentifier;
        this.definedAndroid = definedAndroid;
        this.definediOS = definediOS;
        this.definedPC = definedPC;
        this.definedPS2 = definedPS2;
        this.definedXbox = definedXbox;
        this.defaultValue = defaultValue;
        isArray = false;
        populate(1);
    }
    
    public int getSizeInBytes()
    {
        int size = 0;
        for (T value : values) {
            size += value.getSize();
        }
        return size;
    }
    
    public int getElementCount()
    {
        return values.size();
    }
    
    public String getVariableName()
    {
        return variableName;
    }
    
    public boolean hasValueChanged()
    {
        return valueChanged;
    }
    
    public void resetValueChanged()
    {
        valueChanged = false;
    }
    
    public T getDefaultValue()
    {
        return defaultValue;
    }
    
    public T getValue()
    {
        return getValueAt(0);
    }
    
    public void setValue(String value)
    {
        setValueAt(0, value);
    }
    
    @SuppressWarnings("unchecked")
    public void setValueAt(int index, String value) {
        if (defaultValue instanceof GTABoolean8) {
            setValueAt(index, (T)new GTABoolean8(value));
        } else if (defaultValue instanceof GTABoolean16) {
            setValueAt(index, (T)new GTABoolean16(value));
        } else if (defaultValue instanceof GTABoolean32) {
            setValueAt(index, (T)new GTABoolean32(value));
        } else if (defaultValue instanceof GTAByte) {
            setValueAt(index, (T)new GTAByte(value));
        } else if (defaultValue instanceof GTAFloat) {
            setValueAt(index, (T)new GTAFloat(value));
        } else if (defaultValue instanceof GTAInteger) {
            setValueAt(index, (T)new GTAInteger(value));
        } else if (defaultValue instanceof GTAShort) {
            setValueAt(index, (T)new GTAShort(value));
        } else if (defaultValue instanceof GTAStringUTF8) {
            setValueAt(index, (T)new GTAStringUTF8(value, ((GTAStringUTF8)defaultValue).getBufferSize()));
        } else if (defaultValue instanceof GTAStringUTF16) {
            setValueAt(index, (T)new GTAStringUTF16(value, ((GTAStringUTF16)defaultValue).getBufferSize()));
        }
    }
    
    public void setValue(T value)
    {
        setValueAt(0, value);
    }
    
    public T getValueAt(int index)
    {
        return values.get(index);
    }
    
    public void setValueAt(int index, T value)
    {
        values.set(index, value);
        valueChanged = true;
    }
    
    public void add(T value)
    {
        values.add(value);
        valueChanged = true;
    }
    
    public void remove(int index)
    {
        values.remove(index);
        if (values.isEmpty()) {
            values.add(defaultValue);
        }
        valueChanged = true;
    }
    
    public boolean isArray()
    {
        return isArray;
    }
    
    public boolean isDefinedForPlatform(Platform platform)
    {
        switch (platform) {
            case ANDROID:
                return definedAndroid;
            case iOS:
                return definediOS;
            case PC:
                return definedPC;
            case PS2:
                return definedPS2;
            case XBOX:
                return definedXbox;
            default:
                return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    public DataStructure asDataStructure(Platform platform)
    {
        if (isDefinedForPlatform(platform)) {
            if (isArray) {
                Array arrayStruct = new Array(values, defaultValue);
                arrayStruct.setVariableMappedToName(variableName);
                return arrayStruct;
            } else {
                return asSingleValueDataStructure();
            }
        } else {
            return new Align(platform, 0, 0, 0, 0, 0);
        }
    }
    
    @SuppressWarnings("unchecked")
    public DataStructure asDataStructure(Platform platform, Variable<GTAInteger> elementCountVar)
    {
        if (isDefinedForPlatform(platform)) {
            if (isArray) {
                Array arrayStruct = new Array(values, defaultValue, elementCountVar);
                arrayStruct.setVariableMappedToName(variableName);
                return arrayStruct;
            } else {
                return asSingleValueDataStructure();
            }
        } else {
            return new Align(platform, 0, 0, 0, 0, 0);
        }
    }
    
    @SuppressWarnings("unchecked")
    public DataStructure asDataStructure(Platform platform, Variable<GTAInteger> arraySizeBytesVar, int elementSizeBytes)
    {
        if (isDefinedForPlatform(platform)) {
            if (isArray) {
                Array arrayStruct = new Array(values, defaultValue, arraySizeBytesVar, elementSizeBytes);
                arrayStruct.setVariableMappedToName(variableName);
                return arrayStruct;
            } else {
                return asSingleValueDataStructure();
            }
        } else {
            return new Align(platform, 0, 0, 0, 0, 0);
        }
    }
    
    @Override
    public String toString()
    {
        return values.get(0).toString();
    }
    
    private DataStructure asSingleValueDataStructure()
    {
        T value = values.get(0);
        value.setVariableMappedToName(variableName);
        return value;
    }
    
    private void populate(int size)
    {
        for (int i = 0; i < size; i++) {
            values.add(i, defaultValue);
        }
    }
}