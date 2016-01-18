
package thehambone.gtatools.gta3savefileeditor.gui.component;

import javax.swing.JCheckBox;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.Variable;

/**
 * Created on Jan 17, 2016.
 *
 * @author thehambone
 * @param <T>
 */
public abstract class VariableCheckBox<T extends Variable>
        extends JCheckBox implements VariableComponent<T>
{
    protected VariableCheckBox()
    {
        super();
    }
}
