package thehambone.gtatools.gta3savefileeditor.gui.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import thehambone.gtatools.gta3savefileeditor.gui.GUIUtils;
import thehambone.gtatools.gta3savefileeditor.gui.component.document.FloatDocumentFilter;
import thehambone.gtatools.gta3savefileeditor.gui.component.document.WholeNumberDocumentFilter;
import thehambone.gtatools.gta3savefileeditor.gui.component.document.StringDocumentFilter;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.DataStructure;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarByte;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarFloat;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarShort;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarString;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.Variable;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * An extension of a {@link javax.swing.JTextField} that is used to modify the
 * value of a
 * {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable} or
 * set of Variables.
 * <p>
 * Created on Mar 29, 2015.
 * 
 * @author thehambone
 * @deprecated 
 */
public final class VariableValueTextField
        extends JTextField implements VariableValueComponent
{
    private final List<Variable> additionalVarsToUpdate;
    
    private Variable var;
//    private String initialValue;
//    private boolean valueChanged;
    private String displayFormat;
    private InputType inputType;
    private boolean inputLengthRestricted;
    
    public VariableValueTextField()
    {
        this(null);
    }
    
    public VariableValueTextField(Variable var)
    {
        super();
        
        this.var = var;
        additionalVarsToUpdate = new ArrayList<>();
//        initialValue = null;
//        valueChanged = false;
        displayFormat = null;
        inputType = InputType.STRING;
        inputLengthRestricted = false;
        
        determineInputType();
        
//        initDocumentListener();
        initDocumentFilter();
        initFocusListener();
    }
    
//    private void initDocumentListener()
//    {
//        getDocument().addDocumentListener(new DocumentListener()
//        {
//            private void update()
//            {
////                valueChanged = !getText().equals(initialValue);
////                if (!getText().isEmpty()) {
////                    updateVariable();
////                }
//            }
//            
//            @Override
//            public void insertUpdate(DocumentEvent e)
//            {
//                if (inputType == InputType.STRING && stringInputLength > -1) {
//                    if (getText().l)
//                }
//            }
//            
//            @Override
//            public void removeUpdate(DocumentEvent e)
//            {
////                update();
//            }
//            
//            @Override
//            public void changedUpdate(DocumentEvent e)
//            {
//                // Nop
//            }
//        });
//    }
    
    private void initDocumentFilter()
    {
        PlainDocument doc = (PlainDocument)getDocument();
        switch (inputType) {
            case STRING:
//                doc.setDocumentFilter(new StringDocumentFilter());
                break;
            case INT:
                doc.setDocumentFilter(new WholeNumberDocumentFilter(
                        Integer.MIN_VALUE, Integer.MAX_VALUE));
                break;
            case BYTE:
                doc.setDocumentFilter(new WholeNumberDocumentFilter(
                        Byte.MIN_VALUE, Byte.MAX_VALUE));
                break;
            case SHORT:
                doc.setDocumentFilter(new WholeNumberDocumentFilter(
                        Short.MIN_VALUE, Short.MAX_VALUE));
                break;
            case FLOAT:
                doc.setDocumentFilter(new FloatDocumentFilter());
                break;
        }
    }
    
    private void initFocusListener()
    {
        addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
//                initialValue = getText();
                selectAll();
            }
            
            @Override
            public void focusLost(FocusEvent e)
            {
                updateVariable();
                if (getText().equals("-")) {
                    setText("0");       // Default value
                }
            }
        });
    }
    
    private void determineInputType()
    {
        DataStructure dsToBeModified = var;
        if (var instanceof VarArray) {
            dsToBeModified = ((VarArray)var).getElementAt(0);
        }
        
        if (dsToBeModified == null || dsToBeModified instanceof VarString) {
            inputType = InputType.STRING;
        } else if (dsToBeModified instanceof VarByte) {
            inputType = InputType.BYTE;
        } else if (dsToBeModified instanceof VarFloat) {
            inputType = InputType.FLOAT;
        } else if (dsToBeModified instanceof VarInt) {
            inputType = InputType.INT;
        } else if (dsToBeModified instanceof VarShort) {
            inputType = InputType.SHORT;
        } else {
            throw new UnsupportedOperationException("foo"); // TODO: message
        }
    }
    
    public void addVariableToUpdate(Variable var)
    {
        // todo: handle array variables
        additionalVarsToUpdate.add(var);
    }
    
    public void setNumberDisplayFormat(String displayFormat)
    {
        this.displayFormat = displayFormat;
    }
    
//    public void setInputType(InputType inputType)
//    {
//        this.inputType = inputType;
//        initDocumentFilter();
//    }
    
    public void updateVariable()
    {
//        if (!valueChanged || var == null) {
//            return;
//        }
        if (var == null) {
            return;
        }
        
        if (getText().isEmpty()) {
            if (inputType == InputType.STRING) {
//                GUIUtils.showErrorMessage(getTopLevelAncestor(), "Property cannot be empty!", "Error");
//                requestFocus();
//                return;
                setText((String)var.getValue());    // TODO: unsafe
            } else {
                setText("0");
            }
        } else if (getText().equals("-") && inputType != InputType.STRING) {
            return;
        }
        try {
            var.parseValue(getText());
            for (Variable v : additionalVarsToUpdate) {
                v.parseValue(getText());
            }
        } catch (NumberFormatException ex) {
            GUIUtils.showErrorMessage(getTopLevelAncestor(), "Property format is incorrect!", "Invalid Format", ex);
            requestFocus();
            selectAll();
        }
        Logger.debug("Variable updated: " + var);
    }
    
    @Override
    public void setVariable(Variable var)
    {
        this.var = var;
        determineInputType();
        initDocumentFilter();
        update();
    }
    
//    @Override
//    public void setVariable(Variable var, int arrayIndex)
//    {
//        this.var = var;
////        this.arrayIndex = arrayIndex;
//        update();
//    }
    
    @Override
    public void update()
    {
        if (var != null) {
            if (displayFormat == null || displayFormat.isEmpty()) {
                setText(var.getValue().toString());
            } else {
                // TODO: This is unsafe -- What if the format is not for an integer? What if the variable value is not a number?
                setText(String.format(displayFormat, Integer.parseInt(var.getValue().toString())));
            }
        }
    }
    
    public enum InputType
    {
        STRING,
        FLOAT,
        BYTE,
        SHORT,
        INT,
        UNSIGNED_BYTE,
        UNSIGNED_SHORT,
        UNSIGNED_INT;
    }
}
