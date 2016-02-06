package thehambone.gtatools.gta3savefileeditor.savefile.var.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import thehambone.gtatools.gta3savefileeditor.Observer;
import thehambone.gtatools.gta3savefileeditor.savefile.var.Variable;

/**
 * Created on Jan 17, 2016.
 *
 * @author thehambone
 * @param <T>
 */
public abstract class VariableSlider<T extends Variable>
        extends JSlider implements VariableComponent<T>
{
    private final List<Observer> observers;
    private final List<T> supplementaryVars;
    
    protected boolean doUpdateOnChange;
    
    private T var;
    
    @SafeVarargs
    public VariableSlider(T var, int min, int max, int value,
            T... supplementaryVars)
    {
        super(min, max, value);
        observers = new ArrayList<>();
        this.var = var;
        this.supplementaryVars
                = new ArrayList<>(Arrays.asList(supplementaryVars));
        
        doUpdateOnChange = true;
        
        initChangeListener();
    }
    
    private void initChangeListener()
    {
        addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
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
    public T getVariable()
    {
        return var;
    }
    
    @Override
    @SafeVarargs
    public final void setVariable(T var, T... supplementaryVars)
    {
        this.var = var;
        
        this.supplementaryVars.clear();
        this.supplementaryVars.addAll(Arrays.asList(supplementaryVars));
        
        refreshComponent();
    }
    
    @Override
    public List<T> getSupplementaryVariables()
    {
        return Collections.unmodifiableList(supplementaryVars);
    }
    
    @Override
    public void updateVariableOnChange(boolean doUpdate)
    {
        doUpdateOnChange = doUpdate;
    }
    
    @Override
    public void addObserver(Observer o)
    {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }
    
    @Override
    public void removeObserver(Observer o)
    {
        observers.remove(o);
    }
    
    @Override
    public void notifyObservers(Object message, Object... args)
    {
        for (Observer o : observers) {
            o.update(message, args);
        }
    }
}
