
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
public class VariableComboBox<E>
        extends JComboBox<E> implements VariableComponent<IntegerVariable>
{
    private IntegerVariable var;
    
    public VariableComboBox()
    {
        this(null);
    }
    
    public VariableComboBox(IntegerVariable var)
    {
        this.var = var;
        initActionListener();
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
        
        setSelectedIndex(Integer.parseInt(var.getValue().toString()));
    }

    @Override
    public void updateVariable()
    {
        if (var == null) {
            return;
        }
        
        var.parseValue(Integer.toString(getSelectedIndex()));
        Logger.debug("Variable updated: " + var);
    }
}
