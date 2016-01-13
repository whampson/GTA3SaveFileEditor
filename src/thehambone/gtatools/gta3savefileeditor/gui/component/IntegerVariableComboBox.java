
package thehambone.gtatools.gta3savefileeditor.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private IntegerVariable var;
    private int valueOffset;
    
    public IntegerVariableComboBox()
    {
        this(null, 0);
    }
    
    public IntegerVariableComboBox(IntegerVariable var, int valueOffset)
    {
        this.var = var;
        this.valueOffset = valueOffset;
        
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
                updateVariable();
            }
        });
    }
    
    @Override
    public IntegerVariable getVariable()
    {
        return var;
    }
    
    @Override
    public void setVariable(IntegerVariable var)
    {
        this.var = var;
        
        refreshComponent();
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
        
        var.parseValue(Integer.toString(getSelectedIndex() + valueOffset));
        Logger.debug("Variable updated: " + var);
    }
}
