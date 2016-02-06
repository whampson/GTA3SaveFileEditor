package thehambone.gtatools.gta3savefileeditor.cellrenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import thehambone.gtatools.gta3savefileeditor.savefile.PCSaveSlot;

/**
 * Renders a {@code PCSaveSlot} for use as an item in a graphical list. The list
 * cell contains the name of the PC save file as well as the file timestamp.
 * <p>
 * Created on Apr 6, 2015.
 * 
 * @author thehambone
 */
public class SaveSlotCellRenderer implements ListCellRenderer<PCSaveSlot>
{
    @Override
    public Component getListCellRendererComponent(
            JList<? extends PCSaveSlot> list, PCSaveSlot value, int index,
            boolean isSelected, boolean cellHasFocus)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(1, 5, 1, 5));
        
        JLabel titleLabel = new JLabel();
        JLabel timestampLabel = new JLabel();
        titleLabel.setFont(list.getFont());
        timestampLabel.setFont(list.getFont());
        
        if (!value.isUsable()) {
            titleLabel.setText("Slot is not usable "
                    + "- check file or GTA3 save dir");
            titleLabel.setForeground(Color.gray);
        } else if (value.isEmpty()) {
            titleLabel.setText("Slot " + value.getSlotNumber() + " is free");
            titleLabel.setForeground(Color.gray);
        } else {
            titleLabel.setText(value.getSaveName());
            timestampLabel.setText(value.getSaveTimestamp());
        }
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(timestampLabel, BorderLayout.EAST);
        
        if (isSelected) {
            panel.setBackground(list.getSelectionBackground());
        } else {
            panel.setBackground(list.getBackground());
        }
        
        return panel;
    }
}
