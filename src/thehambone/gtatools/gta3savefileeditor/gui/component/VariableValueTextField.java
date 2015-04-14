package thehambone.gtatools.gta3savefileeditor.gui.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;
import thehambone.gtatools.gta3savefileeditor.gui.GUIUtils;
import thehambone.gtatools.gta3savefileeditor.gui.component.document.FloatingPointDocumentFilter;
import thehambone.gtatools.gta3savefileeditor.gui.component.document.IntegerDocumentFilter;

/**
 * An extension of a {@link javax.swing.JTextField} that is used to modify the
 * value of a
 * {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable} or
 * set of Variables.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 29, 2015
 */
public class VariableValueTextField extends JTextField implements VariableValueComponent
{
    private Variable mainVar;
    private int arrayIndex = 0;
    private String initialValue;
    private boolean valueChanged;
    private String displayFormat;
    private List<Variable> additionalVarsToUpdate = new ArrayList<>();
    private InputType inputType = InputType.ANYTHING;
    
    public VariableValueTextField()
    {
        super();
        initDocumentListener();
        initFocusListener();
        initDocumentFilter();
    }
    
    private void initDocumentListener()
    {
        getDocument().addDocumentListener(new DocumentListener()
        {
            private void update()
            {
                valueChanged = !getText().equals(initialValue);
                if (!getText().isEmpty()) {
                    updateVariable();
                }
            }
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                update();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                update();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                update();
            }
        });
    }
    
    private void initDocumentFilter()
    {
        PlainDocument doc = (PlainDocument)getDocument();
        switch (inputType) {
            case ANYTHING:
                doc.setDocumentFilter(null);
                break;
            case INTEGER:
                doc.setDocumentFilter(new IntegerDocumentFilter());
                break;
            case DECIMAL:
                doc.setDocumentFilter(new FloatingPointDocumentFilter());
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
                initialValue = getText();
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
    
    public void addVariableToUpdate(Variable var)
    {
        // todo: handle array variables
        additionalVarsToUpdate.add(var);
    }
    
    public void setDisplayFormat(String displayFormat)
    {
        this.displayFormat = displayFormat;
    }
    
    public void setInputType(InputType inputType)
    {
        this.inputType = inputType;
        initDocumentFilter();
    }
    
    public void updateVariable()
    {
        if (!valueChanged || mainVar == null) {
            return;
        }
        if (getText().isEmpty()) {
            if (inputType == InputType.ANYTHING) {
                GUIUtils.showErrorMessage(getTopLevelAncestor(), "Property cannot be empty!", "Error");
                requestFocus();
                return;
            } else {
                setText("0");
            }
        } else if (getText().equals("-") && inputType != InputType.ANYTHING) {
            return;
        }
        try {
            mainVar.setValueAt(arrayIndex, getText());
            for (Variable v : additionalVarsToUpdate) {
                v.setValueAt(arrayIndex, getText());
            }
        } catch (NumberFormatException ex) {
            GUIUtils.showErrorMessage(getTopLevelAncestor(), "Property format is incorrect!", "Invalid Format", ex);
            requestFocus();
            selectAll();
        }
    }
    
    @Override
    public void setVariable(Variable var)
    {
        setVariable(var, 0);
    }
    
    @Override
    public void setVariable(Variable var, int arrayIndex)
    {
        this.mainVar = var;
        this.arrayIndex = arrayIndex;
        update();
    }
    
    @Override
    public void update()
    {
        if (mainVar != null) {
            if (displayFormat == null || displayFormat.isEmpty()) {
                setText(mainVar.getValueAt(arrayIndex).toString());
            } else {
                // This is unsafe -- What if the format is not for an integer? What if the variable value is not a number?
                setText(String.format(displayFormat, Integer.parseInt(mainVar.getValueAt(arrayIndex).toString())));
            }
        }
    }
    
    public static enum InputType
    {
        ANYTHING, INTEGER, DECIMAL;
    }
}