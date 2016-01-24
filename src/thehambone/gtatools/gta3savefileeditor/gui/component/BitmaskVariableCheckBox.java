
package thehambone.gtatools.gta3savefileeditor.gui.component;

import thehambone.gtatools.gta3savefileeditor.gui.page.Page;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.IntegerVariable;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 19, 2016.
 *
 * @author thehambone
 */
public class BitmaskVariableCheckBox extends IntegerVariableCheckBox
{
    private int mask;
    
    public BitmaskVariableCheckBox()
    {
        this(null, 0);
    }
    
    public BitmaskVariableCheckBox(IntegerVariable var, int mask,
            IntegerVariable... supplementaryVars)
    {
        super(var, -1, -1, supplementaryVars);
        this.mask = mask;
    }
    
    public int getMask()
    {
        return mask;
    }
    
    public void setMask(int mask)
    {
        this.mask = mask;
    }
    
    @Override
    public void refreshComponent()
    {
        IntegerVariable v = getVariable();
        if (v == null) {
            return;
        }
        
        int val = Integer.parseInt(v.getValue().toString());
        
        boolean temp = doUpdateOnChange;
        doUpdateOnChange = false;
        setSelected((val & mask) == mask);
        doUpdateOnChange = temp;
    }
    
    @Override
    public void updateVariable()
    {
        IntegerVariable v = getVariable();
        if (v == null) {
            return;
        }
        
        int currentVal = (int)v.toUnsignedLong();
        int val = isSelected()
                ? currentVal | mask
                : currentVal & ~mask;
        v.parseValue(Integer.toString(val));
        Logger.debug("Variable updated: " + v);
        
        for (IntegerVariable v1 : getSupplementaryVariables()) {
            v1.parseValue(Integer.toString(val));
            Logger.debug("Variable updated: " + v1);
        }
        
        if (v.dataChanged()) {
            notifyObservers(Page.Event.VARIABLE_CHANGED);
        }
    }
}
