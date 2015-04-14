package thehambone.gtatools.gta3savefileeditor.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;

/**
 * An extension of a {@link javax.swing.JComboBox} that is used to modify the
 * value of a
 * {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable}.
 * 
 * @author thehambone
 * @version 0.1
 * @param <E> the type of the elements of this ComboBox
 * @since 0.1, March 29, 2015
 */
public class VariableValueComboBox<E> extends JComboBox<E> implements VariableValueComponent
{
    private Variable var;
    private int arrayIndex = 0;
    private int initialValue;
    
    public VariableValueComboBox()
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
                if (getSelectedIndex() == initialValue) {
                    return;
                }
                if (var != null) {
                    var.setValueAt(arrayIndex, Integer.toString(getSelectedIndex()));
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
        initialValue = Integer.parseInt(var.getValueAt(arrayIndex).toString());
        update();
    }
    
    @Override
    public void update()
    {
        if (var != null) {
            setSelectedIndex(Integer.parseInt(var.getValueAt(arrayIndex).toString()));
        }
    }
}