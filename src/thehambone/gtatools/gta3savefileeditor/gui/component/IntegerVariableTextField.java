
package thehambone.gtatools.gta3savefileeditor.gui.component;

import javax.swing.text.PlainDocument;
import thehambone.gtatools.gta3savefileeditor.gui.component.document.IntegerDocumentFilter;
import thehambone.gtatools.gta3savefileeditor.gui.page.Page;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarByte;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarShort;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.IntegerVariable;
import thehambone.gtatools.gta3savefileeditor.util.Logger;
import thehambone.gtatools.gta3savefileeditor.util.NumberUtilities;

/**
 * Created on Jan 10, 2016.
 *
 * @author thehambone
 */
public final class IntegerVariableTextField
        extends VariableTextField<IntegerVariable>
{
    private DataType dataType;
    private boolean isUnsigned;
    
    public IntegerVariableTextField()
    {
        this(null, false);
    }

    public IntegerVariableTextField(IntegerVariable var,
            boolean isUnsigned, IntegerVariable... supplementaryVars)
    {
        super(var, supplementaryVars);
        this.isUnsigned = isUnsigned;
        dataType = DataType.INT;
        
        determineDataType();
        initDocumentFilter();
        refreshComponent();
    }
    
    public boolean isUnsigned()
    {
        return dataType == DataType.UNSIGNED_BYTE
                || dataType == DataType.UNSIGNED_INT
                || dataType == DataType.UNSIGNED_SHORT;
    }
    
    public void setUnsigned(boolean isUnsigned)
    {
        this.isUnsigned = isUnsigned;
        determineDataType();
        initDocumentFilter();
        refreshComponent();
    }
    
    private void determineDataType()
    {
        IntegerVariable v = getVariable();
        if (v instanceof VarByte) {
            dataType = isUnsigned ? DataType.UNSIGNED_BYTE : DataType.BYTE;
        } else if (v instanceof VarInt) {
            dataType = isUnsigned ? DataType.UNSIGNED_INT : DataType.INT;
        } else if (v instanceof VarShort) {
            dataType = isUnsigned ? DataType.UNSIGNED_SHORT : DataType.SHORT;
        }
    }
    
    private void initDocumentFilter()
    {
        PlainDocument doc = (PlainDocument)getDocument();
        switch (dataType) {
            case BYTE:
                doc.setDocumentFilter(new IntegerDocumentFilter(
                        Byte.MIN_VALUE, Byte.MAX_VALUE));
                break;
            case INT:
                doc.setDocumentFilter(new IntegerDocumentFilter(
                        Integer.MIN_VALUE, Integer.MAX_VALUE));
                break;
            case SHORT:
                doc.setDocumentFilter(new IntegerDocumentFilter(
                        Short.MIN_VALUE, Short.MAX_VALUE));
                break;
            case UNSIGNED_BYTE:
                doc.setDocumentFilter(new IntegerDocumentFilter(
                        0, NumberUtilities.UNSIGNED_BYTE_MAX_VALUE));
                break;
            case UNSIGNED_INT:
                doc.setDocumentFilter(new IntegerDocumentFilter(
                        0, NumberUtilities.UNSIGNED_INT_MAX_VALUE));
                break;
            case UNSIGNED_SHORT:
                doc.setDocumentFilter(new IntegerDocumentFilter(
                        0, NumberUtilities.UNSIGNED_SHORT_MAX_VALUE));
                break;
        }
    }
    
    @Override
    protected boolean isInputValid()
    {
        return NumberUtilities.isInteger(getText());
    }
    
    @Override
    public void refreshComponent()
    {
        IntegerVariable v = getVariable();
        if (v == null) {
            return;
        }
        
        long l = 0;
        switch (dataType) {
            case UNSIGNED_BYTE:
            case UNSIGNED_INT:
            case UNSIGNED_SHORT:
                l = v.toUnsignedLong();
                break;
            case BYTE:
            case INT:
            case SHORT:
                l = Long.parseLong(v.getValue().toString());
                break;
        }
        
        boolean temp = doUpdateOnChange;
        doUpdateOnChange = false;
        isComponentRefreshing = true;
        
        String format = getDisplayFormat();
        if (format == null || format.isEmpty()) {
            setText(Long.toString(l));
        } else {
            setText(String.format(format, l));
        }
        
        isComponentRefreshing = false;
        doUpdateOnChange = temp;
    }
    
    @Override
    public void updateVariable()
    {
        IntegerVariable v = getVariable();
        if (v == null) {
            return;
        }
        
        v.parseValue(getText(), isUnsigned);
        Logger.debug("Variable updated: " + v);
        
        for (IntegerVariable v1 : getSupplementaryVariables()) {
            v1.parseValue(getText(), isUnsigned);
            Logger.debug("Variable updated: " + v1);
        }
        
        if (v.dataChanged()) {
            notifyObservers(Page.Event.VARIABLE_CHANGED);
        }
    }
    
    private static enum DataType
    {
        BYTE,
        SHORT,
        INT,
        UNSIGNED_BYTE,
        UNSIGNED_SHORT,
        UNSIGNED_INT;
    }
}
