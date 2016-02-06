package thehambone.gtatools.gta3savefileeditor.page;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import thehambone.gtatools.gta3savefileeditor.Settings;
import thehambone.gtatools.gta3savefileeditor.util.GUIUtilities;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * This pages contains components for modifying the program configuration.
 * <p>
 * Created on Mar 31, 2015.
 * 
 * @author thehambone
 */
public final class OptionsPage extends Page
{
    /**
     * Creates a new {@code OptionsPage} object.
     */
    public OptionsPage()
    {
        super("Options", Visibility.ALWAYS_VISIBLE);
        
        initComponents();
        initSaveDirComponents();
        initBackupComponents();
        initTimestampComponents();
    }
    
    /**
     * Sets up the save file folder text field, browse button, and path status
     * label.
     */
    private void initSaveDirComponents()
    {
        saveFileDirectoryTextField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                JTextField tf = saveFileDirectoryTextField;
                
                if (tf.getText().isEmpty()) {
                    tf.setText(Settings.getDefault(Settings.Key.GTA3_USER_DIR));
                }
                
                Settings.set(Settings.Key.GTA3_USER_DIR, tf.getText());
            }
        });
        
        Document doc = saveFileDirectoryTextField.getDocument();
        doc.addDocumentListener(new DocumentListener()
        {
            private void update()
            {
                String path = saveFileDirectoryTextField.getText();
                
                File f = new File(path);
                String labelText;
                
                if (f.isDirectory()) {
                    labelText = GUIUtilities.formatHTMLString(
                            "<b>Path OK.</b>",
                            GUIUtilities.DEFAULT_TEXT_WIDTH,
                            false,
                            new Color(0, 192, 0));
                } else {
                    labelText = GUIUtilities.formatHTMLString(
                            "<b>Invalid path!</b>",
                            GUIUtilities.DEFAULT_TEXT_WIDTH,
                            false,
                            new Color(192, 0, 0));
                }
                
                pathStatusLabel.setText(labelText);
            }
            
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                update();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                update();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                // Nop
            }
        });
        
        browseButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                File f = selectDirectory();
                if (f != null) {
                    saveFileDirectoryTextField.setText(f.getAbsolutePath());
                    Settings.set(Settings.Key.GTA3_USER_DIR,
                            f.getAbsolutePath());
                }
            }
        });
    }
    
    /**
     * Sets up the backup file check box.
     */
    private void initBackupComponents()
    {
        makeFileBackupCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean selected = makeFileBackupCheckBox.isSelected();
                String value = Boolean.toString(selected);
                Settings.set(Settings.Key.MAKE_BACKUPS, value);
            }
        });
    }
    
    /**
     * Sets up the timestamp check box.
     */
    private void initTimestampComponents()
    {
        updateTimestampCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean selected = updateTimestampCheckBox.isSelected();
                String value = Boolean.toString(selected);
                Settings.set(Settings.Key.TIMESTAMP_FILES, value);
            }
        });
    }
    
    /**
     * Displays a JFileChooser configured for selecting directories.
     * 
     * @return the selected directory, {@code null} if the user cancelled
     */
    private File selectDirectory()
    {
        return GUIUtilities.showDirectorySelectionDialog(
                (JFrame)SwingUtilities.getWindowAncestor(this),
                "Browse for GTA3 User Files");
    }
    
    @Override
    public void loadPage()
    {
        Logger.debug("Loading page: %s...\n", getPageTitle());
        
        String gta3UserDir = Settings.get(Settings.Key.GTA3_USER_DIR);
        String makeBackups = Settings.get(Settings.Key.MAKE_BACKUPS);
        String tstampFiles = Settings.get(Settings.Key.TIMESTAMP_FILES);
        
        saveFileDirectoryTextField.setText(gta3UserDir);
        makeFileBackupCheckBox.setSelected(Boolean.parseBoolean(makeBackups));
        updateTimestampCheckBox.setSelected(Boolean.parseBoolean(tstampFiles));
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
        saveFileDirectoryPanel = new javax.swing.JPanel();
        saveFileDirectoryTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        pathStatusLabel = new javax.swing.JLabel();
        fileBackupsPanel = new javax.swing.JPanel();
        makeFileBackupCheckBox = new javax.swing.JCheckBox();
        timestampPanel = new javax.swing.JPanel();
        updateTimestampCheckBox = new javax.swing.JCheckBox();

        saveFileDirectoryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Save File Directory"));

        saveFileDirectoryTextField.setToolTipText("Path to the \"GTA3 User Files\" folder.");

        browseButton.setText("Browse...");

        pathStatusLabel.setText("<path status>");

        javax.swing.GroupLayout saveFileDirectoryPanelLayout = new javax.swing.GroupLayout(saveFileDirectoryPanel);
        saveFileDirectoryPanel.setLayout(saveFileDirectoryPanelLayout);
        saveFileDirectoryPanelLayout.setHorizontalGroup(
            saveFileDirectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveFileDirectoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(saveFileDirectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(saveFileDirectoryPanelLayout.createSequentialGroup()
                        .addComponent(saveFileDirectoryTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseButton))
                    .addGroup(saveFileDirectoryPanelLayout.createSequentialGroup()
                        .addComponent(pathStatusLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        saveFileDirectoryPanelLayout.setVerticalGroup(
            saveFileDirectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveFileDirectoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(saveFileDirectoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveFileDirectoryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pathStatusLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fileBackupsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("File Backups"));

        makeFileBackupCheckBox.setText("Make a backup when file is loaded");
        makeFileBackupCheckBox.setToolTipText("Backup files have the \".bak\" extension and appear in the same folder as the source file.");

        javax.swing.GroupLayout fileBackupsPanelLayout = new javax.swing.GroupLayout(fileBackupsPanel);
        fileBackupsPanel.setLayout(fileBackupsPanelLayout);
        fileBackupsPanelLayout.setHorizontalGroup(
            fileBackupsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileBackupsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(makeFileBackupCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        fileBackupsPanelLayout.setVerticalGroup(
            fileBackupsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileBackupsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(makeFileBackupCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        timestampPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Timestamp"));

        updateTimestampCheckBox.setText("Update timestamp when file is saved (PC saves only)");
        updateTimestampCheckBox.setToolTipText("The timestamp that shows up on the in-game Load menu will be updated to reflect the time that the file was saved.");

        javax.swing.GroupLayout timestampPanelLayout = new javax.swing.GroupLayout(timestampPanel);
        timestampPanel.setLayout(timestampPanelLayout);
        timestampPanelLayout.setHorizontalGroup(
            timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timestampPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateTimestampCheckBox)
                .addContainerGap(283, Short.MAX_VALUE))
        );
        timestampPanelLayout.setVerticalGroup(
            timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timestampPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateTimestampCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveFileDirectoryPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fileBackupsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(timestampPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveFileDirectoryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileBackupsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timestampPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private javax.swing.JPanel fileBackupsPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JCheckBox makeFileBackupCheckBox;
    private javax.swing.JLabel pathStatusLabel;
    private javax.swing.JPanel saveFileDirectoryPanel;
    private javax.swing.JTextField saveFileDirectoryTextField;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel timestampPanel;
    private javax.swing.JCheckBox updateTimestampCheckBox;
    // End of variables declaration//GEN-END:variables
}
