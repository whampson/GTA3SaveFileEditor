
package thehambone.gtatools.gta3savefileeditor.gui.component;

import thehambone.gtatools.gta3savefileeditor.gui.page.Page;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarBoolean;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 13, 2016.
 *
 * @author thehambone
 */
public class BooleanVariableCheckBox extends VariableCheckBox<VarBoolean>
{
    public BooleanVariableCheckBox()
    {
        this(null);
    }
    
    public BooleanVariableCheckBox(VarBoolean var,
            VarBoolean... supplementaryVars)
    {
        super(var, supplementaryVars);
    }
    
    @Override
    public void refreshComponent()
    {
        VarBoolean v = getVariable();
        if (v == null) {
            return;
        }
        
        boolean temp = doUpdateOnChange;
        doUpdateOnChange = false;
        setSelected(v.getValue());
        doUpdateOnChange = temp;
    }
    
    @Override
    public void updateVariable()
    {
        VarBoolean v = getVariable();
        if (v == null) {
            return;
        }
        
        v.setValue(isSelected());
        Logger.debug("Variable updated: " + v);
        
        for (VarBoolean v1 : getSupplementaryVariables()) {
            v1.setValue(isSelected());
            Logger.debug("Variable updated: " + v1);
        }
        
        if (v.dataChanged()) {
            notifyObservers(Page.Event.VARIABLE_CHANGED);
        }
    }
}
