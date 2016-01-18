
package thehambone.gtatools.gta3savefileeditor.gui.component;

import javax.swing.JComboBox;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.Variable;

/**
 * Created on Jan 17, 2016.
 *
 * @author thehambone
 * @param <T>
 * @param <E>
 */
public abstract class VariableComboBox<T extends Variable, E>
        extends JComboBox<E> implements VariableComponent<T>
{
    public VariableComboBox()
    {
        super();
    }
}
