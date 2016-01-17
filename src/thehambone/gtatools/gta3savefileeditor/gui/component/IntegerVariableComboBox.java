
package thehambone.gtatools.gta3savefileeditor.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JComboBox;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.IntegerVariable;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 12, 2016.
 *
 * @author thehambone
 * @param <E>
 */
public class IntegerVariableComboBox<E>
        extends JComboBox<E> implements VariableComponent<IntegerVariable>
{
    private final List<IntegerVariable> supplementaryVars;
    
    private IntegerVariable var;
    private int valueOffset;
    private boolean doUpdateOnChange;
    
    public IntegerVariableComboBox()
    {
        this(null, 0);
    }
    
    public IntegerVariableComboBox(IntegerVariable var, int valueOffset,
            IntegerVariable... supplementaryVars)
    {
        this.var = var;
        this.valueOffset = valueOffset;
        this.supplementaryVars
                = new ArrayList<>(Arrays.asList(supplementaryVars));
        doUpdateOnChange = true;
        
        initActionListener();
    }
    
    public int getValueOffset()
    {
        return valueOffset;
    }
    
    public void setValueOffset(int valueOffset)
    {
        this.valueOffset = valueOffset;
    }
    
    private void initActionListener()
    {
        addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (doUpdateOnChange) {
                    updateVariable();
                }
            }
        });
    }
    
    @Override
    public boolean hasVariable()
    {
        return var != null;
    }
    
    @Override
    public IntegerVariable getVariable()
    {
        return var;
    }
    
    @Override
    public void setVariable(IntegerVariable var,
            IntegerVariable... supplementaryVars)
    {
        this.var = var;
        
        this.supplementaryVars.clear();
        this.supplementaryVars.addAll(Arrays.asList(supplementaryVars));
        
        refreshComponent();
    }
    
    @Override
    public List<IntegerVariable> getSupplementaryVariables()
    {
        return Collections.unmodifiableList(supplementaryVars);
    }
    
    @Override
    public void updateVariableOnChange(boolean doUpdate)
    {
        doUpdateOnChange = doUpdate;
    }
    
    @Override
    public void refreshComponent()
    {
        if (var == null) {
            return;
        }
        
        Logger.debug(var.toString());
        
        int val = Integer.parseInt(var.getValue().toString());
        if (val > 0 && val < getItemCount() - 1) {
            setSelectedIndex(val - valueOffset);
        }
    }

    @Override
    public void updateVariable()
    {
        if (var == null) {
            return;
        }
        
        String val = Integer.toString(getSelectedIndex() + valueOffset);
        var.parseValue(val);
        Logger.debug("Variable updated: " + var);
        
        for (IntegerVariable v : getSupplementaryVariables()) {
            v.parseValue(val);
            Logger.debug("Variable updated: " + v);
        }
    }
}
