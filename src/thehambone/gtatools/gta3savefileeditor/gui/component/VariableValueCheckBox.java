package thehambone.gtatools.gta3savefileeditor.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;

/**
 * An extension of a {@link javax.swing.JCheckBox} that is used to modify the
 * value of a
 * {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable}.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 31, 2015
 */
public class VariableValueCheckBox extends JCheckBox implements VariableValueComponent
{
    private Variable var;
    private int arrayIndex = 0;
    
    public VariableValueCheckBox()
    {
        super();
        initActionListener();
    }
    
    private void initActionListener()
    {
        addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (var != null) {
                    var.setValueAt(arrayIndex, Boolean.toString(isSelected()));
                }
            }
        });
    }
    
    @Override
    public void setVariable(Variable var)
    {
        setVariable(var, 0);
    }
    
    @Override
    public void setVariable(Variable var, int arrayIndex)
    {
        this.var = var;
        this.arrayIndex = arrayIndex;
        update();
    }
    
    @Override
    public void update()
    {
        if (var != null) {
            setSelected(Boolean.parseBoolean(var.getValueAt(arrayIndex).toString()));
        }
    }
}