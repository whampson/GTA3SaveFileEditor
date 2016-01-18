
package thehambone.gtatools.gta3savefileeditor.gxt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * A {@code GXTSelectorDialog} is an extension of {@code JDialog} that allows
 * the user to choose a GXT text entry.
 * <p>
 * Created on Jan 17, 2016.
 *
 * @author thehambone
 * @see GXT
 */
public final class GXTSelectorDialog extends JDialog
{
    private static final String TITLE_STRING = "GXT String Selector";
    
    private Map<String, String> gxtTable;
    
    private JTable table;
    private JButton selectButton;
    private String selectedGXTKey;
    
    /**
     * Creates a new {@code GXTSelectorDialog} instance. The dialog will be
     * modal with respect to the parent window.
     * 
     * @param parent dialog parent window
     * @param gxtTable a {@code Map} containing GXT data
     */
    public GXTSelectorDialog(Window parent, Map<String, String> gxtTable)
    {
        super(parent, TITLE_STRING);
        this.gxtTable = gxtTable;
        selectedGXTKey = null;
        
        initWindow();
        initComponents();
        populateTable();
    }
    
    /**
     * Gets the GXT map used with this {@code GXTSelectorDialog} instance.
     * 
     * @return the GXT map associated with this {@code GXTSelectorDialog}
     *         instance
     */
    public Map<String, String> getGXTTable()
    {
        return Collections.unmodifiableMap(gxtTable);
    }
    
    /**
     * Sets the GXT data to be used with this this {@code GXTSelectorDialog}
     * instance.
     * 
     * @param gxtTable a {@code Map} containing GXT data
     */
    public void setGXTTable(Map<String, String> gxtTable)
    {
        this.gxtTable = gxtTable;
        populateTable();
    }
    
    /**
     * Shows the selection dialog. The value returned is the GXT key for the\
     * selected GXT string. {@code null} is returned if no selection is made.
     * 
     * @return the key for the selected GXT string; {@code null} if no selection
     *         is made
     */
    public String showKeySelectionDialog()
    {
        selectedGXTKey = null;
        setVisible(true);
        return selectedGXTKey;
    }
    
    /**
     * Shows the selection dialog. The value returned is the selected GXT
     * string. {@code null} is returned if no selection is made.
     * 
     * @return the selected GXT string; {@code null} if no selection is made
     */
    public String showStringSelectionDialog()
    {
        selectedGXTKey = null;
        setVisible(true);
        return gxtTable.get(selectedGXTKey);
    }
    
    /*
     * Sets dialog properties.
     */
    private void initWindow()
    {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setPreferredSize(new Dimension(450, 350));
        setLayout(new BorderLayout());
        pack();
        setLocationRelativeTo(getParent());
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                 setModal(false);
                 selectedGXTKey = null;
            }
        });
    }
    
    /*
     * Defines components, places them on the dialog, and defines their actions.
     */
    private void initComponents()
    {
        // Define components
        JPanel mainPanel = new JPanel();
        JPanel tablePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        table = new JTable();
        selectButton = new JButton();
        
        // Place table in scroll pane
        scrollPane.setViewportView(table);
        
        // Initialize table
        initTableCellRenderer();
        initTableListSelectionModel();
        initTableMouseListener();
        
        // Set "Select" button proprties
        selectButton.setText("Select");
        selectButton.setEnabled(false);
        selectButton.setPreferredSize(new Dimension(80, 23));
        selectButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                selectButtonAction(e);
            }
        });
        
        // Set up table panel
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        // Set up button panel
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(selectButton, BorderLayout.EAST);
        buttonPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        
        // Place sub-panels onto main panel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to content pane
        getContentPane().add(mainPanel);
    }
    
    /*
     * Sets up the table cell renderer.
     */
    private void initTableCellRenderer()
    {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value,
                    boolean isSelected, boolean hasFocus, 
                    int row, int column)
            {
                // Get component
                JLabel comp = (JLabel)super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                // Set tooltip text to match cell text
                comp.setToolTipText(comp.getText());
                
                return comp;
            }
        };
        
        table.setDefaultRenderer(Object.class, renderer);
    }
    
    /*
     * Sets up the table selection model. This is used to extract the select GXT
     * entry.
     */
    private void initTableListSelectionModel()
    {
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                // Get selected row index
                int tableRowIndex = table.getSelectedRow();
                
                // No row selected, nullify gxt key and disable "Select" button
                if (tableRowIndex == -1) {
                    selectedGXTKey = null;
                    selectButton.setEnabled(false);
                    return;
                }
                
                // Get selected GXT key
                int modelRowIndex
                        = table.convertRowIndexToModel(table.getSelectedRow());
                selectedGXTKey
                        = (String)table.getModel().getValueAt(modelRowIndex, 0);
                
                // Enable "Select" button
                selectButton.setEnabled(true);
            }
        });
    }
    
    /*
     * Sets up the mouse listener for the table.
     */
    private void initTableMouseListener()
    {
        MouseAdapter mouseAdapter = new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                /* Click "Select" button if user double-clicks on an item with
                   the left mouse button */
                if (e.getButton() == MouseEvent.BUTTON1
                        && e.getClickCount() == 2
                        && table.getSelectedRow() != -1) {
                    selectButton.doClick();
                }
            }
        };
        
        table.addMouseListener(mouseAdapter);
    }
    
    /*
     * Fills the table with GXT keys and their respective values.
     */
    private void populateTable()
    {
        // Set up table model
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Key", "Value" },
                gxtTable != null ? gxtTable.size() : 0)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }
        };
        table.setModel(model);
        
        // Set up row sorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        table.setRowSorter(sorter);
        
        // Set width for "Key" column
        table.getColumnModel().getColumn(0).setPreferredWidth(75);
        table.getColumnModel().getColumn(0).setMaxWidth(75);
        table.getColumnModel().getColumn(0).setMinWidth(75);
        
        // End if there is no GXT data
        if (gxtTable == null) {
            return;
        }
        
        // Fill table with GXT keys and values
        int i = 0;
        for (Map.Entry<String, String> entry : gxtTable.entrySet()) {
            JLabel key = new JLabel(entry.getKey());
            key.setToolTipText(entry.getKey());
            JLabel value = new JLabel(entry.getValue());
            value.setToolTipText(entry.getValue());
            model.setValueAt(entry.getKey(), i, 0);
            model.setValueAt(entry.getValue(), i++, 1);
        }
        
        // Sort the table
        sorter.sort();
    }
    
    /*
     * Defines the click action for the "Select" button.
     */
    private void selectButtonAction(ActionEvent e)
    {
        setModal(false);
        dispose();
    }
}
