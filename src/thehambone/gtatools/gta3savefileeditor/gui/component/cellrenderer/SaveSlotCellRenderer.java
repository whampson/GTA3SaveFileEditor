package thehambone.gtatools.gta3savefileeditor.gui.component.cellrenderer;

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
 * Renders a
 * {@link thehambone.gtatools.gta3savefileeditor.savefile.PCSaveSlot} as
 * a JLabel, which can be used in lists.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 06, 2015
 */
public class SaveSlotCellRenderer implements ListCellRenderer<PCSaveSlot>
{
    @Override
    public Component getListCellRendererComponent(JList<? extends PCSaveSlot> list, PCSaveSlot value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(1, 5, 1, 5));
        
        JLabel titleLabel = new JLabel();
        JLabel timestampLabel = new JLabel();
        titleLabel.setFont(list.getFont());
        timestampLabel.setFont(list.getFont());
        
        if (!value.isEmpty()) {
            if (value.getSaveTitle().isEmpty()) {
                titleLabel.setText("Slot " + value.getSlotNumber() + " is corrupt");
                titleLabel.setForeground(Color.gray);
            } else {
                titleLabel.setText(value.getSaveTitle());
                timestampLabel.setText(value.getSaveTimeStampString());
            }
        } else {
            titleLabel.setText("Slot " + value.getSlotNumber() + " is free");
            titleLabel.setForeground(Color.gray);
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