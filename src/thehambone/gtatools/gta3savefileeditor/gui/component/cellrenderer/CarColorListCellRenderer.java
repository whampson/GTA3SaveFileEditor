package thehambone.gtatools.gta3savefileeditor.gui.component.cellrenderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;

/**
 * Renders a
 * {@link thehambone.gtatools.gta3savefileeditor.game.GameConstants.CarColor} as
 * a JLabel, which can be used in lists.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 04, 2015
 */
public class CarColorListCellRenderer implements ListCellRenderer<GameConstants.CarColor>
{
    private final DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList<? extends GameConstants.CarColor> list, GameConstants.CarColor value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel renderer = (JLabel)defaultListCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value == null) {
            return renderer;
        }
        renderer.setText(value.getFriendlyName());
        if (isSelected) {
            renderer.setForeground(list.getSelectionForeground());
            renderer.setBackground(list.getSelectionBackground());
        } else {
            renderer.setBackground(value.getColor());
            renderer.setForeground(new Color(~value.getColor().getRGB()));
        }
        return renderer;
    } 
}