package thehambone.gtatools.gta3savefileeditor.gui.component.cellrenderer;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.StoredCar;

/**
 * Renders a
 * {@link thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.StoredCar}
 * as a JLabel, which can be used in lists.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 04, 2015
 */
public class StoredCarListCellRenderer implements ListCellRenderer<StoredCar>
{
    private final DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList<? extends StoredCar> list, StoredCar value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel renderer = (JLabel)defaultListCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value == null) {
            return renderer;
        }
        int vehicleID = value.getModelIDAsVariable().getValue().intValue();
        if (vehicleID == 0) {
            renderer.setText("(empty)");
            return renderer;
        }
        GameConstants.Vehicle vehicle = GameConstants.Vehicle._EMPTY;
        for (GameConstants.Vehicle v : GameConstants.Vehicle.values()) {
            if (vehicleID == v.getID()) {
                vehicle = v;
            }
        }
        renderer.setText(vehicle.getFriendlyName());
        if (isSelected) {
            renderer.setForeground(list.getSelectionForeground());
            renderer.setBackground(list.getSelectionBackground());
        }
        return renderer;
    }

}