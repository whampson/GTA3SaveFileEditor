
package thehambone.gtatools.gta3savefileeditor.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JCheckBox;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarBoolean;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarFloat;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 13, 2016.
 *
 * @author thehambone
 */
public class BooleanVariableCheckBox
        extends JCheckBox implements VariableComponent<VarBoolean>
{
    private final List<VarBoolean> supplementaryVars;
    
    private VarBoolean var;
    
    public BooleanVariableCheckBox()
    {
        this(null);
    }
    
    public BooleanVariableCheckBox(VarBoolean var,
            VarBoolean... supplementaryVars)
    {
        this.var = var;
        this.supplementaryVars
                = new ArrayList<>(Arrays.asList(supplementaryVars));
        
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
    public VarBoolean getVariable()
    {
        return var;
    }
    
    @Override
    public void setVariable(VarBoolean var, VarBoolean... supplementaryVars)
    {
        this.var = var;
        
        this.supplementaryVars.clear();
        this.supplementaryVars.addAll(Arrays.asList(supplementaryVars));
        
        refreshComponent();
    }
    
    @Override
    public List<VarBoolean> getSupplementaryVariables()
    {
        return Collections.unmodifiableList(supplementaryVars);
    }
    
    @Override
    public void refreshComponent()
    {
        if (var == null) {
            return;
        }
        
        setSelected(var.getValue());
    }
    
    @Override
    public void updateVariable()
    {
        if (var == null) {
            return;
        }
        
        var.setValue(isSelected());
        Logger.debug("Variable updated: " + var);
        
        for (VarBoolean v : getSupplementaryVariables()) {
            v.setValue(isSelected());
            Logger.debug("Variable updated: " + v);
        }
    }
}
