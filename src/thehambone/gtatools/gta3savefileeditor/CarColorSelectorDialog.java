
package thehambone.gtatools.gta3savefileeditor;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;

/**
 * Created on Jan 20, 2016.
 *
 * @author thehambone
 */
public class CarColorSelectorDialog extends JDialog
{
    private GameConstants.CarColor selectedCarColor;
    
    public CarColorSelectorDialog(Window parent)
    {
        super(parent, "Car Color Selector");
        selectedCarColor = null;
        
        initWindow();
        initColorGrid();
    }
    
    public GameConstants.CarColor showColorSelectionDialog()
    {
        setVisible(true);
        return selectedCarColor;
    }
    
    private void initWindow()
    {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setPreferredSize(new Dimension(300, 300));
        setLayout(new GridLayout(10, 10, 2, 2));
        pack();
        setLocationRelativeTo(getParent());
    }
    
    private void initColorGrid()
    {
        for (final GameConstants.CarColor cc : GameConstants.CarColor.values()) {
            JPanel colorPanel = new JPanel();
            colorPanel.setBackground(cc.getColor());
            colorPanel.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    selectedCarColor = cc;
                    dispose();
                }
            });
            add(colorPanel);
        }
    }
}
