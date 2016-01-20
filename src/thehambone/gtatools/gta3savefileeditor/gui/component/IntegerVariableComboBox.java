
package thehambone.gtatools.gta3savefileeditor.gui.component;

import thehambone.gtatools.gta3savefileeditor.gui.page.Page;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.IntegerVariable;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 12, 2016.
 *
 * @author thehambone
 * @param <E>
 */
public class IntegerVariableComboBox<E>
        extends VariableComboBox<IntegerVariable, E>
{
    private int valueOffset;
    
    public IntegerVariableComboBox()
    {
        this(null, 0);
    }
    
    public IntegerVariableComboBox(IntegerVariable var, int valueOffset,
            IntegerVariable... supplementaryVars)
    {
        super(var, supplementaryVars);
        this.valueOffset = valueOffset;
    }
    
    public int getValueOffset()
    {
        return valueOffset;
    }
    
    public void setValueOffset(int valueOffset)
    {
        this.valueOffset = valueOffset;
    }
    
    @Override
    public void refreshComponent()
    {
        IntegerVariable v = getVariable();
        if (v == null) {
            return;
        }
        
        int val = Integer.parseInt(v.getValue().toString()) - valueOffset;
        if (val > -1 && val < getItemCount() - 1) {
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
        
        String val = Integer.toString(getSelectedIndex() + valueOffset);
        v.parseValue(val);
        Logger.debug("Variable updated: " + v);
        
        for (IntegerVariable v1 : getSupplementaryVariables()) {
            v1.parseValue(val);
            Logger.debug("Variable updated: " + v1);
        }
        
        if (v.dataChanged()) {
            notifyObservers(Page.Event.VARIABLE_CHANGED);
        } else {
            notifyObservers(Page.Event.VARIABLE_UNCHANGED);
        }
    }
}
