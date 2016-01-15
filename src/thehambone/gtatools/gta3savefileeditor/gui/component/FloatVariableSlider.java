
package thehambone.gtatools.gta3savefileeditor.gui.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarFloat;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 12, 2016.
 *
 * @author thehambone
 */
public class FloatVariableSlider
        extends JSlider implements VariableComponent<VarFloat>
{
    private final List<VarFloat> supplementaryVars;
    
    private VarFloat var;
    private int scale;
    
    public FloatVariableSlider()
    {
        this(null, 0, 1, 0, 1);
    }
    
    public FloatVariableSlider(VarFloat var, int min, int max, int value,
            int scale, VarFloat... supplementaryVars)
    {
        super(min, max, value);
        this.var = var;
        this.supplementaryVars
                = new ArrayList<>(Arrays.asList(supplementaryVars));
        
        initChangeListener();
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
    
    private void initChangeListener()
    {
        addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                updateVariable();
            }
        });
    }
    
    @Override
    public VarFloat getVariable()
    {
        return var;
    }
    
    @Override
    public void setVariable(VarFloat var, VarFloat... supplementaryVars)
    {
        this.var = var;
        
        this.supplementaryVars.clear();
        this.supplementaryVars.addAll(Arrays.asList(supplementaryVars));
        
        refreshComponent();
    }
    
    @Override
    public List<VarFloat> getSupplementaryVariables()
    {
        return Collections.unmodifiableList(supplementaryVars);
    }
    
    @Override
    public void refreshComponent()
    {
        if (var == null) {
            return;
        }
        
        setScaledValue(var.getValue());
    }
    
    @Override
    public void updateVariable()
    {
        if (var == null) {
            return;
        }
        
        var.setValue(getScaledValue());
        Logger.debug("Variable updated: " + var);
        
        for (VarFloat v : getSupplementaryVariables()) {
            v.setValue(getScaledValue());
            Logger.debug("Variable updated: " + v);
        }
    }
}
