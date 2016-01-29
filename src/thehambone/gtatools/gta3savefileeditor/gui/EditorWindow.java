package thehambone.gtatools.gta3savefileeditor.gui;

import thehambone.gtatools.gta3savefileeditor.util.GUIUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
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
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import thehambone.gtatools.gta3savefileeditor.Main;
import thehambone.gtatools.gta3savefileeditor.Settings;
import thehambone.gtatools.gta3savefileeditor.gui.observe.Observer;
import thehambone.gtatools.gta3savefileeditor.gui.page.GangsPage;
import thehambone.gtatools.gta3savefileeditor.gui.page.GaragesPage;
import thehambone.gtatools.gta3savefileeditor.gui.page.GeneralPage;
import thehambone.gtatools.gta3savefileeditor.gui.page.OptionsPage;
import thehambone.gtatools.gta3savefileeditor.gui.page.Page;
import thehambone.gtatools.gta3savefileeditor.gui.page.PlayerPage;
import thehambone.gtatools.gta3savefileeditor.gui.page.WelcomePage;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.PCSaveSlot;
import thehambone.gtatools.gta3savefileeditor.util.FixedLengthQueue;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * The program's main window frame.
 * <p>
 * Created on Mar 24, 2015.
 * 
 * @author thehambone
 */
public class EditorWindow extends JFrame implements Observer
{
    private final Page[] pages = new Page[] {
        new WelcomePage(),
        new GeneralPage(),
        new PlayerPage(),
        new GaragesPage(),
        new GangsPage(),
        new OptionsPage()
    };
    
    private FixedLengthQueue<String> recentFiles;
    private JMenu loadSlotMenu;
    private JMenu loadRecentMenu;
    private JMenu saveSlotMenu;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem closeFileMenuItem;
    private JMenuItem refreshMenuItem;
    private JTabbedPane tabbedPane;
    private JLabel statusMessageLabel;
    private JLabel modificationStatusLabel;
    private JLabel platformStatusLabel;
    private boolean changesMade;
    private int crc;
    
    /**
     * Creates a new {@code EditorWindow} instance.
     */
    public EditorWindow()
    {
        initWindowListener();
        initMenuBar();
        loadRecentFilesList();
        initTabbedPane();
        initPanels();
        initObservers();
        refreshMenus();
        refreshPages();
        setChangesMade(false);
        setStatusMessage("Welcome to the GTA III Save File Editor!");
    }
    
    /**
     * Sets up the window close and window focus events for the frame.
     */
    private void initWindowListener()
    {
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent evt)
            {
                closeFrame();
            }
            
            @Override
            public void windowActivated(WindowEvent evt)
            {
                /* This event will trigger whenever the frame gains focus, which
                   can occur after closing a modal dialog. We only want this
                   code to run when the entire program gains focus. This can be
                   checked by testing whether the opposite window is null. */
                if (evt.getOppositeWindow() != null) {
                    return;
                }
                Logger.debug("Program gained focus");
                
                refreshSlotMenus();
                
                if (SaveFile.isFileLoaded()) {
                    checkForExternalChanges();
                }
            }
            
            @Override
            public void windowDeactivated(WindowEvent evt)
            {
                /* This event will trigger whenever the frame loses focus, which
                   can occur after activating a modal dialog. We only want this
                   code to run when the entire program loses focus. This can be
                   checked by testing whether the opposite window is null. */
                if (evt.getOppositeWindow() != null) {
                    return;
                }
                
                // Calculate CRC on current data
            }
        });
    }
    
    /**
     * Prepares the menu bar with all of its submenus.
     */
    private void initMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        initFileMenu(menuBar);
        if (Main.isDebugModeEnabled()) {
            initDebugMenu(menuBar);
        }
        initHelpMenu(menuBar);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Sets up the "File" menu with all of its submenus and menu item actions.
     */
    private void initFileMenu(JMenuBar menuBar)
    {
        // Menu item definitions
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem loadMenuItem = new JMenuItem("Load File...");
        loadSlotMenu = new JMenu("Load Slot");
        loadRecentMenu = new JMenu("Load Recent");
        
        saveMenuItem  = new JMenuItem("Save");
        saveAsMenuItem = new JMenuItem("Save As...");
        saveSlotMenu = new JMenu("Save Slot");
        
        closeFileMenuItem = new JMenuItem("Close File");
        
        refreshMenuItem = new JMenuItem("Refresh");
        JMenuItem refreshSlotsMenuItem = new JMenuItem("Refresh Save Slots");
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        
        // Add menu items to File menu
        fileMenu.add(loadMenuItem);
        fileMenu.add(loadSlotMenu);
        fileMenu.add(loadRecentMenu);
        fileMenu.add(new JPopupMenu.Separator());
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(saveSlotMenu);
        fileMenu.add(new JPopupMenu.Separator());
        fileMenu.add(closeFileMenuItem);
        fileMenu.add(new JPopupMenu.Separator());
        fileMenu.add(refreshMenuItem);
        fileMenu.add(refreshSlotsMenuItem);
        fileMenu.add(new JPopupMenu.Separator());
        fileMenu.add(exitMenuItem);
        
        // Add menu to menu bar
        menuBar.add(fileMenu);
        
        // Define keyboard mnemonics and keystrokes
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        loadMenuItem.setMnemonic(KeyEvent.VK_L);
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        loadSlotMenu.setMnemonic(KeyEvent.VK_O);
        loadRecentMenu.setMnemonic(KeyEvent.VK_E);
        
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S,
                KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        saveSlotMenu.setMnemonic(KeyEvent.VK_V);
        
        closeFileMenuItem.setMnemonic(KeyEvent.VK_C);
        closeFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        
        refreshMenuItem.setMnemonic(KeyEvent.VK_R);
        refreshMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        refreshSlotsMenuItem.setMnemonic(KeyEvent.VK_F);
        refreshSlotsMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F5, KeyEvent.SHIFT_DOWN_MASK));
        
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        
        // Define "Load" action
        loadMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                loadFile();
            }
        });
        
        // Define "Save" action
        saveMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!SaveFile.isFileLoaded()) {
                    return;
                }
                
                saveFile();
            }
        });
        
        // Define "Save As" action
        saveAsMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!SaveFile.isFileLoaded()) {
                    return;
                }
                
                File f = promptForFile("Save As", "Save");
                if (f == null) {
                    return;
                }
                
                saveFile(f);
            }
        });
        
        // Define "Close File" action
        closeFileMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                closeFile();
            }
        });
        
        // Define "Refresh" action
        refreshMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!SaveFile.isFileLoaded() || !promptSaveChanges()) {
                    return;
                }
                
                try {
                    SaveFile.load(SaveFile.getCurrentSaveFile().getSourceFile());
                    changesMade = false;
                    updateFrameTitleAndModificationStatus();
                    // TODO: prompt save as
                } catch (IOException ex) {
                    Logger.stackTrace(ex);
                    // TODO: message
                }
                
                refreshPages();
                setStatusMessage("Current file has been re-loaded.");
            }
        });
        
        // Define "Refresh Slots" action
        refreshSlotsMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                refreshSlotMenus();
                setStatusMessage("Save slots have been refreshed.");
            }
        });
        
        // Define "Exit" action
        exitMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                closeFrame();
            }
        });
    }
    
    /**
     * Sets up the "Debug" menu and defines its menu item actions. 
     */
    private void initDebugMenu(JMenuBar menuBar)
    {
        // Menu item definitions
        JMenu debugMenu = new JMenu("Debug");
        JMenuItem runtimeExceptionMenuItem
                = new JMenuItem("Cause RuntimeException");
        
        // Add menu items to Debug menu
        debugMenu.add(runtimeExceptionMenuItem);
        
        // Add menu to menu bar
        menuBar.add(debugMenu);
        
        // Set keyboard mnemonics
        debugMenu.setMnemonic(KeyEvent.VK_D);
        runtimeExceptionMenuItem.setMnemonic(KeyEvent.VK_R);
        
        // Define "Cause RuntimeException" action
        runtimeExceptionMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                throw new RuntimeException(
                        "This is a fake exception for testing purposes.");
            }
        });
    }
    
    /**
     * Sets up the "Help" menu and defines its menu item actions. 
     */
    private void initHelpMenu(JMenuBar menuBar)
    {
        // Menu item definitions
        JMenu helpMenu = new JMenu("Help");
        
        JMenuItem checkForUpdatesMenuItem = new JMenuItem("Check for Updates");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        
        // Add menu items to Help menu
        helpMenu.add(checkForUpdatesMenuItem);
        helpMenu.add(new JPopupMenu.Separator());
        helpMenu.add(aboutMenuItem);
        
        // Add menu to menu bar
        menuBar.add(helpMenu);
        
        // Set keyboard mnemonics and keystrokes
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        checkForUpdatesMenuItem.setMnemonic(KeyEvent.VK_U);
        aboutMenuItem.setMnemonic(KeyEvent.VK_A);
        aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        
        // Define "Check for Updates" action
        checkForUpdatesMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Open update URL in default Internet browser
                try {
                    URI uri = new URI(Main.PROGRAM_UPDATE_URL);
                    Desktop.getDesktop().browse(uri);
                } catch (IOException | URISyntaxException ex) {
                    Logger.stackTrace(ex);
                    // TODO: message
                }
            }
        });
        
        // Define "About" action
        final Frame parentFrame = this;
        aboutMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Show "About" dialog
                AboutDialog ad = new AboutDialog(parentFrame);
                ad.pack();
                ad.setLocationRelativeTo(parentFrame);
                ad.setVisible(true);
            }
        });
    }
    
    /**
     * Populates the recent files queue from the paths stored in the settings
     * file.
     */
    private void loadRecentFilesList()
    {
        recentFiles = Settings.getRecentFiles();
    }
    
    /**
     * Defines the tabbed pane and initializes the tab change listener.
     */
    private void initTabbedPane()
    {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        tabbedPane.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                /* Reload pages when they are switched to to ensure each
                   component is up to date */
                
                Component c = tabbedPane.getSelectedComponent();
                if (c == null) {
                    return;
                }
                
                Page p = (Page)c;
                p.loadPage();
            }
        });
    }
    
    /**
     * Sets up the main panel and status panel
     */
    private void initPanels()
    {
        setLayout(new BorderLayout());
        
        // Background panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        add(mainPanel);
        
        // Panel at bottom of window for program status
        JPanel statusPanel = new JPanel(new BorderLayout());
        
        // Set up status labels
        Dimension statusLabelSize = new Dimension(50, 14);
        
        statusMessageLabel = new JLabel();
        
        modificationStatusLabel = new JLabel();
        modificationStatusLabel.setMinimumSize(statusLabelSize);
        modificationStatusLabel.setMaximumSize(statusLabelSize);
        modificationStatusLabel.setPreferredSize(statusLabelSize);
        modificationStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        platformStatusLabel = new JLabel();
        platformStatusLabel.setMinimumSize(statusLabelSize);
        platformStatusLabel.setMaximumSize(statusLabelSize);
        platformStatusLabel.setPreferredSize(statusLabelSize);
        platformStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Place status labels on status panel
        Box box = Box.createHorizontalBox();
        box.setOpaque(true);
        box.add(new JSeparator(SwingConstants.VERTICAL));
        box.add(Box.createHorizontalStrut(5));
        box.add(modificationStatusLabel);
        box.add(Box.createHorizontalStrut(5));
        box.add(new JSeparator(SwingConstants.VERTICAL));
        box.add(Box.createHorizontalStrut(5));
        box.add(platformStatusLabel);
        statusPanel.add(box, BorderLayout.EAST);
        statusPanel.add(statusMessageLabel, BorderLayout.WEST);
        
        // Place tabbed pane and status panel on main panel
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Marks the frame as an observer to all pages. This allows the frame to
     * serve as an action listener for event notifications emitted by the pages
     * on the tabbed pane.
     */
    private void initObservers()
    {
        for (Page p : pages) {
            p.addObserver(this);
        }
    }
    
    /**
     * Marks certain menu items enabled or disabled based on whether a file is
     * loaded, and also refreshes the save slot menu items.
     */
    private void refreshMenus()
    {
        final boolean isFileLoaded = SaveFile.isFileLoaded();
        final boolean isPC;
        
        if (isFileLoaded) {
            SaveFile.Platform p = SaveFile.getCurrentSaveFile().getPlatform();
            isPC = p == SaveFile.Platform.PC;
        } else {
            isPC = false;
        }
        
        /* Must be run in an "invokeLater()" thread since the GUI is updated and
           the method can be invoked at any time during the program's life */
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                saveMenuItem.setEnabled(isFileLoaded);
                saveAsMenuItem.setEnabled(isFileLoaded);
                saveSlotMenu.setEnabled(isFileLoaded && isPC);
                closeFileMenuItem.setEnabled(isFileLoaded);
                refreshMenuItem.setEnabled(isFileLoaded);
            }
        });
        
        refreshSlotMenus();
        refreshRecentFilesMenu();
    }
    
    /**
     * Re-loads the save slot menu items.
     */
    private void refreshSlotMenus()
    {
        /* Must be run in an "invokeLater()" thread since the GUI is updated and
           the method can be invoked at any time during the program's life */
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                // Remove all items
                loadSlotMenu.removeAll();
                saveSlotMenu.removeAll();
                
                // Generate new items by reading save dir
                JMenuItem[] load
                        = generateSlotMenuItems(SlotMenuItemAction.LOAD);
                JMenuItem[] save
                        = generateSlotMenuItems(SlotMenuItemAction.SAVE);
                
                // Populate menus with new items
                for (JMenuItem item : load) {
                    loadSlotMenu.add(item);
                }
                for (JMenuItem item : save) {
                    saveSlotMenu.add(item);
                }
                
                Logger.debug("Save slots refreshed");
            }
        });
    }
    
    /**
     * Re-loads the recent files menu.
     */
    public void refreshRecentFilesMenu()
    {
        loadRecentMenu.removeAll();
        
        if (recentFiles.isEmpty()) {
            JMenuItem menuItem = new JMenuItem("(no recent files)");
            menuItem.setEnabled(false);
            loadRecentMenu.add(menuItem);
            return;
        }
        
        final JFrame parentFrame = this;
        
        int numItems = 0;
        Iterator<String> it;
        for (it = recentFiles.reverseOrderIterator(); it.hasNext();) {
            final String path = it.next();
            String menuText = (++numItems) + ": " + path;
            
            JMenuItem menuItem = new JMenuItem(menuText);
            loadRecentMenu.add(menuItem);
            
            // Add CTRL+SHIFT+R keystroke to first item in menu
            if (numItems == 1) {
                menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                        KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
            }
            
            // Define menu item action
            menuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    File f = new File(path);
                    if (f.exists()) {
                        loadFile(f);
                    } else {
                        GUIUtils.showErrorMessageBox(parentFrame,
                                "File not found - " + f, "File Not Found");
                    }
                }
            });
        }
    }
    
    /**
     * Creates an array of JMenuItems each representing a save slot. The menu
     * item text shows the slot index and the save file name. If the slot is not
     * readable, the menu item will be disabled.
     */
    private JMenuItem[] generateSlotMenuItems(final SlotMenuItemAction action)
    {
        PCSaveSlot[] slots = PCSaveSlot.getSaveSlots();
        JMenuItem[] slotMenuItems = new JMenuItem[slots.length];
        
        for (int i = 0; i < slots.length; i++) {
            // Get save slot
            final PCSaveSlot slot = slots[i];
            
            // Make new menu item
            JMenuItem slotMenuItem = new JMenuItem();
            slotMenuItems[i] = slotMenuItem;
            
            // Set menu item mnemonic key equal to the slot index
            slotMenuItem.setMnemonic(KeyEvent.VK_1 + i);
            
            // Refresh the slot
            try {
                slot.refresh();
            } catch (IOException ex) {
                Logger.stackTrace(ex);
                slotMenuItem.setText((i + 1)
                        + ". (error reading file - check GTA3 save dir)");
                slotMenuItem.setEnabled(false);
                continue;
            }
            
            // Set menu item text and availability based on slot status
            if (!slot.isUsable()) {
                slotMenuItem.setText((i + 1)
                        + ". (slot is not usable - check file)");
                slotMenuItem.setEnabled(false);
            } else if (slot.isEmpty()) {
                slotMenuItem.setText((i + 1) + ". (slot is empty)");
                slotMenuItem.setEnabled(false);
            } else {
                slotMenuItem.setText((i + 1) + ". " + slot.getSaveName());
            }
            
            // Set keystrokes based on menu item action
            if (action == SlotMenuItemAction.LOAD) {
                // CTRL + <slot_index>
                slotMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_1 + i, InputEvent.CTRL_DOWN_MASK));
            } else if (action == SlotMenuItemAction.SAVE) {
                // CTRL + SHIFT + <slot_index>
                slotMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_1 + i,
                        InputEvent.CTRL_DOWN_MASK
                                | InputEvent.SHIFT_DOWN_MASK));
            }
            
            // Define menu item action
            final Frame dialogParent = this;
            if (action == SlotMenuItemAction.LOAD) {
                slotMenuItem.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        File f = slot.getFile();
                        loadFile(f);
                    }
                });
            } else if (action == SlotMenuItemAction.SAVE) {
                slotMenuItem.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        // TODO: clean up
                        File f = slot.getFile();
                        if (!SaveFile.isFileLoaded()) {
                            return;
                        }
                        File currentSaveFile
                                = SaveFile.getCurrentSaveFile().getSourceFile();
                        if (!currentSaveFile.getAbsolutePath().equals(f.getAbsolutePath()) && f.exists()) {
                            int option = JOptionPane.showOptionDialog(dialogParent,
                                    GUIUtils.formatHTMLString("Are you sure you want to overwrite \"" + f.getName() + "\"?"),
                                    "Comfirm Overwrite",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    null,
                                    null);
                            if (option != JOptionPane.YES_OPTION) {
                                return;
                            }
                        }
                        saveFile(f);
                    }
                });
            }
        }
        
        return slotMenuItems;
    }
    
    /**
     * Re-loads the pages on the tabbed pane. Different pages may be loaded
     * depending on whether a file is loaded. Re-loading a page ensures the
     * content on the page is up to date.
     */
    private void refreshPages()
    {
        /* Must be run in an "invokeLater()" thread since the GUI is updated and
           the method can be invoked at any time during the program's life */
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                List<Component> addedComponents = new ArrayList<>();
                Component selectedComponent = tabbedPane.getSelectedComponent();
                
                tabbedPane.removeAll();
                
                boolean addPage;
                for (Page p : pages) {
                    addPage = false;
                    
                    // Determine whether page should be added to the tabbed pane
                    switch (p.getVisibility()) {
                        case ALWAYS_VISIBLE:
                            addPage = true;
                            break;
                        case VISIBLE_WHEN_FILE_LOADED_ONLY:
                            if (SaveFile.isFileLoaded()) {
                                addPage = true;
                            }
                            break;
                        case VISIBLE_WHEN_FILE_NOT_LOADED_ONLY:
                            if (!SaveFile.isFileLoaded()) {
                                addPage = true;
                            }
                            break;
                    }
                    
                    // Load the page and add it to the tabbed pane
                    if (addPage) {
                        p.loadPage();
                        tabbedPane.addTab(p.getPageTitle(), p);
                        addedComponents.add(p);
                    }
                }
                
                /* Re-select the previously-selected component if it exists in
                   the tabbed pane and a save file is loaded */
                if (selectedComponent != null && SaveFile.isFileLoaded()) {
                    if (addedComponents.contains(selectedComponent)) {
                        tabbedPane.setSelectedComponent(selectedComponent);
                    }
                }
                
                pack();
            }
        });
    }
    
    /**
     * Closes the frame. If a file is open and has unsaved changes, the user
     * will be prompted to save them.
     */
    private void closeFrame()
    {   
        if (closeFile()) {
            Logger.info("Closing user interface...");
            
            Logger.debug("Exiting JVM with exit status 0...");
            System.exit(0);
        }
    }
    
    /**
     * Updates the frame title and modification status message based on whether
     * changes have been made to the current file. If changes have been made, an
     * asterisk (*) will be prepended to the frame title and the modification
     * status message will read "Modified."
     */
    private void updateFrameTitleAndModificationStatus()
    {
        final StringBuilder titleBuilder = new StringBuilder();
        final String modStatusText;
        final String modStatusToolTip;
        
        if (changesMade) {
            titleBuilder.append("*");
            modStatusText = "Modified";
            modStatusToolTip = "The file has unsaved changes.";
        } else {
            modStatusText = "";
            modStatusToolTip = null;
        }
        
        if (SaveFile.isFileLoaded()) {
            File currentFile = SaveFile.getCurrentSaveFile().getSourceFile();
            titleBuilder.append(currentFile).append(" - ");
        }
        
        titleBuilder.append(String.format("%s %s",
                Main.PROGRAM_TITLE, Main.PROGRAM_VERSION));
        
        
        /* Must be run in an "invokeLater()" thread since the GUI is updated and
           the method can be invoked at any time during the program's life */
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                setTitle(titleBuilder.toString());
                modificationStatusLabel.setText(modStatusText);
                modificationStatusLabel.setToolTipText(modStatusToolTip);
            }
        });
    }
    
    /**
     * Updates the platform status label in the status panel.
     */
    private void updatePlatformStatus()
    {
        final String platStatusText;
        final String platStatusToolTip;
        
        if (SaveFile.isFileLoaded()) {
            SaveFile currentSaveFile = SaveFile.getCurrentSaveFile();
            SaveFile.Platform p = currentSaveFile.getPlatform();
            
            platStatusText = p.getFriendlyName();
            platStatusToolTip = String.format("This is %s %s save file.",
                    p != SaveFile.Platform.PC && p != SaveFile.Platform.PS2
                            ? "an"      // Because grammar matters ;)
                            : "a",
                    p.getFriendlyName());
        } else {
            platStatusText = "";
            platStatusToolTip = null;
        }
        
        /* Must be run in an "invokeLater()" thread since the GUI is updated and
           the method can be invoked at any time during the program's life */
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                platformStatusLabel.setText(platStatusText);
                platformStatusLabel.setToolTipText(platStatusToolTip);
            }
        });
    }
    
    /**
     * Sets the status message. The status message appears in the bottom left
     * of the window.
     */
    private void setStatusMessage(final String s)
    {
        /* Must be run in an "invokeLater()" thread since the GUI is updated and
           the method can be invoked at any time during the program's life */
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                statusMessageLabel.setText(s);
                statusMessageLabel.setToolTipText(
                        s == null || s.isEmpty() ? null : s);
            }
        });
    }
    
    /**
     * Sets the "changes made" flag and updates the GUI to reflect the status of
     * the "changes made" flag.
     */
    private void setChangesMade(boolean changesMade)
    {
        this.changesMade = changesMade;
        updateFrameTitleAndModificationStatus();
    }
    
    /**
     * Closes the current file and closes all tabs related to file editing.
     * 
     * @return true if the current file was closed or if no file was loaded,
     *         false if the current file was not closed
     */
    private boolean closeFile()
    {
        if (SaveFile.isFileLoaded()) {
            // Ask user to save changes, end if user hits "Cancel"
            if (!promptSaveChanges()) {
                return false;
            }
        } else {
            // End if no file is loaded
            return true;
        }
        
        // Close the file
        File f = SaveFile.getCurrentSaveFile().getSourceFile();
        SaveFile.close();
        
        String message = "Closed file: " + f;
        Logger.info(message);
        
        Settings.storeRecentFiles(recentFiles);
        
        // Update GUI
        setStatusMessage(message);
        refreshMenus();
        refreshPages();
        setChangesMade(false);
        updatePlatformStatus();
        
        return true;
    }
    
    private void loadFile()
    {
        loadFile(null);
    }
    
    /**
     * Loads a file and opens all tabs related to file editing.
     * 
     * @param f the file to load
     */
    private void loadFile(File f)
    {
        // Attempt to close the current file; end if the user cancels
        if (!closeFile()) {
            return;
        }
        
        // Prompt for a file
        if (f == null) {
            f = promptForFile("Load File", "Open");
            if (f == null) {
                return;
            }
        }
                
        Logger.info("Loading file...");
        
        // Detect platform
        SaveFile.Platform platform;
        try {
            platform = SaveFile.getPlatform(f);
            Logger.debug("Detected platform: " + platform);
        } catch (IOException ex) {
            String errMsg = "An error occured while loading the file.";
            Logger.error("%s [%s: %s]\n", errMsg,
                    ex.getClass().getName(), ex.getMessage());
            Logger.stackTrace(ex);
            showErrorMessage(errMsg, "Error Loading File", ex);
            return;
        }
        
        // Show error message if file type not supported
        if (!platform.isSupported()) {
            String msg = "This file originates from a gaming platform which "
                    + "is not currently supported by this editor. "
                    + "(Platform: " + platform.getFriendlyName() + ")";
            Logger.info(msg);
            showErrorMessage(msg, "Platform Not Supported", null);
            return;
        }
        
        // Load file
        try {
            SaveFile.load(f, platform);
        } catch (IOException ex) {
            String errMsg = "An error occured while loading the file.";
            Logger.error(errMsg);
            Logger.stackTrace(ex);
            showErrorMessage(errMsg, "Error Loading File", ex);
            return;
        }
        
        /* Ensure that a QueueException is never thrown for attempting to add to
           a full queue */
        if (recentFiles.isFull()) {
            recentFiles.remove();
        }
        
        // Add this file to the list of recent files
        recentFiles.insert(f.getAbsolutePath());
        
        String message = "Loaded file: " + f;
        Logger.info(message);
        GUIUtils.showInformationMessageBox(this,
                "File loaded successfully!", "Success");
        
        crc = SaveFile.getCurrentSaveFile().getCRC32();
        Logger.debug("File checksum (CRC32): 0x%08x\n", crc);
        
        // Update GUI
        setStatusMessage(message);
        refreshMenus();
        refreshPages();
        setChangesMade(false);
        updatePlatformStatus();
    }
    
    /**
     * Saves the current file.
     */
    private void saveFile()
    {
        saveFile(SaveFile.getCurrentSaveFile().getSourceFile());
    }
    
    /**
     * Saves the current file to the specified destination file.
     */
    private void saveFile(File f)
    {
        Logger.info("Saving file...");
        
        // Save the file
        try {
            SaveFile.getCurrentSaveFile().save(f);
        } catch (IOException ex) {
            String errMsg = "An error occured while attempting to save the "
                    + "file.";
            Logger.error(errMsg);
            Logger.stackTrace(ex);
            showErrorMessage(errMsg, "Error Saving File", ex);
        }
        
        String message = "Saved file: " + f;
        Logger.info(message);
        
        // Update GUI
        setStatusMessage(message);
        setChangesMade(false);
        refreshSlotMenus();
    }
    
    /**
     * Asks the user if they want to delete the specified file, then deletes the
     * file if the user confirms.
     * 
     * @param f the file to delete
     */
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
        
        String message = "Deleted file: " + f;
        Logger.info(message);
        
        setStatusMessage(message);
        refreshSlotMenus();
    }
    
    private void checkForExternalChanges()
    {
        int newCRC;
        try {
            newCRC = SaveFile.getCurrentSaveFile().getSourceFileCRC32();
        } catch (IOException ex) {
            Logger.warn("Could not calculate CRC32 on source file [%s: %s]\n",
                    ex.getClass().getName(), ex.getMessage());
            Logger.stackTrace(ex);
            return;
        }
            
        if (newCRC == crc) {
            // No external changes made
            return;
        }

        File f = SaveFile.getCurrentSaveFile().getSourceFile();
        String[] options = new String[] {
            "Reload File", "Ignore and Continue"
        };
        String message = GUIUtils.formatHTMLString(
                "<b>Warning:</b> " + f + " has been modified by another "
                        + "program.\n\nWould you like to reload the file or "
                        + "ignore the changes and continue working with the "
                        + "current data? Reloading the file will destroy all "
                        + "unsaved changes.",
                350, false, null);
        
        Toolkit.getDefaultToolkit().beep();
        int option = JOptionPane.showOptionDialog(this,
                message,
                "External Changes Detected",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        
        if (option == 0) {
            setChangesMade(false);
            loadFile(f);
        } else {
            crc = newCRC;   // Prevent user from being prompted continuously
        }
    }
    
    /**
     * Prompts the user to choose a file from the filesystem. The parent
     * directory of the file chosen will be stored in the program configuration.
     * 
     * @param title the FileChooser dialog title
     * @param approveButtonText the text for the approve button
     * @return the selected file, null if the the user cancelled
     */
    private File promptForFile(String title, String approveButtonText)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        
        String parentDirPath = Settings.get(Settings.Key.LAST_LOCATION);
        
        if (parentDirPath != null) {
            fileChooser.setSelectedFile(new File(parentDirPath));
        }
        
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "GTA III-era Save Files (*.b)", "b"));
        
        int option = fileChooser.showDialog(this, approveButtonText);
        if (option != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        
        File f = fileChooser.getSelectedFile();
        
        Settings.set(Settings.Key.LAST_LOCATION, f.getAbsolutePath());
        
        return f;
    }
    
    /**
     * Asks the user if they want to save the changes made to the file, but only
     * if changes have been made. If the user selects "Yes", the file will be
     * saved.
     * 
     * @return a boolean value indicating whether or not the calling process
     *         should continue; true if "yes" or "no" selected, false otherwise
     */
    private boolean promptSaveChanges()
    {
        if (!changesMade) {
            return true;
        }
        
        // Prompt user to save changes
        int option = JOptionPane.showOptionDialog(this,
                GUIUtils.formatHTMLString("Do you want to save the changes "
                        + "made to this file?"),
                "Save Changes?",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        
        // Save changes if user approves
        if (option == JOptionPane.YES_OPTION) {
            File f = SaveFile.getCurrentSaveFile().getSourceFile();
            saveFile(f);
        }
        
        // Return true if "Yes" or "No" selected
        return option == JOptionPane.YES_OPTION
                || option == JOptionPane.NO_OPTION;
    }
    
    /**
     * Displays an error dialog box.
     * 
     * @param message the message to be shown on the dialog
     * @param title the title of the dialog
     * @param t (optional) the cause of the error, info about the Throwable
     *          object will be displayed on the dialog
     */
    private void showErrorMessage(String message, String title, Throwable t) {
        GUIUtils.showErrorMessageBox(this, message, title, t);
    }

    @Override
    public void update(Object message, Object... args)
    {
        /*
         * Handles page events.
         */
        
        if (!(message instanceof Page.Event)) {
            return;
        }
        
        Page.Event e = (Page.Event)message;
        Logger.debug("Page event: " + e);
        
        switch (e) {
            case VARIABLE_CHANGED:
                if (!changesMade) {
                    setChangesMade(true);
                }
                break;
            case FILE_LOAD:
                if (args[0] instanceof File && args[0] != null) {
                    loadFile((File)args[0]);
                }
                break;
            case FILE_DELETE:
                if (args[0] instanceof File && args[0] != null) {
                    deleteFile((File)args[0]);
                }
                break;
            case REFRESH_SLOTS:
                refreshSlotMenus();
                break;
        }
    }
    
    /**
     * Constants describing the possible actions for slot menu items.
     */
    private static enum SlotMenuItemAction
    {
        LOAD, SAVE;
    }
}
