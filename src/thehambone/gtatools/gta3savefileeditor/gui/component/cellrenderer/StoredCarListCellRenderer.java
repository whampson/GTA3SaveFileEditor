package thehambone.gtatools.gta3savefileeditor.gui.component.cellrenderer;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.StoredCar;

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
    private final DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
    
    @Override
    public Component getListCellRendererComponent(
            JList<? extends StoredCar> list, StoredCar value, int index,
            boolean isSelected, boolean cellHasFocus)
    {
        JLabel comp = (JLabel)dlcr.getListCellRendererComponent(list, value,
                index, isSelected, cellHasFocus);
        
        if (isSelected) {
            comp.setBackground(list.getSelectionBackground());
            comp.setForeground(list.getSelectionForeground());
        }
        
        if (value == null) {
            return comp;
        }
        
        int modelID = value.nModelID.getValue();
        
        GameConstants.Vehicle vehicle = null;
        for (GameConstants.Vehicle v : GameConstants.Vehicle.values()) {
            if (v.getID() == modelID) {
                vehicle = v;
            }
        }
        
        comp.setText(vehicle != null ? vehicle.getFriendlyName() : "<invalid>");
        
        return comp;
    }
}
