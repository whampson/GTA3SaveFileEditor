
package thehambone.gtatools.gta3savefileeditor.gui.component;

import thehambone.gtatools.gta3savefileeditor.gui.page.Page;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarFloat;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 12, 2016.
 *
 * @author thehambone
 */
public class FloatVariableSlider extends VariableSlider<VarFloat>
{
    private int scale;
    
    public FloatVariableSlider()
    {
        this(null, 0, 1, 0, 1);
    }
    
    public FloatVariableSlider(VarFloat var, int min, int max, int value,
            int scale, VarFloat... supplementaryVars)
    {
        super(var, min, max, value, supplementaryVars);
    }
    
    public int getScale()
    {
        return scale;
    }
    
    public void setScale(int scale)
    {
        this.scale = scale;
    }
    
    public float getScaledValue()
    {
        return (float)getValue() / scale;
    }
    
    public void setScaledValue(float value)
    {
        setValue((int)(value * scale));
    }
    
    @Override
    public void refreshComponent()
    {
        VarFloat v = getVariable();
        if (v == null) {
            return;
        }
        
        boolean temp = doUpdateOnChange;
        doUpdateOnChange = false;
        setScaledValue(v.getValue());
        doUpdateOnChange = temp;
    }
    
    @Override
    public void updateVariable()
    {
        VarFloat v = getVariable();
        if (v == null) {
            return;
        }
        
        v.setValue(getScaledValue());
        Logger.debug("Variable updated: " + v);
        
        for (VarFloat v1 : getSupplementaryVariables()) {
            v1.setValue(getScaledValue());
            Logger.debug("Variable updated: " + v1);
        }
        
        if (v.dataChanged()) {
            notifyObservers(Page.Event.VARIABLE_CHANGED);
        }
    }
}
