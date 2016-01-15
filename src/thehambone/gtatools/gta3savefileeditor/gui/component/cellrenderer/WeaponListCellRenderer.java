package thehambone.gtatools.gta3savefileeditor.gui.component.cellrenderer;

import com.sun.java.swing.plaf.windows.WindowsBorders;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;

/**
 * Renders a
 * {@link thehambone.gtatools.gta3savefileeditor.game.GameConstants.Weapon} as
 * a JLabel, which can be used in lists.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 06, 2015
 */
public class WeaponListCellRenderer extends DefaultListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value == null || !(value instanceof GameConstants.Weapon)) {
            return this;
        }
        
        GameConstants.Weapon weapon = (GameConstants.Weapon)value;
        setText(weapon.getFriendlyName());
        
        if (isSelected) {
            setForeground(list.getSelectionForeground());
            setBackground(list.getSelectionBackground());
            if (index == -1 && list.hasFocus()) {
                setBorder(new WindowsBorders.DashedBorder(Color.BLACK));
            }
        }
        return this;
    }
}