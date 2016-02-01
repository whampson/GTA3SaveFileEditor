package thehambone.gtatools.gta3savefileeditor.page;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import thehambone.gtatools.gta3savefileeditor.io.IO;
import thehambone.gtatools.gta3savefileeditor.cellrenderer.SaveSlotCellRenderer;
import thehambone.gtatools.gta3savefileeditor.savefile.PCSaveSlot;
import thehambone.gtatools.gta3savefileeditor.util.GUIUtilities;
import thehambone.gtatools.gta3savefileeditor.util.Logger;
import thehambone.gtatools.gta3savefileeditor.util.ResourceLoader;

/**
 * Created in Mar 30, 2015.
 * 
 * @author thehambone
 */
public class WelcomePage extends Page
{
    private static final String ICON_PATH = "META-INF/res/logo2.png";
    
    public WelcomePage()
    {
        super("Welcome", Visibility.VISIBLE_WHEN_FILE_NOT_LOADED_ONLY);
        
        initComponents();
        initIcon();
        initButtons();
        initListSelectionListener();
        initMouseListener();
    }
    
    private void initIcon()
    {
        try {
            imageLabel.setIcon(new ImageIcon(
                    ResourceLoader.loadImageResource(ICON_PATH)));
        } catch (IOException ex) {
            Logger.error("Failed to load image resource. [%s: %s]\n",
                    ex.getClass().getName(), ex.getMessage());
            Logger.stackTrace(ex);
        }
        imageLabel.setText("");
    }
    
    private void initButtons()
    {
        refreshButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                refresh();
            }
        });
        
        loadButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                loadSelectedSlot();
            }
        });
        
        deleteButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteSelectedSlot();
            }
        });
        
        final JFrame dialogParent
                = (JFrame)SwingUtilities.getWindowAncestor(this);
        browseButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                File f = GUIUtilities.showFileSelectionDialog(dialogParent,
                        "Load File", false);
                if (f != null) {
                    notifyObservers(Page.Event.FILE_LOAD, f);
                }
            }
        });
    }
    
    private void initListSelectionListener()
    {
        saveSlotList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                PCSaveSlot slot = (PCSaveSlot)saveSlotList.getSelectedValue();
                if (slot == null) {
                    return;
                }
                
                loadButton.setEnabled(slot.isUsable());
                deleteButton.setEnabled(slot.isUsable());
            }
        });
    }
    
    private void initMouseListener()
    {
        // Item double-click listener
        saveSlotList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1
                        && e.getClickCount() == 2
                        && !saveSlotList.isSelectionEmpty()) {
                    loadSelectedSlot();
                }
            }
        });
    }
    
    private void loadSelectedSlot()
    {
        PCSaveSlot saveSlot = (PCSaveSlot)saveSlotList.getSelectedValue();
        File f = saveSlot.getFile();
        if (!saveSlot.isUsable()) {
            return;
        }
        
        notifyObservers(Event.FILE_LOAD, f);
    }
    
    private void deleteSelectedSlot()
    {
        PCSaveSlot saveSlot = (PCSaveSlot)saveSlotList.getSelectedValue();
        File f = saveSlot.getFile();
        if (!saveSlot.isUsable()) {
            return;
        }
        
        notifyObservers(Event.FILE_DELETE, f);
        if (!f.exists()) {
            GUIUtilities.showInformationMessageBox(
                    this, "File deleted successfully!", "Success");
            refresh();
        }
    }
    
    private void refresh()
    {
        notifyObservers(Event.REFRESH_SLOTS);
        populateSlotList();
    }
    
    @SuppressWarnings("unchecked")
    private void populateSlotList()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                saveSlotList.clearSelection();
                
                DefaultListModel<PCSaveSlot> saveSlotListModel
                        = new DefaultListModel<>();
                
                for (PCSaveSlot slot : PCSaveSlot.getSaveSlots()) {
                    saveSlotListModel.addElement(slot);
                }
                
                saveSlotList.setModel(saveSlotListModel);
                saveSlotList.setCellRenderer(new SaveSlotCellRenderer());
                saveSlotList.setFont(new Font("Tahoma", Font.PLAIN, 12));

                loadButton.setEnabled(!saveSlotList.isSelectionEmpty());
                deleteButton.setEnabled(!saveSlotList.isSelectionEmpty());

                saveSlotList.repaint();
            }
        });
    }
    
    @Override
    public void loadPage()
    {
        Logger.debug("Loading page: %s...\n", getPageTitle());
        
        refresh();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        scrollPane = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        titlePanel = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();
        loadGamePanel = new javax.swing.JPanel();
        saveSlotScrollPane = new javax.swing.JScrollPane();
        saveSlotList = new javax.swing.JList();
        refreshButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        browseButton = new javax.swing.JButton();

        imageLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.setText("<image>");

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        loadGamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Load Game"));

        saveSlotList.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        saveSlotList.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        saveSlotList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        saveSlotScrollPane.setViewportView(saveSlotList);

        refreshButton.setText("Refresh");
        refreshButton.setToolTipText("");
        refreshButton.setMaximumSize(new java.awt.Dimension(79, 23));
        refreshButton.setMinimumSize(new java.awt.Dimension(79, 23));
        refreshButton.setPreferredSize(new java.awt.Dimension(79, 23));

        loadButton.setText("Load");
        loadButton.setMaximumSize(new java.awt.Dimension(79, 23));
        loadButton.setMinimumSize(new java.awt.Dimension(79, 23));
        loadButton.setPreferredSize(new java.awt.Dimension(79, 23));

        deleteButton.setText("Delete");
        deleteButton.setMaximumSize(new java.awt.Dimension(79, 23));
        deleteButton.setMinimumSize(new java.awt.Dimension(79, 23));
        deleteButton.setPreferredSize(new java.awt.Dimension(79, 23));

        browseButton.setText("Browse...");

        javax.swing.GroupLayout loadGamePanelLayout = new javax.swing.GroupLayout(loadGamePanel);
        loadGamePanel.setLayout(loadGamePanelLayout);
        loadGamePanelLayout.setHorizontalGroup(
            loadGamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadGamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loadGamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveSlotScrollPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loadGamePanelLayout.createSequentialGroup()
                        .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
                        .addComponent(loadButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(browseButton)))
                .addContainerGap())
        );
        loadGamePanelLayout.setVerticalGroup(
            loadGamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadGamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveSlotScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(loadGamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton)
                    .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadGamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loadGamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scrollPane.setViewportView(mainPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JButton loadButton;
    private javax.swing.JPanel loadGamePanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JList saveSlotList;
    private javax.swing.JScrollPane saveSlotScrollPane;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
}
