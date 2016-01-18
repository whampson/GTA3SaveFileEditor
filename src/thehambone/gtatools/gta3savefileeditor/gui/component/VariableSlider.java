
package thehambone.gtatools.gta3savefileeditor.gui.component;

import javax.swing.JSlider;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.Variable;

/**
 * Created on Jan 17, 2016.
 *
 * @author thehambone
 * @param <T>
 */
public abstract class VariableSlider<T extends Variable>
        extends JSlider implements VariableComponent<T>
{
    public VariableSlider(int min, int max, int value)
    {
        super(min, max, value);
    }
}
