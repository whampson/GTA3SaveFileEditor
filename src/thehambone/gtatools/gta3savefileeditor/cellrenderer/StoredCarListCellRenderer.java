package thehambone.gtatools.gta3savefileeditor.cellrenderer;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.StoredCar;

/**
 * Renders a {@code StoredCar} for use as an item in a graphical list. The list
 * cell contains the name of the {@code StoredCar}.
 * <p>
 * Created on Apr 4, 2015.
 * 
 * @author thehambone
 */
public final class StoredCarListCellRenderer
        implements ListCellRenderer<StoredCar>
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
