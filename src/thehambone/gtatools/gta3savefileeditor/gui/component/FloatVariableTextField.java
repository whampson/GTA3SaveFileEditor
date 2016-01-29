
package thehambone.gtatools.gta3savefileeditor.gui.component;

import thehambone.gtatools.gta3savefileeditor.gui.page.Page;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarFloat;
import thehambone.gtatools.gta3savefileeditor.util.Logger;
import thehambone.gtatools.gta3savefileeditor.util.NumberUtilities;

/**
 * Created on Jan 11, 2016.
 *
 * @author thehambone
 */
public class FloatVariableTextField
        extends VariableTextField<VarFloat>
{
    public FloatVariableTextField()
    {
        this(null);
    }
    
    public FloatVariableTextField(VarFloat var, VarFloat... supplementaryVars)
    {
        super(var, supplementaryVars);
    }
    
    @Override
    protected boolean isInputValid()
    {
        return NumberUtilities.isNumeric(getText());
    }
    
    @Override
    public void refreshComponent()
    {
        VarFloat v = getVariable();
        if (v == null) {
            return;
        }
        
        if (Float.isInfinite(v.getValue()) || Float.isNaN(v.getValue())) {
            v.setValue(0f);
        }
        
        boolean temp = doUpdateOnChange;
        doUpdateOnChange = false;
        isComponentRefreshing = true;
        
        String format = getDisplayFormat();
        if (format == null || format.isEmpty()) {
            setText(v.getValue().toString());
        } else {
            setText(String.format(format, v.getValue().toString()));
        }
        
        isComponentRefreshing = false;
        doUpdateOnChange = temp;
    }
    
    @Override
    public void updateVariable()
    {
        VarFloat v = getVariable();
        if (v == null) {
            return;
        }
        
        v.parseValue(getText());
        Logger.debug("Variable updated: " + v);
        
        for (VarFloat v1 : getSupplementaryVariables()) {
            v1.parseValue(getText());
            Logger.debug("Variable updated: " + v1);
        }
        
        notifyObservers(Page.Event.VARIABLE_CHANGED);
    }
}
