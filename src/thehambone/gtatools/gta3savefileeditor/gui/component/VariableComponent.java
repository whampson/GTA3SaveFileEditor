
package thehambone.gtatools.gta3savefileeditor.gui.component;

import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.Variable;


/**
 * Created on Jan 10, 2016.
 *
 * @author thehambone
 * @param <T>
 */
public interface VariableComponent<T extends Variable>
{
    public T getVariable();
    
    public void setVariable(T var);
    
    /**
     * Sets the data displayed by the component based on the current value of
     * the variable.
     */
    public void refreshComponent();
    
    /**
     * Sets the value of variable based on the data currently displayed by the
     * component.
     */
    public void updateVariable();
}
