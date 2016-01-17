package thehambone.gtatools.gta3savefileeditor.gui.page;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;
import thehambone.gtatools.gta3savefileeditor.gxt.GXT;
import thehambone.gtatools.gta3savefileeditor.gxt.GXTSelectorDialog;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAByte;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.Gang;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.PedType;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.BlockSimpleVars;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Mar 29, 2015.
 * 
 * @author thehambone
 */
public class GeneralPage extends Page
{
    private static final char GXT_INDICATOR = '\uFFFF';
    
    private Map<String, String> gxt;
    private JComponent[] timestampComponents;
    private BlockSimpleVars simp;
    private String prevTitleGXTKey;
    
    public GeneralPage()
    {
        super("General", Visibility.VISIBLE_WHEN_GAMESAVE_LOADED_ONLY);
        
        initComponents();
        initVariableComponentParameters();
        initSaveNameComponents();
        initTimestampComponents();
        initWeatherComponents();
        
        addNotifiersToComponents(mainPanel);
    }
    
    /*
     * Sets parameters for the various VariableComponents found on the page.
     */
    private void initVariableComponentParameters()
    {
        monthComboBox.setValueOffset(1);
        dayTextField.setUnsigned(true);
        yearTextField.setUnsigned(true);
        hourTextField.setDisplayFormat("%02d");
        hourTextField.setUnsigned(true);
        minuteTextField.setDisplayFormat("%02d");
        minuteTextField.setUnsigned(true);
        secondTextField.setDisplayFormat("%02d");
        secondTextField.setUnsigned(true);
        millisTextField.setUnsigned(true);
        gameTimeHourTextField.setDisplayFormat("%02d");
        gameTimeHourTextField.setUnsigned(true);
        gameTimeMinuteTextField.setDisplayFormat("%02d");
        gameTimeMinuteTextField.setUnsigned(true);
        globalTimerTextField.setUnsigned(true);
        weatherTimerTextField.setUnsigned(true);
        minuteLengthTextField.setUnsigned(true);
        weatherInterpolationSlider.setMinimum(0);
        weatherInterpolationSlider.setMaximum(1000);
        weatherInterpolationSlider.setScale(1000);
        weatherNeverChangesCheckBox.setDeselectedValue(-1);
    }
    
    /*
     * Sets up the components in the "Save Name" pane.
     */
    private void initSaveNameComponents()
    {
        // Place radio buttons in a button group.
        // This allows toggling between buttons.
        ButtonGroup bg = new ButtonGroup();
        bg.add(gxtKeyRadioButton);
        bg.add(textRadioButton);
        
        // Set up "GXT Key" radio button action listener
        gxtKeyRadioButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                gxtKeyRadioButtonAction(e);
            }
        });
        
        // Set up "Text" radio button action listener
        textRadioButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                textRadioButtonAction(e);
            }
        });
        
        // Set up "Select GXT String" button action listener
        selectGXTStringButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                selectGXTStringButtonAction(e);
            }
        });
    }
    
    /*
     * Sets up the components in the "Timestamp" pane.
     */
    private void initTimestampComponents()
    {
        // This is used to enable/disable all timestamp components
        timestampComponents = new JComponent[] {
            monthComboBox, dayTextField, dayOfWeekComboBox, yearTextField,
            hourTextField, minuteTextField, secondTextField, millisTextField,
            monthLabel, dayLabel, dayOfWeekLabel, yearLabel, hourLabel,
            minuteLabel, secondLabel, millisLabel
        };
    }
    
    /*
     * Sets up the components in the "Weather" pane.
     */
    @SuppressWarnings("unchecked")
    private void initWeatherComponents()
    {
        DefaultComboBoxModel<String> currentWeatherModel
                = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> previousWeatherModel
                = new DefaultComboBoxModel<>();
        
        // Populate combo boxes with weather types
        for (GameConstants.WeatherType w : GameConstants.WeatherType.values()) {
            currentWeatherModel.addElement(w.getFriendlyName());
            previousWeatherModel.addElement(w.getFriendlyName());
        }
        currentWeatherComboBox.setModel(currentWeatherModel);
        previousWeatherComboBox.setModel(previousWeatherModel);
        
        // Set up current weather combo box action listener
        currentWeatherComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                currentWeatherComboBoxAction(e);
            }
        });
        
        // Set up previous weather combo box action listener
        previousWeatherComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                previousWeatherComboBoxAction(e);
            }
        });
    }
    
    /*
     * Updates the save title text field and variable
     */
    private void updateSaveTitle(String str, boolean isGXTKey)
    {
        if (isGXTKey) {
            // Put text field in "GXT" mode
            saveTitleTextField.updateVariableOnChange(false);
            saveTitleTextField.setMaxInputLength(-1);
            saveTitleTextField.setEditable(false);
                
            String gxtValue = gxt.get(str);
            boolean isKeyValid = gxtValue != null;
            
            if (isKeyValid) {
                // Use GXT value as text field text
                saveTitleTextField.setText(gxtValue);
                saveTitleTextField.setToolTipText(gxtValue);
                
                prevTitleGXTKey = str;
                
                // Update variable with GXT key
                simp.szSaveName.setValue(GXT_INDICATOR + str);
                Logger.debug("Variable updated: " + simp.szSaveName);
            } else {
                saveTitleTextField.setText("<invalid GXT key>");
                saveTitleTextField.setToolTipText("Invalid GXT key.");
            }
        } else {
            // Put text field in "Text" mode
            int maxLen = simp.szSaveName.getMaxLength() - 1;
            saveTitleTextField.setMaxInputLength(maxLen);
            saveTitleTextField.setEditable(true);
            saveTitleTextField.updateVariableOnChange(true);
            
            // Update variable with string
            simp.szSaveName.setValue(str);
            Logger.debug("Variable updated: " + simp.szSaveName);
            
            // Refresh text field
            saveTitleTextField.refreshComponent();
            saveTitleTextField.setToolTipText(null);
        }
    }
    
    private void textRadioButtonAction(ActionEvent e)
    {
        selectGXTStringButton.setEnabled(false);
        updateSaveTitle(saveTitleTextField.getText(), false);
    }
    
    private void gxtKeyRadioButtonAction(ActionEvent e)
    {
        selectGXTStringButton.setEnabled(true);
        if (prevTitleGXTKey != null) {
            updateSaveTitle(prevTitleGXTKey, true);
        } else {
            updateSaveTitle(saveTitleTextField.getText(), true);
        }
    }
    
    private void selectGXTStringButtonAction(ActionEvent e)
    {
        // Show GXT Selector dialog
        Window parent = SwingUtilities.getWindowAncestor(this);
        GXTSelectorDialog gxtSelect = new GXTSelectorDialog(parent);
        String key = gxtSelect.showSelectionDialog(gxt);
        
        // Update save name if user made a selection
        if (key != null) {
            updateSaveTitle(key, true);
        }
    }
    
    private void currentWeatherComboBoxAction(ActionEvent e)
    {
        // Update rightmost label above weather interpolation slider
        currentWeatherSliderLabel.setText(
                currentWeatherComboBox.getSelectedItem().toString());
        
        /* Set the selected value of the "Never changes" checkbox equal to the
           ID of the current weather selection */
        weatherNeverChangesCheckBox.setSelectedValue(
                currentWeatherComboBox.getSelectedIndex());
        
        /* Write selected value from aboce to the "Never changes" checkbox
        variable if the checkbox is selected */
        if (weatherNeverChangesCheckBox.isSelected()) {
            weatherNeverChangesCheckBox.updateVariable();
        }
    }
    
    private void previousWeatherComboBoxAction(ActionEvent e)
    {
        // Update leftmost label above weather interpolation slider
        previousWeatherSliderLabel.setText(
                previousWeatherComboBox.getSelectedItem().toString());
    }
    
    @Override
    public void loadPage()
    {
        Logger.debug("Loading page: %s...\n", getTitle());
        
        // Disables page event notifiers; required for all pages
        isPageInitializing = true;
        
        gxt = GXT.getGXTTable();
        
        SaveFileNew saveFile = SaveFileNew.getCurrentSaveFile();
        simp = saveFile.simpleVars;
        
        boolean isPC = (saveFile.getPlatform() == SaveFileNew.Platform.PC);
        boolean isMobile
                = (saveFile.getPlatform() == SaveFileNew.Platform.ANDROID)
                        || (saveFile.getPlatform() == SaveFileNew.Platform.IOS);
        
        // Enable or disable timestamp components based on platform
        for (JComponent comp : timestampComponents) {
            comp.setEnabled(isPC);
        }
        
        // Enable GXT-related components for save name (mobile only)
        gxtKeyRadioButton.setVisible(isMobile);
        textRadioButton.setVisible(isMobile);
        selectGXTStringButton.setVisible(isMobile);
        
        // Set up "Save Name" page
        saveTitleTextField.setVariable(simp.szSaveName);
        saveTitleTextField.setMaxInputLength(simp.szSaveName.getMaxLength()- 1);
        if (isMobile) {
            if (gxt != null) {
                // Enable GXT features
                gxtKeyRadioButton.setEnabled(true);
                
                // Check whether save title is a GXT key
                if (simp.szSaveName.getValue().charAt(0) == GXT_INDICATOR) {
                    // Select "GXT Key" button and get GXT string
                    prevTitleGXTKey = simp.szSaveName.getValue().substring(1);
                    gxtKeyRadioButton.doClick();
                } else {
                    // Select "Text" button
                    textRadioButton.doClick();
                }
            } else {
                // GXT failed to load; disable GXT features
                gxtKeyRadioButton.setEnabled(false);
                textRadioButton.doClick();
            }
        }
        
        // Set variables for timestamp components (PC only)
        if (isPC) {
            monthComboBox.setVariable(simp.timestamp.nMonth);
            dayTextField.setVariable(simp.timestamp.nDay);
            dayOfWeekComboBox.setVariable(simp.timestamp.nDayOfWeek);
            yearTextField.setVariable(simp.timestamp.nYear);
            hourTextField.setVariable(simp.timestamp.nHour);
            minuteTextField.setVariable(simp.timestamp.nMinute);
            secondTextField.setVariable(simp.timestamp.nSecond);
            millisTextField.setVariable(simp.timestamp.nMillisecond);
        }
        
        // Set variables for all other VariableComponents
        gameTimeHourTextField.setVariable(simp.nGameHours);
        gameTimeMinuteTextField.setVariable(simp.nGameMinutes);
        globalTimerTextField.setVariable(simp.nTimeInMillis);
        weatherTimerTextField.setVariable(simp.nLastClockTick);
        minuteLengthTextField.setVariable(simp.nGameMinuteLengthMillis);
        weatherInterpolationSlider.setVariable(simp.fWeatherInterpolationValue);
        currentWeatherComboBox.setVariable(simp.nCurrentWeatherType);
        previousWeatherComboBox.setVariable(simp.nPreviousWeatherType);
        weatherNeverChangesCheckBox.setVariable(simp.nForcedWeatherType);
        
        // Detect bugs/glitches
//        detectPurpleNinesGlitch();
//        detectPedHostilityCheatsEnabled();
        
        // Re-enables page event notifiers; required for all pages
        isPageInitializing = false;
    }
    
    private void detectPurpleNinesGlitch()
    {
        boolean glitchPresent = false;
        Gang hoods = vars.aGangs.getValueAt(GameConstants.Gang.GANG07.getID());
        if (hoods.getPedModelOverrideFlagAsVariable().getValue().byteValue() != -1) {
            glitchPresent = true;
        }
        purpleNinesGlitchFixButton.setText(glitchPresent ? "Fix" : "Not present");
        purpleNinesGlitchFixButton.setEnabled(glitchPresent);
    }
    
    private void detectPedHostilityCheatsEnabled()
    {
        boolean enabled = false;
        for (GameConstants.PedType pedType : GameConstants.PedType.values()) {
            PedType savedPedType = vars.aPedTypes.getValueAt(pedType.getID());
            int threatValue = savedPedType.getThreat();
            if (pedType.getID() > -1 && pedType.getID() < 4) {
                continue;   // skip players
            }
            if (pedType.getID() > 6 && pedType.getID() < 16) {
                continue;   // skip gangs
            }
            if ((threatValue & GameConstants.PedType.PLAYER01.getThreatNumber()) != 0
                    || (threatValue & GameConstants.PedType.CIVMALE.getThreatNumber()) != 0
                    || (threatValue & GameConstants.PedType.CIVFEMALE.getThreatNumber()) != 0) {
                // if ped type is hostile toward player, civmale, or civfemale...
                // one of the cheats was most likely enabled
                enabled = true;
                break;
            }
        }
        reversePedHostilityCheatsButton.setText(enabled ? "Fix" : "Cheats not enabled");
        reversePedHostilityCheatsButton.setEnabled(enabled);
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
        saveTitlePanel = new javax.swing.JPanel();
        saveTitleTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.StringVariableTextField();
        gxtKeyRadioButton = new javax.swing.JRadioButton();
        textRadioButton = new javax.swing.JRadioButton();
        selectGXTStringButton = new javax.swing.JButton();
        gameTimePanel = new javax.swing.JPanel();
        gameTimeHourLabel = new javax.swing.JLabel();
        gameTimeHourTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        globalTimerLabel = new javax.swing.JLabel();
        globalTimerTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        gameTimeMinuteLabel = new javax.swing.JLabel();
        gameTimeMinuteTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        weatherTimerLabel = new javax.swing.JLabel();
        weatherTimerTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        minuteLengthLabel = new javax.swing.JLabel();
        minuteLengthTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        msLabel = new javax.swing.JLabel();
        timestampPanel = new javax.swing.JPanel();
        monthLabel = new javax.swing.JLabel();
        monthComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox();
        dayLabel = new javax.swing.JLabel();
        dayTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        dayOfWeekLabel = new javax.swing.JLabel();
        dayOfWeekComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox();
        yearLabel = new javax.swing.JLabel();
        yearTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        hourLabel = new javax.swing.JLabel();
        hourTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        minuteLabel = new javax.swing.JLabel();
        minuteTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        secondLabel = new javax.swing.JLabel();
        secondTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        millisLabel = new javax.swing.JLabel();
        millisTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        glitchAndBugFixesPanel = new javax.swing.JPanel();
        purpleNinesGlitchLabel = new javax.swing.JLabel();
        purpleNinesGlitchFixButton = new javax.swing.JButton();
        reversePedHostilityCheatsLabel = new javax.swing.JLabel();
        reversePedHostilityCheatsButton = new javax.swing.JButton();
        weatherPanel = new javax.swing.JPanel();
        currentWeatherSelectionLabel = new javax.swing.JLabel();
        currentWeatherComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.old.VariableValueComboBox();
        previousWeatherSelectionLabel = new javax.swing.JLabel();
        previousWeatherComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.old.VariableValueComboBox();
        previousWeatherSliderLabel = new javax.swing.JLabel();
        currentWeatherSliderLabel = new javax.swing.JLabel();
        weatherInterpolationSlider = new thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableSlider();
        weatherNeverChangesCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableCheckBox();

        saveTitlePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Save Name"));

        saveTitleTextField.setText("a");

        gxtKeyRadioButton.setText("GXT String");

        textRadioButton.setText("Text");

        selectGXTStringButton.setText("Select GXT String");

        javax.swing.GroupLayout saveTitlePanelLayout = new javax.swing.GroupLayout(saveTitlePanel);
        saveTitlePanel.setLayout(saveTitlePanelLayout);
        saveTitlePanelLayout.setHorizontalGroup(
            saveTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(saveTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(saveTitlePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(textRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(gxtKeyRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectGXTStringButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(saveTitlePanelLayout.createSequentialGroup()
                        .addComponent(saveTitleTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        saveTitlePanelLayout.setVerticalGroup(
            saveTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(saveTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gxtKeyRadioButton)
                    .addComponent(textRadioButton)
                    .addComponent(selectGXTStringButton))
                .addContainerGap())
        );

        gameTimePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Time"));

        gameTimeHourLabel.setText("Hour:");

        gameTimeHourTextField.setText("0");

        globalTimerLabel.setText("Global timer:");

        globalTimerTextField.setText("0");

        gameTimeMinuteLabel.setText("Minute:");
        gameTimeMinuteLabel.setToolTipText("");

        gameTimeMinuteTextField.setText("0");
        gameTimeMinuteTextField.setToolTipText("");

        weatherTimerLabel.setText("Weather timer:");

        weatherTimerTextField.setText("0");

        minuteLengthLabel.setText("Minute length:");

        minuteLengthTextField.setText("0");

        msLabel.setText("ms");

        javax.swing.GroupLayout gameTimePanelLayout = new javax.swing.GroupLayout(gameTimePanel);
        gameTimePanel.setLayout(gameTimePanelLayout);
        gameTimePanelLayout.setHorizontalGroup(
            gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameTimePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gameTimePanelLayout.createSequentialGroup()
                        .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(gameTimeHourLabel)
                            .addComponent(gameTimeMinuteLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(gameTimeHourTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(gameTimeMinuteTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(weatherTimerLabel)
                            .addComponent(globalTimerLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(globalTimerTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(weatherTimerTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)))
                    .addGroup(gameTimePanelLayout.createSequentialGroup()
                        .addComponent(minuteLengthLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minuteLengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(msLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        gameTimePanelLayout.setVerticalGroup(
            gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameTimePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gameTimeHourLabel)
                    .addComponent(globalTimerLabel)
                    .addComponent(gameTimeHourTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(globalTimerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gameTimeMinuteLabel)
                    .addComponent(weatherTimerLabel)
                    .addComponent(gameTimeMinuteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weatherTimerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minuteLengthLabel)
                    .addComponent(msLabel)
                    .addComponent(minuteLengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        timestampPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Save Timestamp"));

        monthLabel.setText("Month:");

        monthComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jan.", "Feb.", "Mar.", "Apr.", "May", "Jun.", "Jul.", "Aug.", "Sep.", "Oct.", "Nov.", "Dec." }));

        dayLabel.setText("Day:");

        dayTextField.setText("0");

        dayOfWeekLabel.setText("Day of week:");

        dayOfWeekComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sun.", "Mon.", "Tue.", "Wed.", "Thu.", "Fri.", "Sat." }));

        yearLabel.setText("Year:");

        yearTextField.setText("0");

        hourLabel.setText("Hour:");
        hourLabel.setToolTipText("");

        hourTextField.setText("0");

        minuteLabel.setText("Minute:");
        minuteLabel.setToolTipText("");

        minuteTextField.setText("0");

        secondLabel.setText("Second:");

        secondTextField.setText("0");

        millisLabel.setText("Millis.:");

        millisTextField.setText("0");

        javax.swing.GroupLayout timestampPanelLayout = new javax.swing.GroupLayout(timestampPanel);
        timestampPanel.setLayout(timestampPanelLayout);
        timestampPanelLayout.setHorizontalGroup(
            timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timestampPanelLayout.createSequentialGroup()
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, timestampPanelLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(monthLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monthComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, timestampPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(hourLabel)
                            .addComponent(dayOfWeekLabel)
                            .addComponent(secondLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(hourTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(secondTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dayOfWeekComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(yearLabel)
                    .addComponent(dayLabel)
                    .addComponent(minuteLabel)
                    .addComponent(millisLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dayTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(yearTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(minuteTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(millisTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        timestampPanelLayout.setVerticalGroup(
            timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, timestampPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(monthLabel)
                    .addComponent(dayLabel)
                    .addComponent(dayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yearLabel)
                    .addComponent(dayOfWeekLabel)
                    .addComponent(yearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dayOfWeekComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hourLabel)
                    .addComponent(minuteLabel)
                    .addComponent(hourTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minuteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(secondLabel)
                    .addComponent(millisLabel)
                    .addComponent(secondTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(millisTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        glitchAndBugFixesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Glitch and Bug Fixes"));

        purpleNinesGlitchLabel.setText("Purple Nines glitch:");

        purpleNinesGlitchFixButton.setText("Not present");
        purpleNinesGlitchFixButton.setToolTipText("Disable the Purple Nine's glitch, allowing you to progress on D-Ice's missions.");
        purpleNinesGlitchFixButton.setEnabled(false);
        purpleNinesGlitchFixButton.setMaximumSize(new java.awt.Dimension(127, 23));
        purpleNinesGlitchFixButton.setMinimumSize(new java.awt.Dimension(127, 23));
        purpleNinesGlitchFixButton.setPreferredSize(new java.awt.Dimension(127, 23));
        purpleNinesGlitchFixButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                purpleNinesGlitchFixButtonActionPerformed(evt);
            }
        });

        reversePedHostilityCheatsLabel.setText("Reverse ped hostility cheats:");

        reversePedHostilityCheatsButton.setText("Cheats not enabled");
        reversePedHostilityCheatsButton.setToolTipText("Reverse the effects of the \"peds riot\" and \"peds attack player\" cheats.");
        reversePedHostilityCheatsButton.setEnabled(false);
        reversePedHostilityCheatsButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                reversePedHostilityCheatsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout glitchAndBugFixesPanelLayout = new javax.swing.GroupLayout(glitchAndBugFixesPanel);
        glitchAndBugFixesPanel.setLayout(glitchAndBugFixesPanelLayout);
        glitchAndBugFixesPanelLayout.setHorizontalGroup(
            glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(glitchAndBugFixesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(reversePedHostilityCheatsLabel)
                    .addComponent(purpleNinesGlitchLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(purpleNinesGlitchFixButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(reversePedHostilityCheatsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        glitchAndBugFixesPanelLayout.setVerticalGroup(
            glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(glitchAndBugFixesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(purpleNinesGlitchLabel)
                    .addComponent(purpleNinesGlitchFixButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reversePedHostilityCheatsButton)
                    .addComponent(reversePedHostilityCheatsLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        purpleNinesGlitchFixButton.getAccessibleContext().setAccessibleName("Not Present");

        weatherPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Weather"));

        currentWeatherSelectionLabel.setText("Current weather:");

        currentWeatherComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<weather_name>" }));

        previousWeatherSelectionLabel.setText("Previous weather:");

        previousWeatherComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<weather_name>" }));

        previousWeatherSliderLabel.setText("WeatherA");

        currentWeatherSliderLabel.setText("WeatherB");

        weatherNeverChangesCheckBox.setText("Never changes");

        javax.swing.GroupLayout weatherPanelLayout = new javax.swing.GroupLayout(weatherPanel);
        weatherPanel.setLayout(weatherPanelLayout);
        weatherPanelLayout.setHorizontalGroup(
            weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weatherPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(weatherInterpolationSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addGroup(weatherPanelLayout.createSequentialGroup()
                        .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(weatherPanelLayout.createSequentialGroup()
                                .addComponent(currentWeatherSelectionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(currentWeatherComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(weatherPanelLayout.createSequentialGroup()
                                .addComponent(previousWeatherSelectionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(previousWeatherComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(weatherPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(weatherNeverChangesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(weatherPanelLayout.createSequentialGroup()
                        .addComponent(previousWeatherSliderLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(currentWeatherSliderLabel)))
                .addContainerGap())
        );
        weatherPanelLayout.setVerticalGroup(
            weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weatherPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentWeatherSelectionLabel)
                    .addComponent(currentWeatherComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weatherNeverChangesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previousWeatherSelectionLabel)
                    .addComponent(previousWeatherComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previousWeatherSliderLabel)
                    .addComponent(currentWeatherSliderLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weatherInterpolationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(gameTimePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(glitchAndBugFixesPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveTitlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(timestampPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(weatherPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(timestampPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weatherPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(saveTitlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gameTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(glitchAndBugFixesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    private void purpleNinesGlitchFixButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_purpleNinesGlitchFixButtonActionPerformed
    {//GEN-HEADEREND:event_purpleNinesGlitchFixButtonActionPerformed
        if (vars == null) {
            return;
        }
        int hoodsID = GameConstants.Gang.GANG07.getID();
        Gang hoods = vars.aGangs.getValueAt(hoodsID);
        hoods.getPedModelOverrideFlagAsVariable().setValue(new GTAByte((byte)-1));
        purpleNinesGlitchFixButton.setText("Fixed");
        purpleNinesGlitchFixButton.setEnabled(false);
    }//GEN-LAST:event_purpleNinesGlitchFixButtonActionPerformed

    private void reversePedHostilityCheatsButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_reversePedHostilityCheatsButtonActionPerformed
    {//GEN-HEADEREND:event_reversePedHostilityCheatsButtonActionPerformed
        if (vars == null) {
            return;
        }
        for (GameConstants.PedType pedType : GameConstants.PedType.values()) {
            PedType savedPedType = vars.aPedTypes.getValueAt(pedType.getID());
            int threatValue = savedPedType.getThreat();
            if (pedType.getID() > 6 && pedType.getID() < 16) {
                // If pedtype is a gang...
                threatValue &= ~pedType.getThreatNumber();  // make gang not hostile towards itself
                if (pedType == GameConstants.PedType.GANG04 || pedType == GameConstants.PedType.GANG07) {
                    threatValue &= ~GameConstants.PedType.PLAYER01.getThreatNumber();   // make Yakuza and Hoods not hostile towards the player
                }
            } else {
                // If pedtype is anything else...
                // make pedtype not hostile toward gangs or the player
                threatValue &= ~GameConstants.PedType.PLAYER01.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG01.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG02.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG03.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG04.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG05.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG06.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG07.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG08.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG09.getThreatNumber();
            }
            // make pedtype not hostile toward anyone else
            threatValue &= ~GameConstants.PedType.PLAYER02.getThreatNumber();
            threatValue &= ~GameConstants.PedType.PLAYER03.getThreatNumber();
            threatValue &= ~GameConstants.PedType.PLAYER04.getThreatNumber();
            threatValue &= ~GameConstants.PedType.CIVMALE.getThreatNumber();
            threatValue &= ~GameConstants.PedType.CIVFEMALE.getThreatNumber();
            threatValue &= ~GameConstants.PedType.COP.getThreatNumber();
            threatValue &= ~GameConstants.PedType.EMERGENCY.getThreatNumber();
            threatValue &= ~GameConstants.PedType.FIREMAN.getThreatNumber();
            threatValue &= ~GameConstants.PedType.CRIMINAL.getThreatNumber();
            threatValue &= ~GameConstants.PedType.SPECIAL01.getThreatNumber();
            threatValue &= ~GameConstants.PedType.PROSTITUTE.getThreatNumber();
            threatValue &= ~GameConstants.PedType.SPECIAL02.getThreatNumber();
            savedPedType.setThreat(threatValue);
            vars.aPedTypes.setValueAt(pedType.getID(), savedPedType);
        }
        reversePedHostilityCheatsButton.setText("Fixed");
        reversePedHostilityCheatsButton.setEnabled(false);
    }//GEN-LAST:event_reversePedHostilityCheatsButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private thehambone.gtatools.gta3savefileeditor.gui.component.old.VariableValueComboBox currentWeatherComboBox;
    private javax.swing.JLabel currentWeatherSelectionLabel;
    private javax.swing.JLabel currentWeatherSliderLabel;
    private javax.swing.JLabel dayLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox dayOfWeekComboBox;
    private javax.swing.JLabel dayOfWeekLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField dayTextField;
    private javax.swing.JLabel gameTimeHourLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField gameTimeHourTextField;
    private javax.swing.JLabel gameTimeMinuteLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField gameTimeMinuteTextField;
    private javax.swing.JPanel gameTimePanel;
    private javax.swing.JPanel glitchAndBugFixesPanel;
    private javax.swing.JLabel globalTimerLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField globalTimerTextField;
    private javax.swing.JRadioButton gxtKeyRadioButton;
    private javax.swing.JLabel hourLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField hourTextField;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel millisLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField millisTextField;
    private javax.swing.JLabel minuteLabel;
    private javax.swing.JLabel minuteLengthLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField minuteLengthTextField;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField minuteTextField;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox monthComboBox;
    private javax.swing.JLabel monthLabel;
    private javax.swing.JLabel msLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.old.VariableValueComboBox previousWeatherComboBox;
    private javax.swing.JLabel previousWeatherSelectionLabel;
    private javax.swing.JLabel previousWeatherSliderLabel;
    private javax.swing.JButton purpleNinesGlitchFixButton;
    private javax.swing.JLabel purpleNinesGlitchLabel;
    private javax.swing.JButton reversePedHostilityCheatsButton;
    private javax.swing.JLabel reversePedHostilityCheatsLabel;
    private javax.swing.JPanel saveTitlePanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.StringVariableTextField saveTitleTextField;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel secondLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField secondTextField;
    private javax.swing.JButton selectGXTStringButton;
    private javax.swing.JRadioButton textRadioButton;
    private javax.swing.JPanel timestampPanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableSlider weatherInterpolationSlider;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableCheckBox weatherNeverChangesCheckBox;
    private javax.swing.JPanel weatherPanel;
    private javax.swing.JLabel weatherTimerLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField weatherTimerTextField;
    private javax.swing.JLabel yearLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField yearTextField;
    // End of variables declaration//GEN-END:variables
}