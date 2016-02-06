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
 * A {@code CarColorSelectorDialog} is an extension of {@code JDialog} that
 * allows the user to choose one of the game's predefined vehicle colors. The
 * colors are arranged in a 10x10 grid and the user must simply click the
 * desired color to select it.
 * <p>
 * Created on Jan 20, 2016.
 *
 * @author thehambone
 */
public class CarColorSelectorDialog extends JDialog
{
    private GameConstants.CarColor selectedCarColor;
    
    /**
     * Creates a new {@code CarColorSelectorDialog} that is modal with respect
     * to the specified parent window.
     * 
     * @param parent the dialog parent window
     */
    public CarColorSelectorDialog(Window parent)
    {
        super(parent, "Car Color Selector");
        selectedCarColor = null;
        
        initWindow();
        initColorGrid();
    }
    
    /**
     * Displays the color selection dialog on top of the parent window.
     * 
     * @return the {@code CarColor} that corresponds to the color selected by
     *         the user; {@code null} if the user cancelled
     */
    public GameConstants.CarColor showColorSelectionDialog()
    {
        selectedCarColor = null;
        setVisible(true);
        return selectedCarColor;
    }
    
    /**
     * Sets the dialog properties.
     */
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
    
    /**
     * Places clickable colored JPanels in a 10x10 grid.
     */
    private void initColorGrid()
    {
        for (final GameConstants.CarColor cc
                : GameConstants.CarColor.values()) {
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
