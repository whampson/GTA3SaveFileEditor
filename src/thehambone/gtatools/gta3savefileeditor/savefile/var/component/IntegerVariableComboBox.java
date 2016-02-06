package thehambone.gtatools.gta3savefileeditor.savefile.var.component;

import javax.swing.ComboBoxModel;
import thehambone.gtatools.gta3savefileeditor.page.Page;
import thehambone.gtatools.gta3savefileeditor.savefile.var.IntegerVariable;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 12, 2016.
 *
 * @author thehambone
 */
public class IntegerVariableComboBox
        extends VariableComboBox<IntegerVariable>
{
    public IntegerVariableComboBox()
    {
        this(null);
    }
    
    public IntegerVariableComboBox(IntegerVariable var,
            IntegerVariable... supplementaryVars)
    {
        super(var, supplementaryVars);
    }
    
    private int getItemIndex(int id)
    {
        int index = -1;        
        ComboBoxModel<VariableComboBoxItem> model = getModel();
        
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).getID() == id) {
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    @Override
    public void refreshComponent()
    {
        IntegerVariable v = getVariable();
        if (v == null) {
            return;
        }
        
        int val = getItemIndex(Integer.parseInt(v.getValue().toString()));
        if (val == -1) {
            setSelectedIndex(-1);
        } else {
            boolean temp = doUpdateOnChange;
            doUpdateOnChange = false;
            setSelectedIndex(val);
            doUpdateOnChange = temp;
        }
    }

    @Override
    public void updateVariable()
    {
        IntegerVariable v = getVariable();
        if (v == null) {
            return;
        }
        
        if (getSelectedIndex() == -1) {
            return;
        }
        VariableComboBoxItem selected = (VariableComboBoxItem)getSelectedItem();
        String val = Integer.toString(selected.getID());
        v.parseValue(val);
        Logger.debug("Variable updated: " + v);
        
        for (IntegerVariable v1 : getSupplementaryVariables()) {
            v1.parseValue(val);
            Logger.debug("Variable updated: " + v1);
        }
        
        notifyObservers(Page.Event.VARIABLE_CHANGED);
    }
}
