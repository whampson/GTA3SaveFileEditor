
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
 * Created on Jan 17, 2016.
 *
 * @author thehambone
 */
public final class GXTSelectorDialog extends JDialog
{
    private static final String TITLE_STRING = "GXT String Selector";
    
    private JTable table;
    private JButton selectButton;
    private String selectedGXTKey;
    
    public GXTSelectorDialog(Window parent)
    {
        super(parent, TITLE_STRING);
        selectedGXTKey = null;
        
        initWindow();
        initComponents();
    }
    
    public String showSelectionDialog(Map<String, String> gxtData)
    {
        selectedGXTKey = null;
        
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Key", "Value" },
                gxtData.size())
        {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }
        };
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        
        int i = 0;
        for (Map.Entry<String, String> entry : gxtData.entrySet()) {
            JLabel key = new JLabel(entry.getKey());
            key.setToolTipText(entry.getKey());
            JLabel value = new JLabel(entry.getValue());
            value.setToolTipText(entry.getValue());
            model.setValueAt(entry.getKey(), i, 0);
            model.setValueAt(entry.getValue(), i++, 1);
        }
        
        sorter.sort();
        table.setModel(model);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setPreferredWidth(75);
        table.getColumnModel().getColumn(0).setMaxWidth(75);
        table.getColumnModel().getColumn(0).setMinWidth(75);
        
        setVisible(true);
        
        return selectedGXTKey;
    }
    
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
            }
        });
    }
    
    private void initComponents()
    {
        JPanel mainPanel = new JPanel();
        JPanel tablePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        table = new JTable();
        selectButton = new JButton();
        
        scrollPane.setViewportView(table);
        
        initTableCellRenderer();
        initTableListSelectionModel();
        initTableMouseListener();
        
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
        
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(selectButton, BorderLayout.EAST);
        buttonPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        getContentPane().add(mainPanel);
    }
    
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
                JLabel comp = (JLabel)super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                // Set tooltip text to match cell text
                comp.setToolTipText(comp.getText());
                
                return comp;
            }
        };
        
        table.setDefaultRenderer(Object.class, renderer);
    }
    
    private void initTableListSelectionModel()
    {
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                int rowIndex = table.getSelectedRow();
                if (rowIndex == -1) {
                    selectedGXTKey = null;
                    selectButton.setEnabled(false);
                    return;
                }
                
                int modelIndex
                        = table.convertRowIndexToModel(table.getSelectedRow());
                selectedGXTKey
                        = (String)table.getModel().getValueAt(modelIndex, 0);
                selectButton.setEnabled(true);
            }
        });
    }
    
    private void initTableMouseListener()
    {
        MouseAdapter mouseAdapter = new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1
                        && e.getClickCount() == 2
                        && table.getSelectedRow() != -1) {
                    selectButton.doClick();
                }
            }
        };
        
        table.addMouseListener(mouseAdapter);
    }
    
    private void selectButtonAction(ActionEvent e)
    {
        setModal(false);
        dispose();
    }
}
