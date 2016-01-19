
package thehambone.gtatools.gta3savefileeditor.gui.component;

import thehambone.gtatools.gta3savefileeditor.gui.page.Page;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.IntegerVariable;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 13, 2016.
 *
 * @author thehambone
 */
public class IntegerVariableCheckBox extends VariableCheckBox<IntegerVariable>
{
    private int deselectedValue;
    private int selectedValue;
    
    public IntegerVariableCheckBox()
    {
        this(null, 0, 1);
    }
    
    public IntegerVariableCheckBox(IntegerVariable var, int deselectedValue,
            int selectedValue, IntegerVariable... supplementaryVars)
    {
        super(var, supplementaryVars);
        this.deselectedValue = deselectedValue;
        this.selectedValue = selectedValue;
    }
    
    public int getDeselectedValue()
    {
        return deselectedValue;
    }
    
    public void setDeselectedValue(int value)
    {
        deselectedValue = value;
    }
    
    public int getSelectedValue()
    {
        return selectedValue;
    }
    
    public void setSelectedValue(int value)
    {
        selectedValue = value;
    }
    
    @Override
    public void refreshComponent()
    {
        IntegerVariable v = getVariable();
        if (v == null) {
            return;
        }
        
        int val = Integer.parseInt(v.getValue().toString());
        
        setSelected(val == selectedValue);
    }
    
    @Override
    public void updateVariable()
    {
        IntegerVariable v = getVariable();
        if (v == null) {
            return;
        }
        
        int val = isSelected() ? selectedValue : deselectedValue;
        v.parseValue(Integer.toString(val));
        Logger.debug("Variable updated: " + v);
        
        for (IntegerVariable v1 : getSupplementaryVariables()) {
            v1.parseValue(Integer.toString(val));
            Logger.debug("Variable updated: " + v1);
        }
        
        if (v.dataChanged()) {
            notifyObservers(Page.Event.VARIABLE_CHANGED);
        } else {
            notifyObservers(Page.Event.VARIABLE_UNCHANGED);
        }
    }
}
