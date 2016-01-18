package thehambone.gtatools.gta3savefileeditor.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import thehambone.gtatools.gta3savefileeditor.Main;
import thehambone.gtatools.gta3savefileeditor.gui.component.VariableComponent;
import thehambone.gtatools.gta3savefileeditor.savefile.PCSaveSlot;
import thehambone.gtatools.gta3savefileeditor.gui.observe.Observable;
import thehambone.gtatools.gta3savefileeditor.gui.observe.Observer;
import thehambone.gtatools.gta3savefileeditor.gui.page.DebugPage;
import thehambone.gtatools.gta3savefileeditor.gui.page.GangsPage;
import thehambone.gtatools.gta3savefileeditor.gui.page.GaragesPage;
import thehambone.gtatools.gta3savefileeditor.gui.page.GeneralPage;
import thehambone.gtatools.gta3savefileeditor.gui.page.OptionsPage;
import thehambone.gtatools.gta3savefileeditor.gui.page.Page;
import thehambone.gtatools.gta3savefileeditor.gui.page.PlayerPage;
import thehambone.gtatools.gta3savefileeditor.gui.page.WelcomePage;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * The program's main window frame.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 24, 2015
 */
public class EditorWindow extends JFrame implements Observer
{
    private final Page[] pages = new Page[] {
        new WelcomePage(),
        new GeneralPage(),
        new PlayerPage(),
        new GaragesPage(),
        new GangsPage(),
        new OptionsPage(),
        new DebugPage()
    };   
    private final List<Observable> subjects = new ArrayList<>();
    
    private JMenu slotLoadMenu;
    private JMenu slotSaveMenu;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem closeFileMenuItem;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;
    private boolean changesMade = false;
    
    public EditorWindow()
    {
        initComponents();
        initObservers();
        refreshMenus();
        refreshPages();
        changesMade = false;
        setStatus("Welcome to the GTA III Save File Editor!");
    }
    
    private void initComponents()
    {
        initWindowListeners();
        initMenus();
        initPanels();
    }
    
    private void initWindowListeners()
    {
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent evt)
            {
                closeFrame();
            }
        });
    }
    
    private void initMenus()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load...");
        slotLoadMenu = new JMenu("Load Slot");
        saveMenuItem = new JMenuItem("Save");
        saveAsMenuItem = new JMenuItem("Save As...");
        slotSaveMenu = new JMenu("Save Slot");
        closeFileMenuItem = new JMenuItem("Close File");
        JMenuItem refreshSlotsMenuItem = new JMenuItem("Refresh Slots");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem checkForUpdatesMenuItem = new JMenuItem("Check for Updates");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        
        JMenu debugMenu = new JMenu("Debug");
        JMenuItem causeRuntimeExceptionMenuItem = new JMenuItem("Cause RuntimeException");
        
        fileMenu.add(loadMenuItem);
        fileMenu.add(slotLoadMenu);
        fileMenu.add(new JPopupMenu.Separator());
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(slotSaveMenu);
        fileMenu.add(new JPopupMenu.Separator());
        fileMenu.add(closeFileMenuItem);
        fileMenu.add(new JPopupMenu.Separator());
        fileMenu.add(refreshSlotsMenuItem);
        fileMenu.add(new JPopupMenu.Separator());
        fileMenu.add(exitMenuItem);
        
        helpMenu.add(checkForUpdatesMenuItem);
        helpMenu.add(new JPopupMenu.Separator());
        helpMenu.add(aboutMenuItem);
        
        debugMenu.add(causeRuntimeExceptionMenuItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        if (Main.isDebugModeEnabled()) {
            menuBar.add(debugMenu);
        }
        
        setJMenuBar(menuBar);
        
        loadMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (SaveFileNew.isFileLoaded()) {
                    if (!promptSaveChanges()) {
                        return;
                    }
                }
                File f = promptForFile("Load");
                if (f != null) {
                    loadFile(f);
                }
            }
        });
        saveMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!SaveFileNew.isFileLoaded()) {
                    return;
                }
                
                // TODO: change this
                File f = SaveFileNew.getCurrentSaveFile().getSourceFile();
                saveFile(f);
            }
        });
        saveAsMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!SaveFileNew.isFileLoaded()) {
                    return;
                }
                File f = promptForFile("Save");
                if (f != null) {
                    saveFile(f);
                }
            }
        });
        closeFileMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (SaveFileNew.isFileLoaded()) {
                    if (!promptSaveChanges()) {
                        return;
                    }
                    closeFile();
                }
            }
        });
        refreshSlotsMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               refreshSlotMenus();
                setStatus("Save slots refreshed.");
            }
        });
        exitMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                closeFrame();
            }
        });
        checkForUpdatesMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    Desktop.getDesktop().browse(new URI(Main.PROGRAM_UPDATE_URL));
                } catch (URISyntaxException | IOException ex) {
                    Logger.error("Failed to open URI.");
                    Logger.stackTrace(ex);
                }
            }
        });
        final Frame parent = this;
        aboutMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                AboutDialog ad = new AboutDialog(parent);
                ad.pack();
                ad.setLocationRelativeTo(parent);
                ad.setVisible(true);
            }
        });
        causeRuntimeExceptionMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                throw new RuntimeException("This a fake exception.");   // For testing
            }
        });
        
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        closeFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        refreshSlotsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    }
    
    private void initPanels()
    {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("<status>");
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void initObservers()
    {
        for (Page p : pages) {
            p.addObserver(this);
        }
    }
    
    private void refreshMenus()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                saveMenuItem.setEnabled(SaveFileNew.isFileLoaded());
                saveAsMenuItem.setEnabled(SaveFileNew.isFileLoaded());
                slotSaveMenu.setEnabled(SaveFileNew.isFileLoaded());
                closeFileMenuItem.setEnabled(SaveFileNew.isFileLoaded());
            }
        });
        refreshSlotMenus();
    }
    
    private void refreshSlotMenus()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                slotLoadMenu.removeAll();
                slotSaveMenu.removeAll();
                JMenuItem[] load = generateSlotMenuItems(SlotMenuItemAction.LOAD);
                JMenuItem[] save = generateSlotMenuItems(SlotMenuItemAction.SAVE);
                for (JMenuItem item : load) {
                    slotLoadMenu.add(item);
                }
                for (JMenuItem item : save) {
                    slotSaveMenu.add(item);
                }
            }
        });
    }
    
    private JMenuItem[] generateSlotMenuItems(final SlotMenuItemAction itemAction)
    {
        PCSaveSlot[] slots = PCSaveSlot.getSaveSlots();
        JMenuItem[] slotMenuItems = new JMenuItem[slots.length];
        for (int i = 0; i < slots.length; i++) {
            final PCSaveSlot slot = slots[i];
            JMenuItem slotMenuItem = new JMenuItem();
            slotMenuItems[i] = slotMenuItem;
            try {
                slot.refresh();
            } catch (IOException ex) {
                slotMenuItem.setText((i + 1) + ". <slot is corrupt>");
                continue;
            }
            if (slot.isEmpty()) {
                slotMenuItem.setText((i + 1) + ". <slot is empty>");
            } else {
                slotMenuItem.setText((i + 1) + ". " + slot.getSaveTitle());
            }
            if (itemAction == SlotMenuItemAction.LOAD) {
                slotMenuItem.setAccelerator(KeyStroke.getKeyStroke(49 + i, InputEvent.CTRL_DOWN_MASK));
            } else {
                slotMenuItem.setAccelerator(KeyStroke.getKeyStroke(49 + i, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
            }
            slotMenuItem.setEnabled(!slot.isEmpty() || itemAction != SlotMenuItemAction.LOAD);
            final Frame dialogParent = this;
            slotMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    File f = slot.getSaveFile();
                    switch (itemAction) {
                        case LOAD:
                            if (SaveFileNew.isFileLoaded()) {
                                if (!promptSaveChanges()) {
                                    return;
                                }
                            }
                            loadFile(f);
                            break;
                        case SAVE:
                            if (!SaveFileNew.isFileLoaded()) {
                                break;
                            }
                            File currentlyLoadedFile = SaveFileNew.getCurrentSaveFile().getSourceFile();
                            if (!currentlyLoadedFile.getAbsolutePath().equals(f.getAbsolutePath())
                                    && f.exists()) {
                                int option = JOptionPane.showOptionDialog(dialogParent,
                                        GUIUtils.formatHTMLString("Are you sure you want to overwrite \"" + f.getName() + "\"?"),
                                        "Overwrite Confirmation",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        null,
                                        null);
                                if (option != JOptionPane.YES_OPTION) {
                                    break;
                                }
                            }
                            saveFile(f);
                            break;
                    }
                }
            });
        }
        return slotMenuItems;
    }
    
    private void refreshPages()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                boolean addPage;
                tabbedPane.removeAll();
                for (Page p : pages) {
                    addPage = false;
                    if (p instanceof DebugPage && !Main.isDebugModeEnabled()) {
                        continue;
                    }
                    switch (p.getVisibility()) {
                        case ALWAYS_VISIBLE:
                            addPage = true;
                            break;
                        case VISIBLE_WHEN_FILE_LOADED_ONLY:
                            if (SaveFileNew.isFileLoaded()) {
                                addPage = true;
                            }
                            break;
                        case VISIBLE_WHEN_FILE_NOT_LOADED_ONLY:
                            if (!SaveFileNew.isFileLoaded()) {
                                addPage = true;
                            }
                            break;
                    }
                    if (addPage) {
                        p.loadPage();
                        tabbedPane.addTab(p.getPageTitle(), p);
                        pack();
                    }
                }
            }
        });
        pack();
    }
    
    private void closeFrame()
    {   
        if ((SaveFileNew.isFileLoaded() && promptSaveChanges()) || !SaveFileNew.isFileLoaded()) {
            Logger.info("Closing user interface...");
            dispose();
        } else {
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);   // Prevents window from closing
        }
    }
    
    private void updateFrameTitle()
    {
        StringBuilder titleBuilder = new StringBuilder();
        if (changesMade) {
            titleBuilder.append("*");
        }
        if (SaveFileNew.isFileLoaded()) {
            titleBuilder.append(SaveFileNew.getCurrentSaveFile().getSourceFile()).append(" - ");
        }
        titleBuilder.append(String.format("%s %s", Main.PROGRAM_TITLE, Main.PROGRAM_VERSION));
        setTitle(titleBuilder.toString());
    }
    
    private void setStatus(final String s)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                statusLabel.setText(s);
            }
        });
    }
    
    private void closeFile()
    {
        Logger.info("Closing file...");
        File f = SaveFileNew.getCurrentSaveFile().getSourceFile();
        SaveFileNew.close();
        Logger.info("Successfully closed file: %s\n", f);
        setStatus(String.format("Closed file: %s", f));
        refreshMenus();
        refreshPages();
        changesMade = false;
        updateFrameTitle();
    }
    
    private void loadFile(File f)
    {
        try {
            Logger.info("Loading file...");
            SaveFileNew.load(f);
            Logger.info("Successfully loaded file: %s\n", f);
            setStatus(String.format("Loaded file: %s", f));
            JOptionPane.showMessageDialog(this, "File loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshMenus();
            refreshPages();
            changesMade = false;
            updateFrameTitle();
        } catch (IOException ex) {
            String errMsg = "An error occured while loading the file.";
            Logger.error(errMsg);
            Logger.stackTrace(ex);
            showErrorMessage(errMsg, "Error Loading File", ex);
        } catch (UnsupportedPlatformException ex) {
            String errMsg = "This save file originates from a platform which is not currently supported by this editor.";
            Logger.error(errMsg);
            Logger.stackTrace(ex);
            showErrorMessage(errMsg, "Unsupported Platform", ex);
        }
    }
    
    private void saveFile(File f)
    {
        try {
            Logger.info("Saving file...");
            SaveFileNew.getCurrentSaveFile().save(f);
            changesMade = false;
            Logger.info("Successfully saved file: %s\n", f);
            setStatus(String.format("Saved file: %s", f));
            updateFrameTitle();
            refreshSlotMenus();
//            pages[1].loadPage();
        } catch (IOException ex) {
            String errMsg = "An error occured while attempting to save the file.";
            Logger.error(errMsg);
            Logger.stackTrace(ex);
            showErrorMessage(errMsg, "Error Saving File", ex);
        }
    }
    
    private void deleteFile(File f)
{
        int option = JOptionPane.showOptionDialog(this,
                "Are you sure you want to delete \"" + f.getName() + "\"?",
                "Confirm File Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if (option != JOptionPane.YES_OPTION || !f.exists()) {
            return;
        }
        f.delete();
        Logger.info("Deleted file: %s\n", f);
        setStatus(String.format("Deleted file: %s", f));
        refreshSlotMenus();
    }
    
    private File promptForFile(String dialogText)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(dialogText);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("GTA III-era Save Files", "b"));
        int option = fileChooser.showDialog(this, dialogText);
        if (option != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return fileChooser.getSelectedFile();
    }
    
    /**
     * Asks the user if they want to save the changes made to the file. If the
     * user selects "yes", the file will be saved.
     * 
     * @return A boolean value indicating whether or not the calling process
     *         should continue. True if "yes" or "no" selected, false otherwise.
     */
    private boolean promptSaveChanges()
    {
        if (!changesMade)
        {
            return true;
        }
        int option = JOptionPane.showOptionDialog(this,
                GUIUtils.formatHTMLString("Do you want to save the changes made to this file?"),
                "Save Changes?",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if (option == JOptionPane.YES_OPTION) {
            File f = SaveFileNew.getCurrentSaveFile().getSourceFile();
            saveFile(f);
        }
        return option == JOptionPane.YES_OPTION || option == JOptionPane.NO_OPTION;
    }
    
    private void showErrorMessage(String s) {
        showErrorMessage(s, "Error", null);
    }
    
    private void showErrorMessage(String message, String title, Throwable t) {
        GUIUtils.showErrorMessage(this, message, title, t);
    }

    @Override
    public void update(Object message, Object... args)
    {
        if (!(message instanceof Page.Event)) {
            return;
        }
        
        Page.Event e = (Page.Event)message;
        Logger.debug("Page event: " + e);
        
        switch (e) {
            case VARIABLE_CHANGED:
                if (!changesMade) {
                    changesMade = true;
                    updateFrameTitle();
                }
                break;
            case VARIABLE_UNCHANGED:
                if (!changesMade) {
                    break;
                }
                
                boolean changesReverted = true;
                for (Page p : pages) {
                    List<VariableComponent> comps = getAllVariableComponents(p);
                    for (VariableComponent c : comps) {
                        if (c.hasVariable() && c.getVariable().dataChanged()) {
                            changesReverted = false;
                            break;
                        }
                    }
                    changesMade = !changesReverted;
                    updateFrameTitle();
                }
                break;
            case FILE_LOAD:
                File fileToLoad = (File)args[0];    // TODO: unsafe!!
                if (fileToLoad == null) {
                    return;
                }
                loadFile(fileToLoad);
                break;
            case FILE_DELETE:
                File fileToDelete = (File)args[0];  // TODO: unsafe!!
                if (fileToDelete == null) {
                    return;
                }
                deleteFile(fileToDelete);
                break;
            case REFRESH_SLOTS:
                refreshSlotMenus();
                break;
        }
    }
    
    public List<VariableComponent> getAllVariableComponents(Container parent)
    {
        List<VariableComponent> comps = new ArrayList<>();
        for (Component c : parent.getComponents()) {
            if (c instanceof Container) {
                comps.addAll(getAllVariableComponents((Container)c));
            }
            if (c instanceof VariableComponent) {
                comps.add((VariableComponent)c);
            }
        }
        return comps;
    }
    
    private static enum SlotMenuItemAction
    {
        LOAD, SAVE;
    }
}
